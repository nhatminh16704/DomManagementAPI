package com.domhub.api.mapper;


import com.domhub.api.dto.response.RoomDTO;
import com.domhub.api.model.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    @Mapping(source = "block.type", target = "blockType")
    @Mapping(source = "typeRoom.name", target = "typeRoom")
    RoomDTO toDTO(Room room);

    List<RoomDTO> toDTOs(List<Room> rooms);

}
