/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: LabGuide03
 * @date: 2/25/2025
 * @time: 08:31 PM
 * @package: com.example.LabGuide03.entity
 */

package com.example.LabGuide03.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "monhoc")
public class MonHoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String maMH;

    @Column(nullable = false, length = 100)
    private String tenMH;

    @Column(nullable = false)
    private int soTiet;

    //Constructor


    public MonHoc() {}

    public MonHoc(String maMH, String tenMH, int soTiet) {
        this.maMH = maMH;
        this.tenMH = tenMH;
        this.soTiet = soTiet;
    }

    public String getMaMH() {
        return maMH;
    }

    public void setMaMH(String maMH) {
        this.maMH = maMH;
    }

    public String getTenMH() {
        return tenMH;
    }

    public void setTenMH(String tenMH) {
        this.tenMH = tenMH;
    }

    public int getSoTiet() {
        return soTiet;
    }

    public void setSoTiet(int soTiet) {
        this.soTiet = soTiet;
    }
}
