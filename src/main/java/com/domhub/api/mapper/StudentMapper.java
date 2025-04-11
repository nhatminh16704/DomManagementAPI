package com.domhub.api.mapper;

import com.domhub.api.dto.response.StudentDTO;
import com.domhub.api.model.Student;
import com.domhub.api.repository.RoomRentalRepository;
import com.domhub.api.service.ViolationService;
import org.springframework.stereotype.Component;


@Component
public class StudentMapper {
    private final RoomRentalRepository roomRentalRepository;
    private final RoomRentalMapper roomRentalMapper;
    private final ViolationService violationService;

    public StudentMapper(RoomRentalRepository roomRentalRepository, RoomRentalMapper roomRentalMapper, ViolationService violationService) {
        this.roomRentalRepository = roomRentalRepository;
        this.roomRentalMapper = roomRentalMapper;
        this.violationService = violationService;
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
        studentDTO.setViolations(violationService.getAllViolationsByStudentId(student.getId()));
        return studentDTO;
    }

}
