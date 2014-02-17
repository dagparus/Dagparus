package com.haulmont.ext.web.ui.TreeCompany;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.bali.datastruct.Tree;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.AbstractTreeTableDatasource;
import com.haulmont.ext.core.entity.CompanyExt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CompanyExtDatasource extends AbstractTreeTableDatasource
{
    public CompanyExtDatasource(DsContext context, DataService dataservice, String id, MetaClass metaClass, String viewName) {
        super(context, dataservice, id, metaClass, viewName);
    }

    @Override
    protected Tree loadTree(Map params) {
        LoadContext ctx = new LoadContext(metaClass).setView("browse");
        List<CompanyExt> companiesExt = null;

        if (params.containsKey("republic") && params.containsKey("aread") && params.containsKey("villa")) {
            LoadContext.Query query = new LoadContext.Query("select d from ext$CompanyExt d where d.republic.id = :republic and d.aread.id = :aread " +
                    " and d.villa.id = :villa order by d.name");
            query.setParameters(params);
            ctx.setQuery(query);
            companiesExt = ServiceLocator.getDataService().loadList(ctx);
        } else if (params.containsKey("republic") && params.containsKey("villa")) {
            LoadContext.Query query = new LoadContext.Query("select d from ext$CompanyExt d where d.republic.id = :republic and d.villa.id = :villa order by d.name");
            query.setParameters(params);
            ctx.setQuery(query);
            companiesExt = ServiceLocator.getDataService().loadList(ctx);
        } else if (params.containsKey("republic") && params.containsKey("aread")) {
            LoadContext.Query query = new LoadContext.Query("select d from ext$CompanyExt d where d.republic.id = :republic and d.aread.id = :aread " +
                    "and d.villa.id is null order by d.name");
            query.setParameters(params);
            ctx.setQuery(query);
            companiesExt = ServiceLocator.getDataService().loadList(ctx);
        } else {
            LoadContext.Query query = new LoadContext.Query("select d from ext$CompanyExt d where d.republic.id = :republic and d.villa.id is null " +
                    "and d.aread.id is null order by d.name");
            query.setParameters(params);
            ctx.setQuery(query);
            companiesExt = ServiceLocator.getDataService().loadList(ctx);
        }

        Map<CompanyExt, Node<CompanyExt>> companyExtNodesMap = new HashMap<CompanyExt, Node<CompanyExt>>();
        List<Node<CompanyExt>> rootNodes = new ArrayList<Node<CompanyExt>>();

        for (CompanyExt companyExt : companiesExt) {
            companyExtNodesMap.put(companyExt, new Node<CompanyExt>(companyExt));
        }

        for (CompanyExt companyExt : companiesExt) {
            CompanyExt parentCompanyExt = companyExt.getParentCompanyExt();
            if (parentCompanyExt == null) {
                rootNodes.add(companyExtNodesMap.get(companyExt));
            } else {
                Node<CompanyExt> parentDepNode = companyExtNodesMap.get(parentCompanyExt);
                if (parentDepNode != null) parentDepNode.addChild(companyExtNodesMap.get(companyExt));
                else rootNodes.add(companyExtNodesMap.get(companyExt));
            }
        }

        return new Tree(rootNodes);
    }

    public boolean isCaption(Object itemId) {
        return false;
    }

    public String getCaption(Object itemId) {
        return null;
    }
}
