/*
 * Copyright (c) 2014 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.ext.core.entity;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "ext$ContractModull")
@Table(name = "EXT_CONTRACTMODULL")
@SystemLevel
public class ContractppModull extends StandardEntity {

    private static final long serialVersionUID = 8543853035155300982L;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CONTRACTPP_ID")
    @OnDeleteInverse(DeletePolicy.CASCADE)
    private ContractPP contractPP;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MODULL_ID")
    @OnDeleteInverse(DeletePolicy.CASCADE)
    private ExtModull modull;

    public ContractPP getContractPP() {
        return contractPP;
    }

    public void setContractPP(ContractPP contractPP) {
        this.contractPP = contractPP;
    }

    public ExtModull getModull() {
        return modull;
    }

    public void setModull(ExtModull modull) {
        this.modull = modull;
    }


}
