package com.stalwart.notificationservice;

import com.stalwart.notificationservice.event.OrderPlacedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootApplication
@Slf4j
public class NotificationServiceApplication {

    private final JavaMailSender javaMailSender;

    public NotificationServiceApplication(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @KafkaListener(topics = "notification-topic")
    public void handleNotification(OrderPlacedEvent event){

        //send an email notification

        log.info("Recieved notification for order - " + event.getOrderNumber());
        sendEmail(event);


    }

    public void sendEmail(OrderPlacedEvent event){
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(event.getEmail());
            message.setSubject("Order Confirmed");
            message.setText("Your Order with the order number " + event.getOrderNumber() +
                    " has been placed successfully");
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
