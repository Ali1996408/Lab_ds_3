package com.han.rental.controller;

import com.han.rental.payload.RentalRequest;
import com.han.rental.payload.RentalResponse;
import com.han.rental.service.RentalServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/rental")
public class RentalController {

    private final RentalServiceImpl rentalService;

    @Autowired
    public RentalController(RentalServiceImpl rentalService) {
        this.rentalService = rentalService;
    }


    @GetMapping(value = "/{rentalUid}", produces = "application/json")
    public RentalResponse getRental(@RequestHeader("X-User-Name") String xUserName,
                                         @PathVariable("rentalUid") UUID rentalUid) {
        return rentalService.getRental(rentalUid);
    }

    @GetMapping(produces = "application/json")
    public List<RentalResponse> getRentals(@RequestHeader("X-User-Name") String xUserName) {
        return rentalService.getRentals(xUserName);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public RentalResponse create(@RequestHeader("X-User-Name") String xUserName, @RequestBody @Valid RentalRequest rentalRequest) {
        return rentalService.createRental(xUserName, rentalRequest);
    }

    @DeleteMapping("/{rentalUid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("rentalUid") UUID rentalUid) {
        rentalService.deleteRental(rentalUid);
    }

    //+++
    @PutMapping(value = "/{rentalUid}", produces = "application/json")
    public RentalResponse update(@PathVariable("rentalUid") UUID rentalUid) {
        return rentalService.updateRental(rentalUid);
    }

    //+++
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/manage/health")
    public String checkServiceAvailability() {
        return "Rental service is available";
    }

}
