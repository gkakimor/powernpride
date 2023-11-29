package com.vcc.agile.project.mgmt.PowerNPride.service;

import com.vcc.agile.project.mgmt.PowerNPride.exceptions.SpringPowerNPrideException;
import com.vcc.agile.project.mgmt.PowerNPride.model.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder;
    private static final String FROM_EMAIL = "powernprideproject@gmail.com";

    void sendMail (NotificationEmail notificationEmail)
    {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(FROM_EMAIL);
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(notificationEmail.getBody());
        };

        try
        {
            mailSender.send(messagePreparator);
            log.info("Activation email sent.");
        } catch (MailException e)
        {
            throw new SpringPowerNPrideException("Exception occurred when sending the mail to " + notificationEmail.getRecipient() + ". Error: ", e);
        }
    }

}
