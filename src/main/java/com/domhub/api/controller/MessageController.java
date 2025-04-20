package com.domhub.api.controller;


import com.domhub.api.dto.request.MarkReadRequest;
import com.domhub.api.dto.request.MessageRequest;
import com.domhub.api.dto.response.*;
import com.domhub.api.dto.response.MessageDetailDTO;
import com.domhub.api.service.MessageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
@Validated  // Validate request params/path variables
public class MessageController {

    private final MessageService messageService;


    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> createMessage(@RequestBody @Valid MessageRequest messageRequest) {
        return messageService.createMessage(messageRequest);
    }

    @GetMapping("/users/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<UserSearchDTO>> searchUsers(@RequestParam @NotBlank String keyword) {
        return messageService.searchUsers(keyword);
    }

    @GetMapping("/search/room")
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

    @PatchMapping("/{messageId}/read")
    public ApiResponse<Void> markMessageAsRead(@PathVariable @Min(1) Integer messageId,
                                               @RequestBody @Valid MarkReadRequest request) {
        return messageService.markMessageAsRead(messageId, request.getAccountId());
    }


    @GetMapping("/sent/{accountId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<MessageDTO>> getMessagesSentByAccountId(@PathVariable @Min(1) Integer accountId) {
            return messageService.getMessagesSentByAccountId(accountId);
    }

    @GetMapping("/unread/{accountId}")
    public ApiResponse<Integer> countUnreadMessagesByAccountId(@PathVariable @Min(1) Integer accountId) {
        return messageService.countUnreadMessagesByAccountId(accountId);

    }
}

