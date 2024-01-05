package com.han.cars.controller;

import com.han.cars.payload.response.CarResponse;
import com.han.cars.payload.response.PaginationResponse;
import com.han.cars.service.CarServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cars")
public class CarController {

    private final CarServiceImpl carService;

    @Autowired
    public CarController(CarServiceImpl carService) {
        this.carService = carService;
    }


    @GetMapping(produces = "application/json")
    public PaginationResponse getCars(@RequestParam(required = false, defaultValue = "0") int page,
                                        @RequestParam(required = false, defaultValue = "5") int size) {
        return carService.getCars(page,size);
    }

    @GetMapping(value = "/{carUid}", produces = "application/json")
    public CarResponse getCar(@PathVariable("carUid") UUID carUid) {
        return carService.getCar(carUid);
    }

    //+++
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/manage/health")
    public String checkServiceAvailability(){
        return "Car service is available";
    }
}
