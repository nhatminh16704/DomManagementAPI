package com.domhub.api.mapper;

import com.domhub.api.dto.response.StudentDTO;
import com.domhub.api.model.Student;
import com.domhub.api.repository.RoomRentalRepository;
import org.springframework.stereotype.Component;


@Component
public class StudentMapper {
    private final RoomRentalRepository roomRentalRepository;
    private final RoomRentalMapper roomRentalMapper;

    public StudentMapper(RoomRentalRepository roomRentalRepository, RoomRentalMapper roomRentalMapper) {
        this.roomRentalRepository = roomRentalRepository;
        this.roomRentalMapper = roomRentalMapper;
    }

    public StudentDTO toDTO(Student student) {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setStudentCode(student.getStudentCode());
        studentDTO.setFullName(student.getFullName());
        studentDTO.setGender(student.getGender().toString());
        studentDTO.setHometown(student.getHometown());
        studentDTO.setPhoneNumber(student.getPhoneNumber());
        studentDTO.setEmail(student.getEmail());
        studentDTO.setClassName(student.getClassName());
        studentDTO.setRoomRentals(roomRentalMapper.toDTOList(roomRentalRepository.findByStudentId(student.getId())));

        return studentDTO;
    }

}
