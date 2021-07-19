package com.system.issuetracking.entity;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * @author Monica Rajbhandari on 7/15/21
 */
public class EmailTemplate {
    private final String receiver;
    private String body;
    private String url;

    public EmailTemplate(String receiver, String body, String url) {
        this.receiver = receiver;
        this.body = body;
        this.url = url;
    }


    /* Email body without any url in it */
    public String returnContent() {

        String color= "#0097A7";

        return " <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"700\" style=\"border-collapse: collapse;\">" +
                "        <tr>" +
                "            <td align=\"center\" bgcolor=\"" + color + "\">" +
                "                <p style=\"display: block;color:white;font-weight:bold;\" style=\"padding: 5px 0 5px 0;\">" +
                "                   <b style=\"margin-right:15%;font-size:large;\"> Issue Tracking System" +
                "</b>" +
                "                </p>" +
                "            </td>" +
                "        </tr>" +
                "        <tr>" +
                "            <td bgcolor=\"#eaeaea\" style=\"padding: 30px 25px 30px 25px;\">" +
                (receiver == null || receiver.equals("") ? "<p>Hi,</p>" : "<p>Dear " + receiver + ",</p>")
                + body +
                "                <p>Thank you,</p>" +
             " Issue Tracking System<br/>" +
               "Satdobato, Lalitpur - Nepal<br/>"+
                "Phone: 01-4562431 </p>" +
                "            </td>" +
                "        </tr>" +
                "        <tr>" +
                "            <td bgcolor=\"#c5c5c5\" style=\"padding: 25px 25px 25px 25px;font-size:smaller\">" +
                "                <p>NOTE: This is a post-only email. Please do not reply to this email. This mailbox is not monitored and you will not receive a response. " +
                "The information contained in this email message is intended only for the use of the intended recipient. If you received this communication in error, " +
                "please reply to the sender indicating that fact and delete the copy you received. If you have any questions or concerns, please contact Issue Tracking System at 01-4562431 " +
                "</p>" +
                "            </td>" +
                "        </tr>" +
                "    </table>";
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
