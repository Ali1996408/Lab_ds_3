package com.han.rental.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class RentalResponse {
    private UUID rentalUid;
    private String username;
    private UUID paymentUid;
    /*private Car car;*/
    private UUID carUid;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private String status;
}
