package com.domhub.api.repository;

import com.domhub.api.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findBySentBy(Integer sentBy);
}
