package org.nearbyshops.Utility;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import org.nearbyshops.Constants;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;





public class SendPush {


    public static void sendFCMPushNotification(String topic, String title, String message, String notificationType)
    {

        if (notificationType==null)
        {
            notificationType = Constants.NOTIFICATION_TYPE_GENERAL;
        }


//            Notification notification  = new Notification(title,message);

        // See documentation on defining a message payload.
        Message messageEndUser = Message.builder()
//                    .setNotification(new Notification(title, message))
                .putData("notification_type", notificationType)
                .putData("notification_title", title)
                .putData("notification_message", message)
                .setTopic(topic)
                .build();

//            System.out.println(topic);

        FirebaseMessaging.getInstance().sendAsync(messageEndUser);

    }

}
