/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Lesson03
 * @date: 2/25/2025
 * @time: 07:15 PM
 * @package: com.example.lesson03.controller
 */

package com.example.lesson03.controller;

import com.example.lesson03.entity.Student;
import com.example.lesson03.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StudentController {
    @Autowired
    private StudentService studentService;
    /**
     * Lấy danh sách sinh viên
     */

    @GetMapping("/student-list")
    public List<Student> getStudentList() {
        return studentService.getStudentList();
    }

    /**
     * Lấy thông tin sinh viên theo id
     * @param: id
     */

    @GetMapping("/student-list/{id}")
    public Student getStudentById(@PathVariable String id) {
        Long idStudent = Long.parseLong(id);
        return studentService.getStudent(idStudent);
    }

    /**
     * Thêm mới 1 sinh viên
     */

    @PostMapping("/student-add")
    //@RequestBody lấy toàn bộ thông tin sinh viên
    public Student addStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    /**
     * Sửa thông tin sinh viên
     */

    @PutMapping("/student/{id}")
    public Student updateStudent(@PathVariable String id, @RequestBody Student student) {
        Long idStudent = Long.parseLong(id);
        return studentService.updateStudent(idStudent, student);
    }

    /**
     * Xóa 1 sinh viên
     */

    @DeleteMapping("/student/{id}")
    public boolean deleteStudent(@PathVariable String id) {
        Long idStudent = Long.parseLong(id);
        return studentService.deleteStudent(idStudent);
    }
}
