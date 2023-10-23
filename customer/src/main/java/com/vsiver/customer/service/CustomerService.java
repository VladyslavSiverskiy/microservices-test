package com.vsiver.customer.service;

import com.vsiver.clients.fraud.FraudCheckResponse;
import com.vsiver.clients.fraud.FraudClient;
import com.vsiver.clients.notification.NotificationClient;
import com.vsiver.clients.notification.NotificationRequest;
import com.vsiver.customer.Customer;
import com.vsiver.customer.dto.CustomerRegistrationRequest;
import com.vsiver.customer.repository.CustomerRepository;
import com.vsiver.mssgqueue.RabbitMqMessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final RestTemplate restTemplate;
    private final FraudClient fraudClient;
//    private final NotificationClient notificationClient;
    private final RabbitMqMessageProducer rabbitMqMessageProducer;
//    private final NotificationConfiguration notificationConfiguration;

    @Value("rabbitmq.exchanges.internal")
    private String internalExchange;
    @Value("rabbitmq.queues.notification")
    private String notificationQueue;
    @Value("rabbitmq.routing-keys.internal-notification")
    private String internalNotificationRoutingKey;

    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.getFirstName())
                .secondName(request.getSecondName())
                .email(request.getEmail())
                .build();
        customerRepository.saveAndFlush(customer);

        FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(customer.getId());

        if(fraudCheckResponse.isFraudster()){
            throw new IllegalArgumentException("Customer is fraudster");
        }

        NotificationRequest notificationRequest = new NotificationRequest(
                customer.getId(),
                customer.getEmail(),
                String.format("Hi %s, welcome to my app...",
                        customer.getFirstName())
        );

        rabbitMqMessageProducer.publish(
                notificationRequest,
                internalExchange,
                internalNotificationRoutingKey
        );
    }
}
