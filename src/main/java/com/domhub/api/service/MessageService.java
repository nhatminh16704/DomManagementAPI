package com.domhub.api.service;

import com.domhub.api.dto.request.MessageRequest;
import com.domhub.api.dto.response.*;
import com.domhub.api.exception.AppException;
import com.domhub.api.exception.ErrorCode;
import com.domhub.api.model.Account;
import com.domhub.api.model.Message;
import com.domhub.api.model.MessageTo;
import com.domhub.api.model.MessageToId;
import com.domhub.api.model.Room;
import com.domhub.api.repository.AccountRepository;
import com.domhub.api.repository.MessageRepository;
import com.domhub.api.repository.MessageToRepository;
import com.domhub.api.repository.RoomRentalRepository;
import com.domhub.api.repository.RoomRepository;

import com.domhub.api.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MessageService {

    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final MessageRepository messageRepository;
    private final MessageToRepository messageToRepository;
    private final RoomRepository roomRepository;
    private final RoomRentalRepository roomrentalRepository;
    private final HttpServletRequest httpServletRequest;
    private final JwtUtil jwtUtil;

    public ApiResponse<List<UserSearchDTO>> searchUsers(String keyword) {
        return ApiResponse.success(accountRepository.findByUserNameStartingWith(keyword)
                .stream()
                .map(account -> new UserSearchDTO(account.getId(), account.getUserName()))
                .toList());
    }

    public ApiResponse<List<SearchRoomDTO>> searchRoom(String keyword) {
        List<Room> rooms = roomRepository.findByRoomNameContainingIgnoreCase(keyword);
        List<SearchRoomDTO> result = new ArrayList<>();
        for (Room room : rooms) {
            SearchRoomDTO searchRoomDTO = new SearchRoomDTO(room.getId(),
                    roomrentalRepository.findAccountIdsByRoomId(room.getId()),
                    room.getRoomName());
            result.add(searchRoomDTO);
        }
        return ApiResponse.success(result);
    }


    public ApiResponse<Void> createMessage(MessageRequest messageRequest) {

        accountService.validateAccountExists(messageRequest.getSentBy(), "Sender not found");

        for (Integer receiver : messageRequest.getReceivers()) {
            accountService.validateAccountExists(receiver, "Receiver not found");
        }

        Message message = new Message();
        message.setTitle(messageRequest.getTitle());
        message.setContent(messageRequest.getContent());
        message.setSentBy(messageRequest.getSentBy());
        message = messageRepository.save(message);


        for (Integer receiver : messageRequest.getReceivers()) {
            MessageToId messageToId = new MessageToId();
            messageToId.setMessageId(message.getId());
            messageToId.setReceiver(receiver);
            MessageTo messageTo = new MessageTo();
            messageTo.setId(messageToId);
            messageTo.setRead(false);
            messageToRepository.save(messageTo);
        }

        return ApiResponse.success("Message created successfully");
    }

    public ApiResponse<List<MessageDTO>> getMessagesByAccountId(Integer accountId) {
        accountService.validateAccountExists(accountId, "Account not found");
        return ApiResponse.success(messageToRepository.findMessagesByReceiver(accountId));
    }

    public ApiResponse<MessageDetailDTO> getMessageById(Integer messageId) {

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new AppException(ErrorCode.MESSAGE_NOT_FOUND));

        Account sender = accountRepository.findById(message.getSentBy())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "Sender not found"));

        return ApiResponse.success(new MessageDetailDTO(
                message.getId(),
                message.getTitle(),
                message.getContent(),
                sender.getUserName(),
                message.getDate())
        );
    }

    public ApiResponse<Void> markMessageAsRead(Integer messageId, Integer accountId) {

        MessageTo messageTo = messageToRepository.findById_MessageIdAndId_Receiver(messageId, accountId)
                .orElseThrow(() -> new AppException(ErrorCode.MESSAGE_TO_NOT_FOUND));
        messageTo.setRead(true);
        messageToRepository.save(messageTo);
        return ApiResponse.success("Message marked as read successfully");
    }

    public ApiResponse<Integer> countUnreadMessagesByAccountId() {
        String token = httpServletRequest.getHeader("Authorization");
        Integer accountId = jwtUtil.extractAccountIdFromHeader(token);
        accountService.validateAccountExists(accountId, "Account not found");
        return ApiResponse.success(messageToRepository.countUnreadMessagesByAccountId(accountId));
    }

    public ApiResponse<List<MessageDTO>> getMessagesSentByAccountId() {

        String token = httpServletRequest.getHeader("Authorization");
        Integer accountId = jwtUtil.extractAccountIdFromHeader(token);
        accountService.validateAccountExists(accountId, "Account not found");
        return ApiResponse.success(messageRepository.findMessagesForAdmin(accountId));

    }
}