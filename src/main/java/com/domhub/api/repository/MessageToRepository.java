package com.domhub.api.repository;

import com.domhub.api.model.MessageTo;
import com.domhub.api.model.MessageToId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.domhub.api.dto.response.MessageDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface MessageToRepository extends JpaRepository<MessageTo, MessageToId> {

    @Query("SELECT new com.domhub.api.dto.response.MessageDTO(" +
            "m.id, m.title, m.preview, s.fullName, m.date, mt.isRead) " +
            "FROM MessageTo mt " +
            "JOIN Message m ON mt.id.messageId = m.id " +
            "JOIN Staff s ON m.sentBy = s.accountId " +
            "WHERE mt.id.receiver = :accountId")
    List<MessageDTO> findMessagesByReceiver(@Param("accountId") Integer accountId);


    //     Correct syntax for querying embedded ID fields
    Optional<MessageTo> findById_MessageIdAndId_Receiver(Integer messageId, Integer accountId);

    @Query("SELECT COUNT(mt) FROM MessageTo mt WHERE mt.id.receiver = :accountId AND mt.isRead = false")
    Integer countUnreadMessagesByAccountId(@Param("accountId") Integer accountId);


}