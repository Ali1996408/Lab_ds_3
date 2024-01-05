package com.han.gateway.Controller;


import com.han.gateway.payload.CarResponse;
import com.han.gateway.payload.PaginationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cars")
public class CarServiceController {

    private final RestTemplate restTemplate;

    private final String carServiceBaseUrl = "http://localhost:8070/api/v1/cars";


    @Autowired
    public CarServiceController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @GetMapping("/manage/health")
    public ResponseEntity<String> manageHealth() {
        try {
            String healthUri = carServiceBaseUrl.concat("/manage/health");
            return restTemplate.getForEntity(healthUri, String.class);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Car service unavailable");
        }
    }

    /*成功*/
    @GetMapping(produces = "application/json")
    public ResponseEntity<PaginationResponse> getCars(@RequestParam(required = false, defaultValue = "0") int page,
                                                      @RequestParam(required = false, defaultValue = "5") int size) {
        String uri = "http://localhost:8070/api/v1/cars?page={page}&size={size}";
        ResponseEntity<PaginationResponse> paginationResponse = restTemplate.getForEntity(uri, PaginationResponse.class, page, size);
        return ResponseEntity.ok(paginationResponse.getBody());
    }

    /*成功*/
    @GetMapping(value = "/{carUid}", produces = "application/json")
    public CarResponse getCar(@PathVariable("carUid") UUID carUid) {
        String uri = "http://localhost:8070/api/v1/cars/{carUid}";
        return restTemplate.getForEntity(uri, CarResponse.class, carUid).getBody();
    }

}
