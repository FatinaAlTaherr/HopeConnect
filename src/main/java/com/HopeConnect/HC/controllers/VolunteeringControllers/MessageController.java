package com.HopeConnect.HC.controllers.VolunteeringControllers;

import com.HopeConnect.HC.DTO.Message;
import com.HopeConnect.HC.models.Volunteering.Event;
import com.HopeConnect.HC.repositories.VolunteerngRepositories.EventRepository;
import com.HopeConnect.HC.security.config.eventsChat.RabbitMQJsonProducer;
import com.HopeConnect.HC.services.MessageStorageService;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/chat")public class MessageController {


    private final RabbitMQJsonProducer producer;
    private final MessageStorageService messageStorageService;
    private final EventRepository eventRepository;
    private final RabbitTemplate rabbitTemplate;
    private final MessageConverter messageConverter;

    @Autowired
    public MessageController(
            RabbitMQJsonProducer producer,
            MessageStorageService messageStorageService,
            EventRepository eventRepository,
            RabbitTemplate rabbitTemplate
    ) {
        this.producer = producer;
        this.messageStorageService = messageStorageService;
        this.eventRepository = eventRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.messageConverter = new Jackson2JsonMessageConverter();
    }

    // Publish message to RabbitMQ
    @PostMapping("/publish")
    public ResponseEntity<String> publishMessage(@RequestBody Message message) {
        if (message.getEventName() == null || message.getContent() == null || message.getSender() == null) {
            return ResponseEntity.badRequest().body("Missing required message fields.");
        }

        Event event = eventRepository.findByName(message.getEventName());
        if (event == null) {
            return ResponseEntity.badRequest().body("Event not found.");
        }

        producer.sendJsonMessage(message);
        return ResponseEntity.ok("Message published to RabbitMQ.");
    }

    // Consume messages for the given event name
    @GetMapping("/consume")
    public ResponseEntity<List<Message>> consumeMessages(@RequestParam String eventName) {
        List<Message> consumedMessages = new ArrayList<>();
        boolean keepConsuming = true;

        while (keepConsuming) {
            org.springframework.amqp.core.Message rawMessage = rabbitTemplate.receive("eventQueue");
            if (rawMessage == null) {
                break;
            }

            try {
                Message message = (Message) messageConverter.fromMessage(rawMessage);
                if (eventName.equals(message.getEventName())) {
                    consumedMessages.add(message);
                } else {
                    rabbitTemplate.send("eventQueue", rawMessage); // requeue
                    keepConsuming = false;
                }
            } catch (Exception e) {
                rabbitTemplate.send("eventQueue", rawMessage); // requeue on error
                keepConsuming = false;
            }
        }

        // Save consumed messages
        for (Message message : consumedMessages) {
            messageStorageService.saveMessage(eventName, message);
        }

        List<Message> storedMessages = messageStorageService.getMessagesForEvent(eventName);
        return ResponseEntity.ok(storedMessages);
    }

    // Optional: Clear stored messages for an event
    @DeleteMapping("/{eventName}")
    public ResponseEntity<String> clearMessages(@PathVariable String eventName) {
        messageStorageService.clearMessagesForEvent(eventName);
        return ResponseEntity.ok("Messages cleared for event: " + eventName);
    }
}
