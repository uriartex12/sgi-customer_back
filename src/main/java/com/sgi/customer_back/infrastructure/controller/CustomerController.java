package com.sgi.customer_back.infrastructure.controller;

import com.sgi.customer_back.domain.ports.in.CustomerService;
import com.sgi.customer_back.infrastructure.dto.CustomerRequest;
import com.sgi.customer_back.infrastructure.dto.CustomerResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class CustomerController implements V1Api {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public Mono<ResponseEntity<CustomerResponse>> createCustomer(Mono<CustomerRequest> customerRequest, ServerWebExchange exchange) {
        return customerService.createCustomer(customerRequest);
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteCustomer(String id, ServerWebExchange exchange) {
        return customerService.deleteCustomer(id);
    }

    @Override
    public Mono<ResponseEntity<Flux<CustomerResponse>>> getAllCustomers(ServerWebExchange exchange) {
        return customerService.getAllCustomers();
    }

    @Override
    public Mono<ResponseEntity<CustomerResponse>> getCustomerById(String id, ServerWebExchange exchange) {
        return customerService.getCustomerById(id);
    }

    @Override
    public Mono<ResponseEntity<CustomerResponse>> updateCustomer(String id, Mono<CustomerRequest> customerRequest, ServerWebExchange exchange) {
        return customerService.updateCustomer(id, customerRequest);
    }
}
