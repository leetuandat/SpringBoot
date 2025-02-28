/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Lesson03
 * @date: 2/25/2025
 * @time: 07:02 PM
 * @package: com.example.lesson03.service
 */

package com.example.lesson03.service;

import com.example.lesson03.entity.Student;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Service class: StudentService
 * Lớp dịch vụ thực hiện các chức năng thao tác với List Object Student
 */

@Service
public class StudentService {
    List<Student> studentList = new ArrayList<Student>();
    public StudentService() {
        studentList.addAll(
                Arrays.asList(
                        new Student(1L, "Le Dat", 22, "male", "19 nguyen thi dinh", "0973526118", "tuandat24034@gmail.com"),
                        new Student(2L, "Manh Duc", 22, "male", "21 Cau Dien", "0326389660", "manhducit05@gmail.com")
                )
        );
    }

    //Lấy toàn bộ danh sách sinh viên
    public List<Student> getStudentList() {
        return studentList;
    }

    //Lấy sinh viên theo id
    public Student getStudent(Long id) {
        return studentList.stream().filter(student -> student.getId() == id).findFirst().orElse(null);
    }

    //Thêm mới sinh viên
    public Student addStudent(Student student) {
        studentList.add(student);
        return student;
    }

    //Cập nhật thông tin sinh viên
    public Student updateStudent(Long id, Student student) {
        Student check = getStudent(id);
        if (check == null) {
            return null;
        }
        studentList.forEach(item -> {
            if (item.getId() == id) {
                item.setName(student.getName());
                item.setAge(student.getAge());
                item.setGender(student.getGender());
                item.setEmail(student.getEmail());
                item.setAddress(student.getAddress());
                item.setPhone(student.getPhone());
            }
        });
        return student;
    }

    //Xóa thông tin sinh viên
    public boolean deleteStudent(Long id) {
        Student student = getStudent(id);
        if (student == null) {
            return false;
        }
        studentList.remove(student);
        return true;
    }
}
