package com.domhub.api.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class MessageToId implements Serializable {
    private int messageId;
    private int receiver;
}
