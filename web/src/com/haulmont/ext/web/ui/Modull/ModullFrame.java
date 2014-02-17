/*
 * Copyright (c) 2014 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.ext.web.ui.Modull;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.ext.core.entity.ExtModull;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Магомедзапир
 * Date: 27.01.14
 * Time: 12:51
 * To change this template use File | Settings | File Templates.
 */
public class ModullFrame extends AbstractFrame {
    private boolean tableInited = false;
    private Table table;


    public ModullFrame(IFrame frame) {
        super(frame);
    }

    public void init() {
        if (tableInited) {
            return;
        }

        table = getComponent("modullTable");
        table.addAction(new AddAction());

        //final CollectionDatasource ds = table.getDatasource();

       /* table.addAction(new AbstractAction("add") {

            public void actionPerform(Component component) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("multiselect", true);
                params.put("isLookup", true);
                openLookup("ext$Modull.browse", new com.haulmont.cuba.gui.components.Window.Lookup.Handler() {
                    public void handleLookup(Collection items) {
                        if (items != null && items.size() > 0) {
                            for (ExtModull item : (Collection<ExtModull>) items) {
                                if (!ds.containsItem(item)) {
                                    ds.addItem(item);
                                }
                            }
                        }
                    }
                }, WindowManager.OpenType.THIS_TAB, params);
            }
        });    */


        table.addAction(new RemoveAction());

        tableInited = true;
    }

    private class AddAction extends AbstractAction {

        public AddAction() {
            super("add");
            icon = "icons/add.png";
        }

        public void actionPerform(Component component) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("multiselect", true);
            params.put("isLookup", true);
            openLookup("ext$Modull.browse", new com.haulmont.cuba.gui.components.Window.Lookup.Handler() {
                public void handleLookup(Collection items) {
                    if (items != null && items.size() > 0) {
                        for (ExtModull item : (Collection<ExtModull>) items) {
                            if (!table.getDatasource().containsItem(item)) {
                                table.getDatasource().addItem(item);
                            }
                        }
                    }
                }
            }, WindowManager.OpenType.THIS_TAB, params);
        }
    }

    public void setEditable(boolean editable) {
        table.setEditable(editable);
        for (Action action : table.getActions())
            action.setVisible(editable);
    }

    private class RemoveAction extends AbstractAction {

        protected RemoveAction() {
            super("remove");
        }

        public String getCaption() {
            final String messagesPackage = AppConfig.getMessagesPack();
            return MessageProvider.getMessage(messagesPackage, "actions.Remove");
        }

        public void actionPerform(Component component) {
            final Set selected = ModullFrame.this.table.getSelected();
            if (!selected.isEmpty()) {
                final String messagesPackage = AppConfig.getMessagesPack();
                frame.showOptionDialog(
                        MessageProvider.getMessage(messagesPackage, "dialogs.Confirmation"),
                        MessageProvider.getMessage(messagesPackage, "dialogs.Confirmation.Remove"),
                        IFrame.MessageType.CONFIRMATION,
                        new DialogAction[]{
                                new DialogAction(DialogAction.Type.OK) {
                                    public void actionPerform(Component component) {
                                        @SuppressWarnings({"unchecked"})
                                        final CollectionDatasource ds = ModullFrame.this.table.getDatasource();
                                        for (Object item : selected) {
                                            ds.removeItem((Entity) item);
                                        }
                                    }
                                }, new DialogAction(DialogAction.Type.CANCEL) {
                        }});
            }
        }
    }
}