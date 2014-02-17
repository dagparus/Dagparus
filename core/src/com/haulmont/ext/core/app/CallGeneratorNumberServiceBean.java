/*
 * Copyright (c) 2014 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.ext.core.app;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.docflow.core.app.NumerationWorker;
import com.haulmont.docflow.core.entity.Numerator;
import com.haulmont.ext.core.entity.Call;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mahdi on 1/31/14.
 */
@Service(CallGeneratorNumberService.NAME)
public class CallGeneratorNumberServiceBean implements CallGeneratorNumberService {

    @Inject
    private Persistence persistence;

    @Inject
    private NumerationWorker numerationWorker;

    @Override
    public String getNextNumber(Call call) {
        
        Transaction tx = persistence.createTransaction();
        try {
            String numeratorName = "Звонки";
            String num = null;
            if (StringUtils.isNotBlank(numeratorName)) {
                EntityManager em = persistence.getEntityManager();
                Query q = em.createQuery("select n from df$Numerator n where n.name = ?1");
                q.setParameter(1, numeratorName);
                List<Numerator> list = q.getResultList();
                if (!list.isEmpty()) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("entity", call);
                    num = numerationWorker.getNextNumber(list.get(0), map);
                }
            }
            tx.commit();
            return num;
        } finally {
            tx.end();
        }
    }
}

