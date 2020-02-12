package com.syngenta.portal.data.service;

import com.syngenta.portal.data.model.FileParseResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DefaultMailService implements MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    final static String DEFAULT_MAIL = "riham.fayez@itworx.com";
    final static String SUCCEED_RESULT = "File parsing Succeeded and attached Script file and Flat Object Script";
    final static String FAIL_RESULT = "File parsing Failed and attached errors";

    public boolean sendEmail(String mails, FileParseResults results) {
        try {

            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);

            if (mails != null && mails.length() > 0) {
                mails = mails + "," + DEFAULT_MAIL;
                List<String> mailsList = new ArrayList<>(Arrays.asList(mails.split(",")));
                helper.setTo(mailsList.toArray(new String[mailsList.size()]));
            } else {
                helper.setTo(DEFAULT_MAIL);
            }

            helper.setSubject("Seeds Gateway Data Parsing Results");
            helper.setText("Hello Team \n " + (results.isParsingSucceed() ? SUCCEED_RESULT : FAIL_RESULT));

            if (results.getScript() != null && results.isParsingSucceed())
                helper.addAttachment("sqlScript.sql", results.getScript());

            if (results.getFlatObjectScript() != null && results.isParsingSucceed())
                helper.addAttachment("flatScript.txt", results.getFlatObjectScript());

            if (results.getErrorFile() != null && !results.isParsingSucceed())
                helper.addAttachment("errors.txt", results.getErrorFile());

            javaMailSender.send(msg);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
}

