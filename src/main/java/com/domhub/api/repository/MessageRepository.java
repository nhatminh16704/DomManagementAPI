package com.domhub.api.repository;

import com.domhub.api.dto.response.MessageDTO;
import com.domhub.api.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findBySentBy(Integer sentBy);
    @Query("SELECT new com.domhub.api.dto.response.MessageDTO(m.id, m.title, m.content, CONCAT(s.firstName, ' ', s.lastName), m.date, mr.isRead) " +
        "FROM Message m " +
        "JOIN Staff s ON m.sentBy = s.accountId " +
        "JOIN MessageTo mr ON m.id = mr.messageId " +
        "WHERE m.id = :messageId AND mr.receiver = :receiver")
    MessageDTO findMessageByIdAndReceiver(@Param("messageId") Integer messageId, @Param("receiver") Integer receiver);
}
