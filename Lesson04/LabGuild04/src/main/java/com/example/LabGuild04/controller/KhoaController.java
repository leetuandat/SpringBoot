/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: LabGuild04
 * @date: 3/7/2025
 * @time: 11:45 AM
 * @package: com.example.LabGuild04.controller
 */

package com.example.LabGuild04.controller;

import com.example.LabGuild04.entity.Khoa;
import com.example.LabGuild04.service.KhoaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/khoa")
public class KhoaController {
    private final KhoaService khoaService;

    public KhoaController(KhoaService khoaService) {
        this.khoaService = khoaService;
    }

    @GetMapping
    public List<Khoa> getAllKhoa() {
        return khoaService.getAllKhoa();
    }

    @GetMapping("/{makh}")
    public ResponseEntity<Khoa> getKhoaById(@PathVariable String makh) {
        return khoaService.getKhoaById(makh)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Khoa> addKhoa(@RequestBody Khoa khoa) {
        Khoa createKhoa = khoaService.addKhoa(khoa);
        return ResponseEntity.ok(createKhoa);
    }

    @PutMapping("/{makh}")
    public ResponseEntity<String> updateKhoa(@PathVariable String makh, @RequestBody Khoa khoa) {
        boolean updated = khoaService.updateKhoa(makh, khoa.getTenkh());
        return updated ? ResponseEntity.ok("Cập nhật thành công") : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{makh}")
    public ResponseEntity<String> deleteKhoa(@PathVariable String makh) {
        boolean deleted = khoaService.deleteKhoa(makh);
        return deleted ? ResponseEntity.ok("Xóa thành công") : ResponseEntity.notFound().build();
    }
}
