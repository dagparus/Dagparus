/*
 * Copyright (c) 2014 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.ext.web.ui.TreeCompany;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.ext.core.entity.Aread;
import com.haulmont.ext.core.entity.CompanyExt;
import com.haulmont.ext.core.entity.Republic;
import com.haulmont.ext.core.entity.Villa;
import com.vaadin.ui.Select;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mahdi on 1/16/14.
 */
public class CompanyExtLookup extends AbstractLookup {

    @Inject
    protected CollectionDatasource republicDs, areadDs, villaDs;

    protected TextField searchSimpleText;

    @Inject
    protected HierarchicalDatasource extCompanysDsH;

    @Named("republic")
    protected LookupField republicField;
    @Named("aread")
    protected LookupField areadField;
    @Named("villa")
    protected LookupField villaField;

    protected TreeTable tree;

    protected CollectionDatasource treeDS;

    public Boolean needShowNotification = true;

    public CompanyExtLookup(IFrame frame) {
        super(frame);
    }

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        LoadContext ctx = new LoadContext(CompanyExt.class);
        List<CompanyExt> companiesExt = null;
        extCompanysDsH = getDsContext().get("extCompanysDsH");

        republicDs.refresh();
        areadDs = getDsContext().get("areadDs");
        villaDs = getDsContext().get("villaDs");
        areadDs.refresh();
        villaDs.refresh();
        final Republic republic = (Republic) params.get("param$republic");
        final Aread aread = (Aread) params.get("param$aread");
        final Villa villa= (Villa) params.get("param$villa");

     /*   if (params.containsKey("param$republic") && params.containsKey("param$aread") && params.containsKey("param$villa")) {
            extCompanysDsH.setQuery("select d from ext$CompanyExt d where d.republic.regionname ='"+republic.getRegionname()+"' and d.aread.areadname ='"+aread.getAreadname()+
                    "' and d.villa.villaname ='"+villa.getVillaname()+"' and not d.id in (:custom$extCompanys) order by d.name");
        } else if (params.containsKey("param$republic") &&  params.containsKey("param$villa")) {
            extCompanysDsH.setQuery("select d from ext$CompanyExt d where d.republic.regionname ='"+republic.getRegionname()+"' and d.villa.villaname ='"+villa.getVillaname()+
                    "' and not d.id in (:custom$extCompanys) order by d.name");
        } else {
            extCompanysDsH.setQuery("select d from ext$CompanyExt d where d.republic.regionname ='"+republic.getRegionname()+
                    "' and not d.id in (:custom$extCompanys) order by d.name");
        }    */

        ((Select) WebComponentsHelper.unwrap(republicField)).setNullSelectionAllowed(false);

      /*  if (params.containsKey("visibleTypeCompanyField"))
            republicField.setEnabled((Boolean) params.get("visibleTypeCompanyField") == true ? true : false);  */
        if (republic != null)
            republicField.setValue(republic);
     /*   if (params.containsKey("visibleTypeCompanyField"))
            areadField.setEnabled((Boolean) params.get("visibleTypeCompanyField") == true ? true : false);
        if (aread != null)
            areadField.setValue(aread);
        if (params.containsKey("visibleTypeCompanyField"))
            villaField.setEnabled((Boolean) params.get("visibleTypeCompanyField") == true ? true : false);
        if (villa != null)
            villaField.setValue(villa);   */

        tree = getComponent("extCompanysTable");
        treeDS = tree.getDatasource();

        final CompanyExt parentCompanyExt = (CompanyExt) params.get("extCompany");
        refreshTree(parentCompanyExt, needShowNotification);

        republicDs.addListener(new DsListenerAdapter() {
            @Override
            public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
                if (searchSimpleText.getValue() != null && searchSimpleText.getValue() != "")
                    refreshTree(parentCompanyExt, true);
            }
        });

        final Button searchSimple = getComponent("searchSimple");
        searchSimpleText = getComponent("searchSimpleText");
        searchSimple.setAction(new AbstractAction(ActionsField.OPEN) {
            public void actionPerform(Component component) {
                refreshTree(parentCompanyExt, true);
            }

            @Override
            public String getCaption() {
                return getMessage("actions.Apply");
            }
        });
        if (republicField.getValue() != null)
            searchSimpleText.requestFocus();
        else
            republicField.requestFocus();
    }


    private void refreshTree(CompanyExt parent, Boolean needShowNotification) {
        List<CompanyExt> excludeExtCompanys = new ArrayList<CompanyExt>();
        if (parent != null) {
            excludeExtCompanys.add(parent);
            getChildExtCompanys(parent, excludeExtCompanys);
        }

        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("extCompanys", excludeExtCompanys);

        treeDS.refresh(paramsMap);
        if (needShowNotification && treeDS.getItemIds().size() == 0) {
            showNotification(getMessage("validTreeCompanys"), NotificationType.HUMANIZED);
        }
        tree.expandAll();
    }

    private void getChildExtCompanys(CompanyExt parent, List<CompanyExt> extCompanys) {
        LoadContext ctx = new LoadContext(CompanyExt.class).setView("browse");
        ctx.setQueryString("select p from ext$CompanyExt p where p.parentCompanyExt.id = :parent").addParameter("parent", parent);
        List<CompanyExt> extCompanyList = ServiceLocator.getDataService().loadList(ctx);
        extCompanys.addAll(extCompanyList);
        for (CompanyExt group : extCompanyList) {
            getChildExtCompanys(group, extCompanys);
        }
    }
}