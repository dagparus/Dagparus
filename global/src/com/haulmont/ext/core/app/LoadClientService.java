/*
 * Copyright (c) 2014 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.ext.core.app;

import java.io.File;
import java.io.IOException;

/**
 * Created by mahdi on 1/14/14.
 */
public interface LoadClientService {
    String NAME = "myexp_LoadClientService";
    void loadFromFile(File file) throws IOException;
}
