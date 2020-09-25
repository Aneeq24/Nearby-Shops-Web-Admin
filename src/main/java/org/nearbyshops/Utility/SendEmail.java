package org.nearbyshops.Utility;

import org.nearbyshops.AppProperties;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class SendEmail {


    @Autowired
    AppProperties appProperties;

    public Mailer inHouseMailer;



    public Mailer getMailerInstance()
    {
        if(inHouseMailer==null)
        {
            inHouseMailer = MailerBuilder
                    .withSMTPServer(appProperties.getSmtp_server_url(),appProperties.getSmtp_port(),
                            appProperties.getSmtp_username(),appProperties.getSmtp_password())
                    .buildMailer();

        }


        return inHouseMailer;
    }

}
