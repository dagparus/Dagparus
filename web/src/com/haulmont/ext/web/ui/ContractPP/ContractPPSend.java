package com.haulmont.ext.web.ui.ContractPP;


import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.ext.core.entity.ContractPP;
import com.haulmont.workflow.core.entity.CardInfo;
import com.haulmont.taskman.web.ui.common.CardSend;
import com.haulmont.workflow.core.entity.Card;



import java.util.Locale;
import java.util.Map;

public class ContractPPSend extends CardSend {

    public ContractPPSend(IFrame frame) {
        super(frame);
    }

    public void init(Map<String, Object> params) {
        super.init(params);
    }

    @Override
    protected CardInfo createCardInfo(Card card, User user, String comment) {
        CardInfo ci = super.createCardInfo(card, user, comment);
        if (card instanceof ContractPP) {
            ContractPP contractPP = (ContractPP) card;
            ci.setDescription(MessageProvider.getMessage(ContractPPSend.class, "ContractPP.comment",
                    (user.getLanguage() != null) ? new Locale(user.getLanguage()) : UserSessionProvider.getUserSession().getLocale())
                    + " " + contractPP.getNumber() + " (" + comment + ")");
        }
        return ci;
    }
}
