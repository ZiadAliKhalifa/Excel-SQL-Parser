package com.syngenta.portal.data.service;

import com.syngenta.portal.data.model.FileParseResults;

public interface MailService {

    boolean sendEmail(String mails, FileParseResults results);
}
