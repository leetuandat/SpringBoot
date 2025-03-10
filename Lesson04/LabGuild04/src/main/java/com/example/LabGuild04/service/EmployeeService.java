/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: LabGuild04
 * @date: 3/6/2025
 * @time: 05:57 PM
 * @package: com.example.LabGuild04.service
 */

package com.example.LabGuild04.service;

import com.example.LabGuild04.entity.Employee;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private final List<Employee> employeeList = new ArrayList<>();

    public EmployeeService() {
        employeeList.add(new Employee(1L, "Nguyễn Văn A", "Nam", 30, 5000.0));
        employeeList.add(new Employee(2L, "Trần Thị B", "Nữ", 25, 4000.0));
        employeeList.add(new Employee(3L, "Phạm Văn C", "Nam", 28, 4500.0));
        employeeList.add(new Employee(4L, "Lê Thanh D", "Nữ", 35, 6000.0));
        employeeList.add(new Employee(5L, "Đặng Quốc E", "Nam", 40, 7000.0));

    }

    //Lấy toàn bộ danh sách Employee
    public List<Employee> getAllEmployees() {
        return employeeList;
    }

    //Lấy danh sách Employee theo ID
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeList.stream().filter(employee -> employee.getId() == id).findFirst();
    }

    //Thêm mới một Employee
    public Employee addEmployee(Employee employee) {
        employeeList.add(employee);
        return employee;
    }

    //Sửa đổi thông tin employee theo ID
    public boolean updateEmployee(Long id, String fullName, String gender, int age, double salary) {
        for (Employee e : employeeList) {
            if (e.getId().equals(id)) {
                e.setFullName(fullName);
                e.setGender(gender);
                e.setAge(age);
                e.setSalary(salary);
                return true;
            }
        }
        return false;
    }

    // Xóa Employee theo ID
    public boolean deleteEmployee(Long id) {
        return employeeList.removeIf(e -> e.getId().equals(id));
    }
}
