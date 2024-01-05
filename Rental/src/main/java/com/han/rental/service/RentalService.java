package com.han.rental.service;

import com.han.rental.payload.RentalRequest;
import com.han.rental.payload.RentalResponse;

import java.util.List;
import java.util.UUID;

public interface RentalService {
    RentalResponse getRental(String username);

    RentalResponse getRental(UUID rentalUid);

    List<RentalResponse> getRentals(String username);

    RentalResponse createRental(String username, RentalRequest rentalRequest);

    void deleteRental(UUID rentalUid);

    RentalResponse updateRental(UUID rentalUid);

}
