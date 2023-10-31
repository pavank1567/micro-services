package com.stalwart.notificationservice.service;


import com.stalwart.notificationservice.event.OrderPlacedEvent;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Service
public class NotificationService {

    private final JavaMailSender javaMailSender;

    public NotificationService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(OrderPlacedEvent event){
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(event.getEmail());
            message.setSubject("Order Confirmed");
            message.setText("Your Order with the order number" + event.getOrderNumber() +
                    "has been placed successfully");
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
