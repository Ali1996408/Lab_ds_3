package com.han.payment.controller;

import com.han.payment.payload.PaymentPut;
import com.han.payment.payload.PaymentRequest;
import com.han.payment.payload.PaymentResponse;
import com.han.payment.service.PaymentServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final PaymentServiceImpl paymentService;

    @Autowired
    public PaymentController(PaymentServiceImpl paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public PaymentResponse create(@RequestBody PaymentRequest paymentRequest) {
        return paymentService.createPayment(paymentRequest);
    }

    @PutMapping(value = "/{paymentUid}", consumes = "application/json", produces = "application/json")
    public PaymentResponse updatePaymentStatus(@PathVariable("paymentUid") UUID paymentUid,
                                               @RequestBody @Valid PaymentPut paymentPut) {
        return paymentService.updatePayment(paymentUid, paymentPut);
    }

    @GetMapping(value = "/{paymentUid}", produces = "application/json")
    public PaymentResponse getPayment(@PathVariable("paymentUid") UUID paymentUid) {
        return paymentService.getPayment(paymentUid);
    }

    //+++
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/manage/health")
    public String checkServiceAvailability(){
        return "Payment service is available";
    }
}
