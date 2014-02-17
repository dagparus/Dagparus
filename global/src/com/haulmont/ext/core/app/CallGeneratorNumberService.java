/*
 * Copyright (c) 2014 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.ext.core.app;

import com.haulmont.ext.core.entity.Call;

/**
 * Created by mahdi on 1/31/14.
 */
public interface CallGeneratorNumberService {

    String NAME = "myexp_CallGeneratorNumberService";

    String getNextNumber(Call call);

}
