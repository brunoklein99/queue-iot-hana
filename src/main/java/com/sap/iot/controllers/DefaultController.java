package com.sap.iot.controllers;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@RequestMapping("/")
@RestController
public class DefaultController {

    private final ConnectionFactory factory;

    @Autowired
    public DefaultController(ConnectionFactory factory) {
        this.factory = factory;
    }

    @GetMapping
    public String get() throws IOException, TimeoutException {
        Connection connection = factory.createConnection();
        try {
            try (Channel channel = connection.createChannel(false)) {
                channel.queueDeclare("default", false, false, false, null);
                String message = "Hello Word";
                channel.basicPublish("", "default", null, message.getBytes());
            }
        } finally {
            connection.close();
        }
        return "ok";
    }

    @PostMapping
    public Long post() throws IOException, TimeoutException {
        Connection connection = factory.createConnection();
        try {
            try(Channel channel = connection.createChannel(false)) {
                return channel.messageCountgit("default");
            }
        } finally {
            connection.close();
        }
    }
}
