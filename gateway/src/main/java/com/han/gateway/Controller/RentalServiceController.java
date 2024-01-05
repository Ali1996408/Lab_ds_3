package com.han.gateway.Controller;


import com.han.gateway.payload.*;
import com.han.gateway.service.GatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rental")
public class RentalServiceController {


    private final RestTemplate restTemplate;
    private final GatewayService gatewayService;
    private final String rentalServiceBaseUrl = "http://localhost:8060/api/v1/rental";

    private final String paymentBaseUrl = "http://localhost:8080/api/v1/payments";

    private int count = 0;

    @Autowired
    public RentalServiceController(RestTemplate restTemplate, GatewayService gatewayService) {
        this.restTemplate = restTemplate;
        this.gatewayService = gatewayService;
    }


    @GetMapping("/manage/health")
    public ResponseEntity<String> manageHealth() {
        try {
            String healthUri = rentalServiceBaseUrl.concat("/manage/health");
            return restTemplate.getForEntity(healthUri, String.class);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Rental service unavailable");
        }
    }


    @GetMapping(produces = "application/json")
    public List<RentalShortResponse> getRentals(@RequestHeader("X-User-Name") String xUserName) {
        HttpHeaders headers = gatewayService.createHeader(xUserName);
        HttpEntity<HttpHeaders> request = new HttpEntity<>(headers);
        RentalResponse[] rentalResponses = restTemplate.exchange(rentalServiceBaseUrl,
                HttpMethod.GET, request, RentalResponse[].class).getBody();
        List<RentalShortResponse> results = new ArrayList<>();
        assert rentalResponses != null;
        for (RentalResponse rentalResponse : rentalResponses) {
            PaymentResponse payment = restTemplate.getForObject(paymentBaseUrl.concat("/{paymentUid}"),
                    PaymentResponse.class, rentalResponse.getPaymentUid());
            assert payment != null;
            results.add(RentalShortResponse
                    .builder()
                    .rentalUid(rentalResponse.getRentalUid())
                    .dateTo(rentalResponse.getDateTo())
                    .carUid(rentalResponse.getCarUid())
                    .username(rentalResponse.getUsername())
                    .payment(PaymentInfoResponse
                            .builder()
                            .price(payment.getPrice())
                            .status(payment.getStatus())
                            .build())
                    .dateFrom(rentalResponse.getDateFrom())
                    .status(rentalResponse.getStatus())
                    .build());
        }
        return results;
    }

    @GetMapping(value = "/{rentalUid}", produces = "application/json")
    public RentalShortResponse getRental(@RequestHeader("X-User-Name") String xUserName,
                                         @PathVariable("rentalUid") UUID rentalUid) {
        RentalResponse rental = getRentalLong(xUserName, rentalUid);
        assert rental != null;
        PaymentResponse payment = restTemplate.getForObject(paymentBaseUrl.concat("/{paymentUid}"),
                PaymentResponse.class, rental.getPaymentUid());
//        Car car = rental.getCar();
        assert payment != null;
        return RentalShortResponse
                .builder()
                .rentalUid(rentalUid)
                .carUid(rental.getCarUid())
                .username(rental.getUsername())
                .dateFrom(rental.getDateFrom())
                .dateTo(rental.getDateTo())
                .status(rental.getStatus())
                .payment(PaymentInfoResponse
                        .builder()
                        .status(payment.getStatus())
                        .price(payment.getPrice())
                        .build()
                )
                .build();
    }


    private RentalResponse getRentalLong(String xUserName, UUID rentalUid) {
        String uri = rentalServiceBaseUrl.concat("/{rentalUid}");
        HttpHeaders headers = gatewayService.createHeader(xUserName);
        HttpEntity<HttpHeaders> request = new HttpEntity<>(headers);
        return restTemplate
                .exchange(uri, HttpMethod.GET, request, RentalResponse.class, rentalUid).getBody();
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{rentalUid}")
    public void cancelRental(@RequestHeader("X-User-Name") String xUserName,
                             @PathVariable("rentalUid") UUID rentalUid) {
        //HttpHeaders headers = gatewayService.createHeader(xUserName);
        RentalResponse rental = getRentalLong(xUserName, rentalUid);
        assert rental != null;
        PaymentResponse payment = restTemplate.getForObject(paymentBaseUrl.concat("/{paymentUid}"),
                PaymentResponse.class, rental.getPaymentUid());

        assert payment != null;
        UUID paymentUid = payment.getPaymentUid();

        HttpHeaders paymentHeaders = gatewayService.createHeader();

        PaymentPut paymentPut = new PaymentPut();
        paymentPut.setStatus("CANCELED");
        HttpEntity<PaymentPut> paymentRequest = new HttpEntity<>(paymentPut, paymentHeaders);

        restTemplate.exchange(paymentBaseUrl.concat("/{paymentUid}"), HttpMethod.PUT, paymentRequest, PaymentResponse.class, paymentUid);
        String uriToDelete = "http://localhost:8060/api/v1/rental/{rentalUid}";
        restTemplate.delete(uriToDelete, rentalUid);
    }

}
