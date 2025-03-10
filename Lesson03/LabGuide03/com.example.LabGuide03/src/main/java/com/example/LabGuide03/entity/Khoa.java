/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: LabGuide03
 * @date: 2/25/2025
 * @time: 08:24 PM
 * @package: com.example.LabGuide03.entity
 */

package com.example.LabGuide03.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "Khoa")
public class Khoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String maKH;

    @Column(nullable = false, length = 100)
    String tenKH;

    //Constructor


    public Khoa() {}

    public Khoa(String maKH, String tenKH) {
        this.maKH = maKH;
        this.tenKH = tenKH;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

}


