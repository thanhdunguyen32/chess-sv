package cc.mrbird.febs.system.entity;

import lombok.Data;

import java.util.List;

@Data
public class MailParams {
    private List<Integer> selected_gs;
    private Byte addressee;
    private String receiveId;
    private String sender;
    private String mailTitle;
    private String mailCont;
    private String attch;
    private String validity;

    @Override
    public String toString() {
        return "MailParams{" +
                "selected_gs=" + selected_gs +
                ", addressee=" + addressee +
                ", receiveId='" + receiveId + '\'' +
                ", sender='" + sender + '\'' +
                ", mailTitle='" + mailTitle + '\'' +
                ", mailCont='" + mailCont + '\'' +
                ", attch='" + attch + '\'' +
                ", validity='" + validity + '\'' +
                '}';
    }
}
