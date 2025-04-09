package com.domhub.api.dto.response;

import com.domhub.api.model.RoomBill;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomBillDTO {
    private Integer id;
    private Integer roomId;
    private String roomName;
    private LocalDate billMonth;
    private Integer electricityStart;
    private Integer electricityEnd;
    private BigDecimal totalAmount;
    private RoomBill.BillStatus status;

}

