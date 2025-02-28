/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: LabGuide03
 * @date: 2/28/2025
 * @time: 06:22 PM
 * @package: com.example.LabGuide03.service
 */

package com.example.LabGuide03.service;
import com.example.LabGuide03.entity.Employee;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {
    private final List<Employee> employees = new ArrayList<>();

    public EmployeeService() {
        employees.add(new Employee(1, "Nguyễn Văn A", "Nam", 25, 500.0));
        employees.add(new Employee(2, "Trần Thị B", "Nữ", 30, 600.0));
        employees.add(new Employee(3, "Lê Văn C", "Nam", 28, 550.0));
    }

    public List<Employee> getAll() {
        return employees;
    }

    public Employee getById(int id) {
        return employees.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Employee addEmployee(Employee employee) {
        if (getById(employee.getId()) != null) {
            return null;
        }
        employees.add(employee);
        return employee;
    }

    public Employee updateEmployee(int id, Employee updated) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId() == id) {
                employees.get(i).setFullName(updated.getFullName());
                employees.get(i).setGender(updated.getGender());
                employees.get(i).setAge(updated.getAge());
                employees.get(i).setSalary(updated.getSalary());
                return employees.get(i);
            }
        }
        return null;
    }

    public boolean deleteEmployee(int id) {
        return employees.removeIf(e -> e.getId() == id);
    }
}
