package com.domhub.api.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
    public class RoomDTO {
        private Integer id;
        private String roomName;
        private long price;
        private int maxStudents;
        private String blockType;
        private String typeRoom;
        private int available;

    }

