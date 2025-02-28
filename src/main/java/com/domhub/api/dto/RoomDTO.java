package com.domhub.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
    public class RoomDTO {

        private String roomId;
        private long price;
        private int totalBeds;
        private String blockType;
        private String typeRoom;
        private int availableBeds;

    }

