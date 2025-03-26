package com.domhub.api.repository;

import com.domhub.api.model.MessageTo;
import com.domhub.api.model.MessageToId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.domhub.api.dto.response.MessageDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface MessageToRepository extends JpaRepository<MessageTo, MessageToId> {
    @Query("SELECT new com.domhub.api.dto.response.MessageDTO(m.id, m.title, m.content, a.userName, m.date, mt.isRead) " +
            "FROM MessageTo mt " +
            "JOIN Message m ON mt.messageId = m.id " +
            "JOIN Account a ON m.sentBy = a.id " +
            "WHERE mt.receiver = :accountId")
    List<MessageDTO> findMessagesByReceiver(@Param("accountId") Integer accountId);
}
