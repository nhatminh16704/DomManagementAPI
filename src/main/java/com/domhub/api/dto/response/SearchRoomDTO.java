package com.domhub.api.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SearchRoomDTO {
    Integer id;
    List<Integer> userid;
    String name;
}
