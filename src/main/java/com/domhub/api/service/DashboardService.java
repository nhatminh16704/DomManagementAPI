package com.domhub.api.service;

import com.domhub.api.dto.response.DashboardDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final ReportService reportService;
    private final RoomService roomService;
    private final StudentService studentService;
    private final RoomRentalService roomRentalService;
    private final StaffService staffService;

    public DashboardDTO getDashboard() {
        Long reportCount = reportService.count();
        Long roomCount = roomService.count();
        Long studentCount = studentService.count();
        Long staffCount = staffService.count();
        Double revenue = roomRentalService.getTotalRevenue();

        Long totalRoomCapacity = roomService.countCapacity();
        Long available = roomService.countAvailable();


        return new DashboardDTO(reportCount, roomCount, studentCount, staffCount, revenue, totalRoomCapacity, available);



    }
}
