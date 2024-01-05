package com.han.rental.service;

import com.han.rental.payload.RentalRequest;
import com.han.rental.payload.RentalResponse;
import com.han.rental.pojo.Rental;
import com.han.rental.repository.RentalRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RentalServiceImpl implements RentalService {

    private final RentalRepository rentalRepository;


    @Autowired
    public RentalServiceImpl(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }


    private RentalResponse buildRentalResponse(Rental rental) {
        return RentalResponse.builder()
                .rentalUid(rental.getRentalUid())
                .username(rental.getUsername())
                .paymentUid(rental.getPaymentUid())
                .carUid(rental.getCarUid())
                .dateFrom(rental.getDateFrom())
                .dateTo(rental.getDateTo())
                .status(rental.getStatus())
                .build();
    }

    private Rental buildRental(String username, RentalRequest request) {
        Rental rental = new Rental();
        rental.setRentalUid(request.getRentalUid());
        rental.setUsername(username);
        rental.setPaymentUid(request.getPaymentUid());
        rental.setCarUid(request.getCarUid());
        rental.setDateFrom(request.getDateFrom());
        rental.setDateTo(request.getDateTo());
        rental.setStatus(request.getStatus());
        return rental;
    }


    @Override
    public RentalResponse getRental(String username) {
        return buildRentalResponse(rentalRepository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException("Not found Rental for user " + username)
        ));
    }


    @Override
    public RentalResponse getRental(UUID rentalUid) {
        return buildRentalResponse(rentalRepository.findByRentalUid(rentalUid).orElseThrow(
                () -> new EntityNotFoundException("Not found Rental by uid " + rentalUid)
        ));
    }


    @Override
    public List<RentalResponse> getRentals(String username) {
        return rentalRepository.findAllByUsername(username).stream()
                .map(this::buildRentalResponse).collect(Collectors.toList());
    }

    @Override
    public RentalResponse createRental(String username, RentalRequest rentalRequest) {
        return buildRentalResponse(rentalRepository.save(buildRental(username, rentalRequest)));
    }

    @Override
    public void deleteRental(UUID rentalUid) {

        Rental rental = rentalRepository.findByRentalUid(rentalUid).orElseThrow(
                () -> new EntityNotFoundException("Not found Rental by uid " + rentalUid)
        );
        rentalRepository.delete(rental);
    }

    @Override
    public RentalResponse updateRental(UUID rentalUid) {
        Rental rental = rentalRepository.findByRentalUid(rentalUid).orElseThrow(
                () -> new EntityNotFoundException("Not found rental by uid " + rentalUid));
        rental.setStatus("CANCELED");
        return buildRentalResponse(rentalRepository.save(rental));
    }
}


