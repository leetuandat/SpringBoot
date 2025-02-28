/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: LabGuide03
 * @date: 2/28/2025
 * @time: 06:32 PM
 * @package: com.example.LabGuide03.controller
 */

package com.example.LabGuide03.controller;
import com.example.LabGuide03.entity.Employee;
import com.example.LabGuide03.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAll();
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable int id) {
        return employeeService.getById(id);
    }

    @PostMapping
    public Employee addEmployee(@RequestBody Employee employee) {
        return employeeService.addEmployee(employee);
    }

    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable int id, @RequestBody Employee updated) {
        return employeeService.updateEmployee(id, updated);
    }

    @DeleteMapping("/{id}")
    public boolean deleteEmployee(@PathVariable int id) {
        return employeeService.deleteEmployee(id);
    }

}
