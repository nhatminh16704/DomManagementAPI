package com.domhub.api.repository;

import com.domhub.api.dto.response.MessageDTO;
import com.domhub.api.model.Account;
import com.domhub.api.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Query("SELECT new com.domhub.api.dto.response.MessageDTO(" +
            "m.id, m.title, m.preview, s.fullName, m.date, true) " +
            "FROM Message m " +
            "JOIN Staff s ON m.sentBy = s.accountId ")
    List<MessageDTO> findMessagesForAdmin(@Param("accountId") Integer accountId);

    List<Message> findBySentBy(Integer sentBy);
}