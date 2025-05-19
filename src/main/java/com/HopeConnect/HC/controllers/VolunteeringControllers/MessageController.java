package com.HopeConnect.HC.controllers.VolunteeringControllers;

import com.HopeConnect.HC.DTO.Message;
import com.HopeConnect.HC.security.config.eventsChat.RabbitMQJsonProducer;
import com.HopeConnect.HC.services.MessageStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final RabbitMQJsonProducer producer;
    private final MessageStorageService messageStorageService;

    @Autowired
    public MessageController(RabbitMQJsonProducer producer, MessageStorageService messageStorageService) {
        this.producer = producer;
        this.messageStorageService = messageStorageService;
    }

    // Send a message to a specific event chat
    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody Message message) {
        if (message.getEventName() == null || message.getContent() == null || message.getSender() == null) {
            return ResponseEntity.badRequest().body("Missing required message fields.");
        }

        producer.sendJsonMessage(message);
        return ResponseEntity.ok("Message sent successfully.");
    }

    // Get all messages for an event
    @GetMapping("/{eventName}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable String eventName) {
        List<Message> messages = messageStorageService.getMessagesForEvent(eventName);
        return ResponseEntity.ok(messages);
    }

    // Clear all messages for an event (optional, admin/moderator use case)
    @DeleteMapping("/{eventName}")
    public ResponseEntity<String> clearMessages(@PathVariable String eventName) {
        messageStorageService.clearMessagesForEvent(eventName);
        return ResponseEntity.ok("Messages cleared for event: " + eventName);
    }
}
