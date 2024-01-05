package com.han.gateway.service;

import com.han.gateway.model.Car;
import com.han.gateway.payload.CarResponse;
import com.han.gateway.payload.PaymentRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;


@Service
public class GatewayService {
    public HttpHeaders createHeader(String xUserName) {
        HttpHeaders headers = createHeader();
        headers.set("X-User-Name", xUserName);
        return headers;
    }

    public HttpHeaders createHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    public PaymentRequest createPayment(Integer price) {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setPaymentUid(UUID.randomUUID());
        paymentRequest.setStatus("PAID");
        paymentRequest.setPrice(price);
        return paymentRequest;
    }

    private Car buildCar(CarResponse carResponse) {

        return Car.builder()
                .id(carResponse.getId())
                .carUid(carResponse.getCarUid())
                .brand(carResponse.getBrand())
                .model(carResponse.getModel())
                .registrationNumber(carResponse.getRegistrationNumber())
                .power(carResponse.getPower())
                .price(carResponse.getPrice())
                .type(carResponse.getType())
                .availability(carResponse.getAvailability())
                .build();
    }
}
