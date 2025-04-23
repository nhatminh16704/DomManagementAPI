package com.domhub.api.mapper;

import com.domhub.api.dto.request.StudentRequest;
import com.domhub.api.dto.response.StudentDTO;
import com.domhub.api.model.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(target = "roomRentals", ignore = true )
    @Mapping(target = "violations", ignore = true )
    @Mapping(target = "gender", expression = "java(student.getGender().name())")
    StudentDTO toDTO(Student student);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accountId", ignore = true)
    @Mapping(target = "gender", expression = "java(Student.Gender.valueOf(studentRequest.getGender()))")
    Student toEntity(StudentRequest studentRequest);

}
