package br.com.vrteste.notifications;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class NotificationSystemApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(NotificationSystemApplication.class, args);
    }
}
