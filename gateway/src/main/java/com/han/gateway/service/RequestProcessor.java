package com.han.gateway.service;

import com.han.gateway.exception.ServiceUnavailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.UUID;

@Slf4j
public class RequestProcessor<ResponseType, RequestType> implements Runnable {
    private final String baseUrl;
    private UUID uid;

    private String username;

    private final HttpEntity<RequestType> request;

    private final ResponseType responseTypeClass;
    private final HttpMethodType methodType;


    private final RestTemplate restTemplate;


    public RequestProcessor(String baseUrl,
                            HttpEntity<RequestType> request,
                            ResponseType responseTypeClass,
                            HttpMethodType methodType,
                            RestTemplate restTemplate,
                            String username) {
        this(baseUrl, request, responseTypeClass, methodType, restTemplate);
        this.username = username;
    }


    public RequestProcessor(String baseUrl,
                            HttpEntity<RequestType> request,
                            ResponseType responseTypeClass,
                            HttpMethodType methodType,
                            RestTemplate restTemplate) {
        this.baseUrl = baseUrl;
        this.request = request;
        this.responseTypeClass = responseTypeClass;
        this.methodType = methodType;
        this.restTemplate = restTemplate;
    }

    @Override
    public void run() {
        try {
            String healthUri = this.baseUrl.concat("/manage/health");
            restTemplate.getForEntity(healthUri, String.class);
            processRequest();
        } catch (Exception e) {
            throw new ServiceUnavailableException("Service unavailable");
        }
    }

    private HttpHeaders createHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        if (this.username != null) {
            headers.set("X-User-Name", this.username);
        }
        return headers;
    }

    private void processRequest() {

        switch (this.methodType) {
            case POST -> {
                restTemplate.postForObject(this.baseUrl, this.request, responseTypeClass.getClass());
            }
            case PUT -> {
                if (this.uid != null) {
                    restTemplate.exchange(this.baseUrl, HttpMethod.PUT, this.request, responseTypeClass.getClass(), this.uid);
                }
            }
//            case UPDATE -> {
//                log.info("UPDATE CASE");
//                LoyaltyInfoResponse response;
//                response = restTemplate.exchange(this.baseUrl,
//                        HttpMethod.GET,
//                        this.request,
//                        LoyaltyInfoResponse.class
//                ).getBody();
//                if (response != null) {
//                    log.info("Service on");
//                    LoyaltyPut loyaltyPut = LoyaltyPut.builder()
//                            .reservationCount(response.getReservationCount() - 1)
//                            .status(response.getStatus())
//                            .build();
//                    var loyaltyRequest = new HttpEntity<>(loyaltyPut, this.createHeader());
//                    restTemplate.exchange(this.baseUrl, HttpMethod.PUT, loyaltyRequest, LoyaltyInfoResponse.class).getBody();
//                } else {
//                    log.info("Service off");
//                }
//            }
        }
    }
}

