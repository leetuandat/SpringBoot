/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: LabGuide03
 * @date: 2/28/2025
 * @time: 06:25 PM
 * @package: com.example.LabGuide03.controller
 */

package com.example.LabGuide03.controller;

import com.example.LabGuide03.entity.Khoa;
import com.example.LabGuide03.service.KhoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/khoa")
public class KhoaController {
    @Autowired
    private KhoaService khoaService;

    @GetMapping
    public List<Khoa> getAllKhoa() {
        return khoaService.getAll();
    }

    //lay 1 khoa theo ma
    @GetMapping("/{maKH}")
    public Khoa getKhoa(@PathVariable String maKH) {
        return khoaService.getByMaKhoa(maKH);
    }

    // Thêm 1 Khoa mới
    @PostMapping
    public Khoa addKhoa(@RequestBody Khoa khoa) {
        return khoaService.addKhoa(khoa);
    }

    // Cập nhật Khoa
    @PutMapping("/{maKH}")
    public Khoa updateKhoa(@PathVariable String maKhoa, @RequestBody Khoa updated) {
        return khoaService.updateKhoa(maKhoa, updated);
    }

    // Xoá Khoa
    @DeleteMapping("/{maKH}")
    public boolean deleteKhoa(@PathVariable String maKhoa) {
        return khoaService.deleteKhoa(maKhoa);
    }
}
