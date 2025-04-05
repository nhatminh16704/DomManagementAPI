package com.domhub.api.controller;


import com.domhub.api.dto.request.MessageRequest;
import com.domhub.api.dto.response.MessageDTO;
import com.domhub.api.dto.response.UserSearchDTO;
import com.domhub.api.service.MessageService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import com.domhub.api.model.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    // API tạo tin nhắn
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createMessage(@RequestBody MessageRequest messageRequest) {
        try {
            String response = messageService.createMessage(messageRequest);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi hệ thống: " + e.getMessage());
        }
    }

    @GetMapping("/users/search")
    public ResponseEntity<List<UserSearchDTO>> searchUsers(@RequestParam String keyword) {
        try {
            List<UserSearchDTO> users = messageService.searchUsers(keyword);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{accountId}") 
    public ResponseEntity<List<MessageDTO>> getMessagesByAccountId(@PathVariable Integer accountId) {
        try {
            List<MessageDTO> messages = messageService.getMessagesByAccountId(accountId);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/sent/{accountId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MessageDTO>> getMessagesSentByAccountId(@PathVariable Integer accountId) {
        try {
            List<MessageDTO> messages = messageService.getMessagesSentByAccountId(accountId);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{messageId}/{receiver}")
    public ResponseEntity<MessageDTO> getMessageById( @PathVariable Integer messageId, @PathVariable Integer receiver) {
        MessageDTO messageDTO = messageService.getMessageById(messageId, receiver);
        if (messageDTO != null) {
            return ResponseEntity.ok(messageDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("findId/{id}")
    public MessageDTO getMessageByIdForAdmin(@PathVariable Integer id) {
        return messageService.getMessagebyIDForAdmin(id);
    }
    
}

