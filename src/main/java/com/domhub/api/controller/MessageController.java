package com.domhub.api.controller;


import com.domhub.api.dto.request.MarkReadRequest;
import com.domhub.api.dto.request.MessageRequest;
import com.domhub.api.dto.response.*;
import com.domhub.api.dto.response.MessageDetailDTO;
import com.domhub.api.service.MessageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
@Validated  // Validate request params/path variables
public class MessageController {

    private final MessageService messageService;


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> createMessage(@RequestBody @Valid MessageRequest messageRequest) {
        return messageService.createMessage(messageRequest);
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<UserSearchDTO>> searchUsers(@RequestParam @NotBlank String keyword) {
        return messageService.searchUsers(keyword);
    }

    @GetMapping("/room")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<SearchRoomDTO>> searchRoom(@RequestParam @NotBlank String keyword) {
        return messageService.searchRoom(keyword);
    }


    @GetMapping("/account/{accountId}")
    public ApiResponse<List<MessageDTO>> getMessagesByAccountId(@PathVariable @Min(1) Integer accountId) {
        return messageService.getMessagesByAccountId(accountId);
    }


    @GetMapping("/{id}")
    public ApiResponse<MessageDetailDTO> getMessageById(@PathVariable @Min(1) Integer id) {
        return messageService.getMessageById(id);
    }

    @PatchMapping("/{messageId}/mark-read")
    public ApiResponse<Void> markMessageAsRead(@PathVariable @Min(1) Integer messageId,
                                               @RequestBody @Valid MarkReadRequest request) {
        return messageService.markMessageAsRead(messageId, request.getAccountId());
    }


    @GetMapping("/sent")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<MessageDTO>> getMessagesSentByAccountId() {
            return messageService.getMessagesSentByAccountId();
    }

    @GetMapping("/unread")
    public ApiResponse<Integer> countUnreadMessagesByAccountId() {
        return messageService.countUnreadMessagesByAccountId();

    }
}

