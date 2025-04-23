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

    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "endDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "studentId", ignore = true)
    @Mapping(target = "status", expression = "java(RoomRental.Status.UNPAID)")
    RoomRental toEntity(RoomRentalRequest roomRentalRequest);

    @Mapping(target = "status", expression = "java(roomRental.getStatus().name())")
    @Mapping(target = "roomName", source = "roomRental.room.roomName")
    @Mapping(target = "roomType", source = "roomRental.room.typeRoom.name")
    RoomRentalDTO toDTO(RoomRental roomRental);

    List<RoomRentalDTO> toDTOs(List<RoomRental> roomRentals);

}
