package com.han.rental.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
public class RentalRequest {

    @NotNull
    private UUID rentalUid;
    @NotNull
    private UUID paymentUid;
    /*@NotNull
    private Car car;*/
    @NotNull
    private UUID carUid;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    @NotBlank
    private String status;
}
