/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: LabGuild06.2
 * @date: 3/18/2025
 * @time: 06:01 PM
 * @package: com.example.LabGuild062.controller
 */

package com.example.LabGuild062.controller;

import com.example.LabGuild062.dto.CustomerCreaterDTO;
import com.example.LabGuild062.dto.CustomerDTO;
import com.example.LabGuild062.dto.CustomerUpdateDTO;
import com.example.LabGuild062.dto.PasswordChangeDTO;
import com.example.LabGuild062.repository.CustomerRepository;
import com.example.LabGuild062.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerCreaterDTO customerCreateDTO) {
        CustomerDTO createdCustomer = customerService.createCustomer(customerCreateDTO);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    // Read
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        CustomerDTO customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<CustomerDTO> getCustomerByUsernameg(@PathVariable String username) {
        CustomerDTO customer = customerService.getCustomerByUsername(username);
        return ResponseEntity.ok(customer);
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id, @RequestBody CustomerUpdateDTO customerUpdateDTO) {
        CustomerDTO updatedCustomer = customerService.updateCustomer(id, customerUpdateDTO);
        return ResponseEntity.ok(updatedCustomer);
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(@PathVariable Long id, @Valid @RequestBody PasswordChangeDTO passwordChangeDTO) {
        customerService.changePassword(id, passwordChangeDTO);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/active")
    public ResponseEntity<Void> setActiveStatus(@PathVariable Long id, @RequestParam boolean active) {
        customerService.setActiveStatus(id, active);
        return ResponseEntity.noContent().build();
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/customers")
    public String getAllCustomers(Model model) {
        List<CustomerDTO> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "customerList"; // View name is customerList.html
    }

    @GetMapping("/customer/{id}")
    public String getCustomerById(@PathVariable Long id, Model model) {
        CustomerDTO customer = customerService.getCustomerById(id);
        model.addAttribute("customer", customer);
        return "customerDetail"; // View name is customerDetail.html
    }
}
