package com.vsiver.fraud.controller;


import com.vsiver.clients.fraud.FraudCheckResponse;
import com.vsiver.fraud.service.FraudCheckService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/fraud-check")
public class FraudController {

    private final FraudCheckService fraudCheckService;

    public FraudController(FraudCheckService fraudCheckService) {
        this.fraudCheckService = fraudCheckService;
    }

    @GetMapping("/{customerId}")
    public FraudCheckResponse isFraudster(@PathVariable Integer customerId) {
        log.info("Checking customer with ID " + customerId);
        FraudCheckResponse fraudCheckResponse = new FraudCheckResponse();
        fraudCheckResponse.setFraudster(fraudCheckService.isFraudulentCustomer(customerId));
        return fraudCheckResponse;
    }
}
