package com.domhub.api.model;

import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Embeddable
public class MessageToId implements Serializable {
    private int messageId;
    private int receiver;
}
