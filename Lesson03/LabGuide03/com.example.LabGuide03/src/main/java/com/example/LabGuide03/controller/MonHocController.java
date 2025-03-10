/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: LabGuide03
 * @date: 2/28/2025
 * @time: 06:29 PM
 * @package: com.example.LabGuide03.controller
 */

package com.example.LabGuide03.controller;
import com.example.LabGuide03.entity.MonHoc;
import com.example.LabGuide03.service.MonHocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/monhoc")
public class MonHocController {

    @Autowired
    private MonHocService monHocService;

    @GetMapping
    public List<MonHoc> getAllMonHoc() {
        return monHocService.getAll();
    }

    @GetMapping("/{maMH}")
    public MonHoc getMonHocByMa(@PathVariable String maMH) {
        return monHocService.getByMaMH(maMH);
    }

    @PostMapping
    public MonHoc addMonHoc(@RequestBody MonHoc monHoc) {
        return monHocService.addMonHoc(monHoc);
    }

    @PutMapping("/{maMH}")
    public MonHoc updateMonHoc(@PathVariable String maMH, @RequestBody MonHoc updated) {
        return monHocService.updateMonHoc(maMH, updated);
    }

    @DeleteMapping("/{maMH}")
    public boolean deleteMonHoc(@PathVariable String maMH) {
        return monHocService.deleteMonHoc(maMH);
    }

}
