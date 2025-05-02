package com.domhub.api.mapper;


import com.domhub.api.dto.request.RoomRentalRequest;
import com.domhub.api.dto.response.RoomDTO;
import com.domhub.api.dto.response.RoomRentalDTO;
import com.domhub.api.model.Room;
import com.domhub.api.model.RoomRental;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface RoomRentalMapper {


    @Mapping(target = "status", expression = "java(roomRental.getStatus().name())")
    @Mapping(target = "roomName", source = "roomRental.room.roomName")
    @Mapping(target = "roomType", source = "roomRental.room.typeRoom.name")
    RoomRentalDTO toDTO(RoomRental roomRental);

    List<RoomRentalDTO> toDTOs(List<RoomRental> roomRentals);

}
