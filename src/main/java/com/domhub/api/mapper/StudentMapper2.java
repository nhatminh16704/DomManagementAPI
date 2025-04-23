package com.domhub.api.mapper;

import com.domhub.api.dto.response.StudentDTO;
import com.domhub.api.model.Student;
import com.domhub.api.repository.RoomRentalRepository;
import com.domhub.api.service.ViolationService;
import org.springframework.stereotype.Component;


@Component
public class StudentMapper2 {
    private final RoomRentalRepository roomRentalRepository;
    private final RoomRentalMapper2 roomRentalMapper2;
    private final ViolationService violationService;

    public StudentMapper2(RoomRentalRepository roomRentalRepository, RoomRentalMapper2 roomRentalMapper2, ViolationService violationService) {
        this.roomRentalRepository = roomRentalRepository;
        this.roomRentalMapper2 = roomRentalMapper2;
        this.violationService = violationService;
    }

    public StudentDTO toDTO(Student student) {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setStudentCode(student.getStudentCode());
        studentDTO.setFullName(student.getFullName());
        studentDTO.setBirthday(student.getBirthday());
        studentDTO.setGender(student.getGender().toString());
        studentDTO.setHometown(student.getHometown());
        studentDTO.setPhoneNumber(student.getPhoneNumber());
        studentDTO.setEmail(student.getEmail());
        studentDTO.setClassName(student.getClassName());
        studentDTO.setRoomRentals(roomRentalMapper2.toDTOList(roomRentalRepository.findByStudentId(student.getId())));
        studentDTO.setViolations(violationService.getAllViolationsByStudentId(student.getId()));
        return studentDTO;
    }

}
