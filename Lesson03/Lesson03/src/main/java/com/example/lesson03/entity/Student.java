/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Lesson03
 * @date: 2/25/2025
 * @time: 06:57 PM
 * @package: com.example.lesson03.entity
 */

package com.example.lesson03.entity;

public class Student {
    Long id;
    String name;
    int age;
    String gender;
    String address;
    String phone;
    String email;

    public Student() {}

    public Student(Long id, String name, int age, String gender, String address, String phone, String email) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
