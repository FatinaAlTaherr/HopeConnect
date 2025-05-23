package com.HopeConnect.HC.security.config.eventsChat;

import com.HopeConnect.HC.DTO.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@Service
public class RabbitMQJsonProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQJsonProducer.class);

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMQJsonProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendJsonMessage(Message message) {
        // Ensure the timestamp is set here if not already set
        if (message.getTimestamp() == null) {
            message.setTimestamp(LocalDateTime.now());
        }

        String routingKey = "event." + message.getEventName();
        LOGGER.info("Sending message to {}: {}", routingKey, message);
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
