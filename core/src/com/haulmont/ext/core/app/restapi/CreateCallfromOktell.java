/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.ext.core.app.restapi;


import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.Category;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.restapi.Authentication;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.docflow.core.app.NumerationService;
import com.haulmont.docflow.core.entity.FieldInfo;
import com.haulmont.ext.core.app.CallGeneratorNumberService;
import com.haulmont.ext.core.app.CallGeneratorNumberServiceBean;
import com.haulmont.ext.core.entity.Call;
import com.haulmont.ext.core.entity.ExtClient;
import com.haulmont.taskman.core.app.TaskmanService;
import com.haulmont.taskman.core.entity.Priority;
import com.haulmont.workflow.core.app.WfService;
import com.haulmont.workflow.core.entity.CardRole;
import com.haulmont.workflow.core.entity.Proc;
import com.haulmont.workflow.core.entity.ProcRole;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: mahdi
 *
 * Date: 10/18/13
 * Time: 1:09 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class CreateCallfromOktell {
    protected static Log log = LogFactory.getLog(CreateCallfromOktell.class);

    @Inject
    private TaskmanService taskmanService;

    @Inject
    private NumerationService numerationService;

    @Inject
    private WfService wfService;

    @Inject
    private DataService dataService;

    @Inject
    private CallGeneratorNumberService callGNS;

    private List<FieldInfo> fieldInfos;

    @RequestMapping(method = RequestMethod.GET, value = "/task/call")
    public void getTaskStates(HttpServletResponse response, @RequestParam(value = "s") String sessionId, @RequestParam(value = "p") String phone) throws IOException, JSONException {
        Authentication authentication = Authentication.me(sessionId);
        if (authentication == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        try {

            Call call = new Call();

            call.setName("Входящий звонок:" + phone);

            LoadContext catctx = new LoadContext(Category.class);
            catctx.setQueryString("select t from sys$Category t where t.id = :uuid").addParameter("uuid", UUID.fromString("053ae7ee-d327-4a90-909a-f540e4357324"));
            DataService catService = Locator.lookup(DataService.NAME);
            Category cat =  catService.load(catctx);
            call.setCategory(cat);

            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String stringDate = formatter.format(TimeProvider.currentTimestamp());
            call.setCallDescription("Входящий звонок поступивший в: " + stringDate);

            Priority priority = findPriorityByOrder(3);
            call.setPriority(priority);
            call.setNumber(callGNS.getNextNumber(call));

            LoadContext procctx = new LoadContext(Proc.class);
            procctx.setQueryString("select p from wf$Proc p where p.name =:name").addParameter("name", "Обработка звонка");
            procctx.setView("edit");
            DataService procService = Locator.lookup(DataService.NAME);
            Proc proc =  procService.load(procctx);
            call.setProc(proc);

            String roleInitiator = "oktell";
            String roleExecutor = "office_manager";

            String initiator = findUserByRole(roleInitiator).getLogin();
            String executor = findUserByRole(roleExecutor).getLogin();

            if (call.getRoles() == null) {
                call.setRoles(new ArrayList<CardRole>());
            }

            for (ProcRole procRole : proc.getRoles()) {
                if ((roleInitiator).equals(procRole.getCode()) && initiator != null) {
                    updateCardRole(procRole, call, initiator);
                }   else if ((roleExecutor).equals(procRole.getCode()) && executor != null)
                    updateCardRole(procRole, call, executor);
            }

            Set<Entity> toCommit = new HashSet<Entity>();

            MetaClass metaClass = call.getMetaClass();
            for (MetaProperty metaProperty : metaClass.getProperties()) {
                Object value = call.getValue(metaProperty.getName());
                if (value instanceof List) {
                    for (Object entity : ((List) value))
                        toCommit.add((Entity) entity);
                }
            }

            LoadContext extClt = new LoadContext(ExtClient.class);
            extClt.setQueryString("select c from ext$Client c where c.numberTel = :phone").addParameter("phone", phone);
            extClt.setView("_local");
            DataService dataServiceClt = Locator.lookup(DataService.NAME);
            ExtClient extClient = dataServiceClt.load(extClt);

            if (extClient != null) {
                call.setExtClient(extClient);
                call.setTelephoneNumber(extClient.getNumberTel());
                call.setCallDescription("Входящий звонок поступивший в: " + stringDate + " от " + extClient.getName());
            }
            call.setTelephoneNumber(phone);

            //List<CardRole> roleList = call.getRoles().get(0).getProcRole().;

            toCommit.add(call);
            dataService.commit(new CommitContext(toCommit));

            wfService.startProcess(call);

            response.setStatus(HttpServletResponse.SC_OK);
            PrintWriter writer = new PrintWriter(response.getOutputStream());
            JSONObject responseJson = new JSONObject();
            responseJson.put("id", call.getId());
            writer.write(responseJson.toString());
            writer.close();

        } finally {
            authentication.forget();
        }
    }

    private User findUserByRole(String role) {
        LoadContext userctx = new LoadContext(User.class);
        userctx.setQueryString("select u from sec$User u where u.id in " +
                "(select ur.user.id from sec$UserRole ur where ur.deleteTs is null and ur.role.id in " +
                "(select r.id from sec$Role r where r.name =:name))").addParameter("name", role);
        userctx.setView("_local");
        DataService userService = Locator.lookup(DataService.NAME);
        User user =  userService.load(userctx);
        return user;
    }

    private Priority findPriorityByOrder(Integer priority) {
        LoadContext ctx = new LoadContext(Priority.class);
        ctx.setQueryString("select p from tm$Priority p where p.orderNo = :oNo")
                .addParameter("oNo", priority);
        ctx.setView("_local");
        DataService dataService = Locator.lookup(DataService.NAME);
        List<Priority> entities = dataService.loadList(ctx);
        return entities.isEmpty() ? null : entities.iterator().next();
    }

    private void updateCardRole(ProcRole procRole, Call call, String userForRole) {
        List<CardRole> roles = call.getRoles();
        CardRole cardRole = null;
        for (CardRole role : roles) {
            if (role.getCode().equals(procRole.getCode())) {
                cardRole = role;
                break;
            }
        }
        if (!StringUtils.isEmpty(userForRole)) {
            User user = findUserByNameOrEmail(userForRole);
            if (user == null) {
                throw new RuntimeException("Unknown user: " + userForRole);
            }
            if (cardRole == null) {
                procRole.setCreateTs(TimeProvider.currentTimestamp());
                procRole.setCreatedBy("system");
                cardRole = createCardRole(procRole, call, user);
                call.getRoles().add(cardRole);
            } else {
                cardRole.setUser(user);
            }
        }
    }

    private CardRole createCardRole(ProcRole procRole, Call call, User user) {
        CardRole newCardRole = MetadataProvider.create(CardRole.class);
        newCardRole.setUser(user);
        newCardRole.setProcRole(procRole);
        newCardRole.setCode(procRole.getCode());
        newCardRole.setCard(call);
        newCardRole.setNotifyByEmail(true);
        return newCardRole;
    }

    private User findUserByNameOrEmail(String user) {
        LoadContext ctx = new LoadContext(User.class);
        ctx.setQueryString("select u from sec$User u where u.loginLowerCase = :name or u.email = :name")
                .addParameter("name", user.toLowerCase());
        ctx.setView("_local");
        DataService dataService = Locator.lookup(DataService.NAME);
        List<User> entities = dataService.loadList(ctx);
        return entities.isEmpty() ? null : entities.iterator().next();

    }

}
