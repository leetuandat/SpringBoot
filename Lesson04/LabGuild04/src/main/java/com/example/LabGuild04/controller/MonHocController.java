/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: LabGuild04
 * @date: 3/11/2025
 * @time: 12:32 AM
 * @package: com.example.LabGuild04.controller
 */

package com.example.LabGuild04.controller;

import com.example.LabGuild04.dto.MonHocDTO;
import com.example.LabGuild04.entity.MonHoc;
import com.example.LabGuild04.service.MonHocService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/monhoc")
public class MonHocController {
    private final MonHocService monHocService;
    public MonHocController(MonHocService monHocService) {
        this.monHocService = monHocService;
    }

    @GetMapping
    public List<MonHoc> GetAllMonHoc() {
        return monHocService.getAllMonHoc();
    }

    @GetMapping("/{mamh}")
    public ResponseEntity<MonHoc> GetMonHocById(@PathVariable String mamh) {
        return monHocService.getMonHocById(mamh).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MonHoc> addMonHoc(@RequestBody MonHoc monHoc) {
        MonHoc createMonHoc = monHocService.addMonHoc(monHoc);
        return ResponseEntity.ok(createMonHoc);
    }

    @PutMapping("/{mamh}")
    public ResponseEntity<MonHoc> updateMonHoc(@PathVariable String mamh, @RequestBody MonHoc monHoc) {
        boolean updated = monHocService.updateMonHoc(mamh, monHoc.getTenmh(), monHoc.getSotiet());
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{mamh}")
    public ResponseEntity<String> deleteKhoa(@PathVariable String mamh) {
        boolean deleted = monHocService.deleteMonHoc(mamh);
        return deleted ? ResponseEntity.ok("Xóa thành công") : ResponseEntity.notFound().build();
    }

}
