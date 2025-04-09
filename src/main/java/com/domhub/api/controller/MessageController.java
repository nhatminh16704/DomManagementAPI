package com.domhub.api.controller;


import com.domhub.api.dto.request.MessageRequest;
import com.domhub.api.dto.response.MessageDTO;
import com.domhub.api.dto.response.MessageDetailDTO;
import com.domhub.api.dto.response.UserSearchDTO;
import com.domhub.api.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

import com.domhub.api.model.Message;

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

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<MessageDTO>> getMessagesByAccountId(@PathVariable Integer accountId) {
        try {
            List<MessageDTO> messages = messageService.getMessagesByAccountId(accountId);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<MessageDetailDTO> getMessageById(@PathVariable Integer id) {
        try {
            MessageDetailDTO message = messageService.getMessageById(id);
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PatchMapping("/{messageId}/read")
    public ResponseEntity<Void> markMessageAsRead(@PathVariable Integer messageId,
                                                  @RequestBody Map<String, Integer> request) {
        try {
            Integer accountId = request.get("accountId");
            if (accountId == null) {
                return ResponseEntity.badRequest().build();
            }
            messageService.markMessageAsRead(messageId, accountId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
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

    @GetMapping("/unread/{accountId}")
    public ResponseEntity<Integer> countUnreadMessagesByAccountId(@PathVariable Integer accountId) {
        try {
            int count = messageService.countUnreadMessagesByAccountId(accountId);
            return ResponseEntity.ok(count);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


}