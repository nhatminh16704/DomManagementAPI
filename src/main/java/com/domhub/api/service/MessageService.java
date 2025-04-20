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

    public List<UserSearchDTO> searchUsers(String keyword) {
        return accountRepository.findByUserNameStartingWith(keyword)
                .stream()
                .map(account -> new UserSearchDTO(account.getId(), account.getUserName()))
                .toList();
    }

    public List<SearchRoomDTO> searchRoom(String keyword) {
        List<Room> rooms = roomRepository.findByRoomNameContainingIgnoreCase(keyword);
        List<SearchRoomDTO> result = new ArrayList<>();
        for (Room room : rooms) {
            SearchRoomDTO searchRoomDTO = new SearchRoomDTO(room.getId(),
                    roomrentalRepository.findAccountIdsByRoomId(room.getId()),
                    room.getRoomName());
            result.add(searchRoomDTO);
        }
        return result;
    }


    public ApiResponse<Void> createMessage(MessageRequest messageRequest) {

        if (!accountService.existsById(messageRequest.getSentBy())) {
            throw new AppException(ErrorCode.USER_NOT_FOUND, "Sender not found");
        }

        for (Integer receiver : messageRequest.getReceivers()) {
            if (!accountService.existsById(receiver)) {
                throw new AppException(ErrorCode.USER_NOT_FOUND, "Receiver not found: " + receiver);
            }
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

    public List<MessageDTO> getMessagesByAccountId(Integer accountId) {
        if (accountId == null) {
            throw new IllegalArgumentException("Account ID cannot be null");
        }
        return messageToRepository.findMessagesByReceiver(accountId);
    }

    public MessageDetailDTO getMessageById(Integer messageId) {
        if (messageId == null) {
            throw new IllegalArgumentException("Message ID cannot be null");
        }
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found: " + messageId));
        Account sender = accountRepository.findById(message.getSentBy())
                .orElseThrow(() -> new IllegalArgumentException("Sender not found: " + message.getSentBy()));

        return new MessageDetailDTO(
                message.getId(),
                message.getTitle(),
                message.getContent(),
                sender.getUserName(),
                message.getDate()
        );
    }

    public void markMessageAsRead(Integer messageId, Integer accountId) {
        if (messageId == null) {
            throw new IllegalArgumentException("Message ID cannot be null");
        }
        MessageTo messageTo = messageToRepository.findById_MessageIdAndId_Receiver(messageId, accountId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found: " + messageId));
        messageTo.setRead(true);
        messageToRepository.save(messageTo);
    }

    public int countUnreadMessagesByAccountId(Integer accountId) {
        if (accountId == null) {
            throw new IllegalArgumentException("Account ID cannot be null");
        }
        return messageToRepository.countUnreadMessagesByAccountId(accountId);
    }

    public List<MessageDTO> getMessagesSentByAccountId(Integer accountId) {
        if (accountId == null) {
            throw new IllegalArgumentException("Account ID cannot be null");
        }
        Optional<Account> accountOpt = accountRepository.findById(accountId);
        if (accountOpt.isEmpty()) {
            throw new IllegalArgumentException("Account not found: " + accountId);
        }

        return messageRepository.findMessagesForAdmin(accountId);
    }
}