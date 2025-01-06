package com.sgi.customer.application.service;

import com.sgi.customer.domain.model.Customer;
import com.sgi.customer.domain.ports.in.CustomerService;
import com.sgi.customer.domain.ports.out.CustomerRepository;
import com.sgi.customer.domain.shared.CustomError;
import com.sgi.customer.infrastructure.dto.CustomerRequest;
import com.sgi.customer.infrastructure.dto.CustomerResponse;
import com.sgi.customer.infrastructure.exception.CustomException;
import com.sgi.customer.infrastructure.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of the CustomerService interface.
 * Provides methods for creating, retrieving, updating, and deleting customer data.
 * Interacts with the repository to persist customer information.
 */
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Mono<CustomerResponse> createCustomer(Mono<CustomerRequest> customer) {
        return customer.flatMap(customerRequest ->
                CustomerMapper.INSTANCE.map(Mono.just(customerRequest))
                        .flatMap(customerRepository::save));
    }

    @Override
    public Mono<Void> deleteCustomer(String id) {
        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(CustomError.E_CUSTOMER_NOT_FOUND)))
                .flatMap(customerRepository::delete);
    }

    @Override
    public Flux<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Mono<CustomerResponse> getCustomerById(String id) {
        return customerRepository.findById(id)
                .map(CustomerMapper.INSTANCE::toCustomerResponse);
    }

    @Override
    public Mono<CustomerResponse> updateCustomer(String id, Mono<CustomerRequest> customerRequestMono) {
        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(CustomError.E_CUSTOMER_NOT_FOUND)))
                .flatMap(customer ->
                        customerRequestMono.map(updatedCustomer -> {
                            Customer updatedEntity = CustomerMapper.INSTANCE.toCustomer(updatedCustomer, customer.getId());
                            updatedEntity.setId(customer.getId());
                            return updatedEntity;
                        })
                )
                .flatMap(customerRepository::save);
    }
}
