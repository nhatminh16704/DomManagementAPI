package com.domhub.api.service;

import com.domhub.api.dto.response.RoomBillDTO;
import com.domhub.api.model.Room;
import com.domhub.api.model.RoomBill;
import com.domhub.api.repository.RoomBillRepository;
import com.domhub.api.repository.RoomRepository;
import com.domhub.api.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomBillService {

    private final RoomBillRepository roomBillRepository;
    private final RoomRepository roomRepository;
    private final JwtUtil jwtUtil;
    private final StudentService studentService;
    private final RoomRentalService roomRentalService;


    public List<RoomBillDTO> getAll() {
        return roomBillRepository.findAllRoomBills();
    }


    public List<RoomBillDTO> getAllByMonthAndStatus(LocalDate billMonth, RoomBill.BillStatus status) {
        return roomBillRepository.findAllByBillMonthAndStatus(billMonth, status);
    }


    public RoomBill create(RoomBill bill) {
        return roomBillRepository.save(bill);
    }

    public Optional<RoomBill> getByRoomIdAndMonth(Integer roomId, LocalDate billMonth) {
        return roomBillRepository.findByRoomIdAndBillMonth(roomId, billMonth);
    }

    @Scheduled(cron = "0 0 0 1 * ?") // Run at 00:00:00 on the first day of each month // mỗi phút chạy 1 lần
    public void generateMonthlyRoomBills() {
        LocalDate currentMonth = LocalDate.now().withDayOfMonth(1);         // 2025-04-01
        LocalDate lastMonth = currentMonth.minusMonths(1);                  // 2025-03-01

        List<Room> rooms = roomRepository.findAll();

        List<Integer> roomIds = roomRepository.findAllRoomIds();
        for (Integer roomId : roomIds) {
            boolean billExists = roomBillRepository.existsByRoomIdAndBillMonth(roomId, currentMonth);
            if (billExists) continue;

            Optional<RoomBill> lastMonthBillOpt = roomBillRepository.findByRoomIdAndBillMonth(roomId, lastMonth);
            int startElectric = lastMonthBillOpt.map(RoomBill::getElectricityEnd).orElse(0);


            RoomBill newBill = RoomBill.builder()
                    .roomId(roomId)
                    .billMonth(currentMonth)
                    .electricityStart(startElectric)
                    .status(RoomBill.BillStatus.PENDING)
                    .build();

            roomBillRepository.save(newBill);
        }

    }

    private static final int ELECTRIC_UNIT_PRICE = 3000;

    public RoomBill updateElectricityEnd(Integer roomId, LocalDate billMonth, int newEndReading) {

        RoomBill bill = roomBillRepository.findByRoomIdAndBillMonth(roomId, billMonth)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        bill.setElectricityEnd(newEndReading);

        int usage = newEndReading - bill.getElectricityStart();
        bill.setTotalAmount(BigDecimal.valueOf(usage).multiply(BigDecimal.valueOf(ELECTRIC_UNIT_PRICE)));
        bill.setStatus(RoomBill.BillStatus.UNPAID);

        return roomBillRepository.save(bill);
    }

    public boolean isBillUnpaid(Integer billId) {
        Optional<RoomBill> bill = roomBillRepository.findById(billId);
        return bill.map(b -> b.getStatus() == RoomBill.BillStatus.UNPAID).orElse(false);
    }

    public Optional<RoomBill> findById(Integer id) {
        return roomBillRepository.findById(id);
    }

    public RoomBill update(RoomBill roomBill) {
        return roomBillRepository.save(roomBill);
    }

    public List<RoomBillDTO> getStudentBills(String authHeader) {
        Integer accountId = jwtUtil.extractAccountIdFromHeader(authHeader);
        if (accountId == null) return Collections.emptyList();

        Integer studentId = studentService.getStudentIdByAccountId(accountId);
        if (studentId == null) return Collections.emptyList();

        Integer roomId = roomRentalService.getCurrentRoomByStudentId(studentId);
        if (roomId == null) return Collections.emptyList();

        return roomBillRepository.findAllByRoomId(roomId);
    }



}

