package com.marceloneuro.internalfintech.service;

import com.marceloneuro.internalfintech.model.EmailDetails;

public interface EmailService {

    String sendSimpleMail(EmailDetails emailDetails);

    String sendMailWithAttachments(EmailDetails emailDetails);
}
