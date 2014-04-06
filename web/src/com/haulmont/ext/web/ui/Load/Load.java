/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.ext.web.ui.Load;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import com.haulmont.cuba.web.gui.components.WebButton;
import com.haulmont.cuba.web.gui.components.WebVBoxLayout;
import com.haulmont.ext.core.app.LoadClientService;
import sun.awt.AppContext;

import javax.inject.Inject;
import java.io.File;
import java.util.Map;

/**
 * Created by mahdi on 12/19/13.
 */
public class Load  extends AbstractLookup {

    @Inject
    protected WebVBoxLayout importRecord;

    @Inject
    protected LoadClientService lCS;

    @Inject
    protected FileUploadField uploadField;

    private File uploadFile;

    private TextField pathToCSV;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        uploadField.addListener(new FileUploadField.Listener() {
            @Override
            public void uploadSucceeded(Event event) {
                FileUploadingAPI fileUploadingAPI = com.haulmont.cuba.core.sys.AppContext.getBean(FileUploadingAPI.NAME);
                uploadFile = fileUploadingAPI.getFile(uploadField.getFileId());
                showNotification("Файл загружен: " + uploadField.getFileName(), NotificationType.HUMANIZED);
            }

            @Override
            public void uploadStarted(Event event) {
            }
            @Override
            public void uploadFinished(Event event) {
            }
            @Override
            public void uploadFailed(Event event) {
            }
            @Override
            public void updateProgress(long readBytes, long contentLength) {
            }
        });

        pathToCSV = getComponent("pathToCSV");

        Button buttonImport = new WebButton();
        buttonImport.setWidth("200px");
        buttonImport.setAction(new ImportAction());
        importRecord.add(buttonImport);
    }

    protected class ImportAction extends AbstractAction {

        protected ImportAction() {
            super("importRecord");
        }

        @Override
        public void actionPerform(Component component) {
            try {
                lCS.loadFromFile(uploadFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

