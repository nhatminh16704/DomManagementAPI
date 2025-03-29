package com.domhub.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    private String studentCode;
    private String fullName;
    private LocalDate birthday;
    private String gender;
    private String phoneNumber;
    private String email;
    private String hometown;
    private String className;
    private List<RoomRentalDTO> roomRentals; // Danh sách thuê phòng (gồm lịch sử)
}

