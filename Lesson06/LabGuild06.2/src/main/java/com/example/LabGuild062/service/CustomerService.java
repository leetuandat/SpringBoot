/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: LabGuild06.2
 * @date: 3/18/2025
 * @time: 05:11 PM
 * @package: com.example.LabGuild062.service
 */

package com.example.LabGuild062.service;


import com.example.LabGuild062.dto.CustomerCreaterDTO;
import com.example.LabGuild062.dto.CustomerDTO;
import com.example.LabGuild062.dto.CustomerUpdateDTO;
import com.example.LabGuild062.dto.PasswordChangeDTO;
import com.example.LabGuild062.entity.Customer;
import com.example.LabGuild062.exception.CustomerAlreadyExistsException;
import com.example.LabGuild062.exception.CustomerNotFoundException;
import com.example.LabGuild062.exception.InvalidPasswordException;
import com.example.LabGuild062.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerDTO createCustomer(CustomerCreaterDTO customercreateDTO) {
        if (customerRepository.existsByUsername(customercreateDTO.getUsername())) {
            throw new CustomerAlreadyExistsException("Customer already exists: " + customercreateDTO.getUsername());
        }

        Customer customer = new Customer();
        customer.setUsername(customercreateDTO.getUsername());
        customer.setPassword(customercreateDTO.getPassword());
        customer.setFullName(customercreateDTO.getFullName());
        customer.setAddress(customercreateDTO.getAddress());
        customer.setPhone(customercreateDTO.getPhone());
        customer.setEmail(customercreateDTO.getEmail());

        if (customercreateDTO.getBirthDay() != null && !customercreateDTO.getBirthDay().isEmpty()) {
            customer.setBirthDay(LocalDate.parse(customercreateDTO.getBirthDay(), DateTimeFormatter.ISO_DATE));
        }

        customer.setActive(customercreateDTO.isActive());

        Customer savedCustomer = customerRepository.save(customer);
        return convertToDTO(savedCustomer);
    }

    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        return convertToDTO(customer);
    }

    public CustomerDTO getCustomerByUsername(String username) {
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with username: " + username));
        return convertToDTO(customer);
    }

    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public CustomerDTO updateCustomer(Long id, CustomerUpdateDTO customerUpdateDTO) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        if(customerUpdateDTO.getFullName() != null) {
            customer.setFullName(customerUpdateDTO.getFullName());
        }
        if(customerUpdateDTO.getAddress() != null) {
            customer.setAddress(customerUpdateDTO.getAddress());
        }
        if(customerUpdateDTO.getPhone() != null) {
            customer.setPhone(customerUpdateDTO.getPhone());
        }
        if(customerUpdateDTO.getEmail() != null) {
            customer.setEmail(customerUpdateDTO.getEmail());
        }
        if(customerUpdateDTO.getBirthDay() != null) {
            customer.setBirthDay(LocalDate.parse(customerUpdateDTO.getBirthDay()));
        }
        Customer updatedCustomer = customerRepository.save(customer);
        return convertToDTO(updatedCustomer);
    }

    public void changePassword(Long id, PasswordChangeDTO passwordChangeDTO) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        if(!customer.getPassword().equals(passwordChangeDTO.getOldPassword())) {
            throw new InvalidPasswordException("Old password is incorrect");
        }
        customer.setPassword(passwordChangeDTO.getNewPassword());
        customerRepository.save(customer);
    }

    public void setActiveStatus(Long id, boolean active) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));

        customer.setActive(active);
        customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        if(!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }

    private CustomerDTO convertToDTO(Customer customer) {
        return new CustomerDTO(
                customer.getId(),
                customer.getUsername(),
                customer.getFullName(),
                customer.getAddress(),
                customer.getPhone(),
                customer.getEmail(),
                customer.getBirthDay(),
                customer.isActive()
        );
    }
}
