package com.domhub.api.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@IdClass(MessageToId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageTo {
    @Id
    private int messageId;

    @Id
    private int receiver;

    private boolean isRead;
}