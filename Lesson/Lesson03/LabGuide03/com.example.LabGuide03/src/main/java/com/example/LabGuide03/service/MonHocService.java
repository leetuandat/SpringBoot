/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: LabGuide03
 * @date: 2/28/2025
 * @time: 06:08 PM
 * @package: com.example.LabGuide03.service
 */

package com.example.LabGuide03.service;

import com.example.LabGuide03.entity.MonHoc;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MonHocService {
    private List<MonHoc> monHocs = new ArrayList<>();

    public MonHocService() {
        monHocs.add(new MonHoc("MH001", "Lập trình Java", 3));
        monHocs.add(new MonHoc("MH002", "Cấu trúc dữ liệu", 4));
        monHocs.add(new MonHoc("MH003", "Hệ điều hành", 3));
    }

    public List<MonHoc> getAll() {
        return monHocs;
    }

    public MonHoc getByMaMH(String maMH) {
        return monHocs.stream()
                .filter(m -> m.getMaMH().equals(maMH))
                .findFirst()
                .orElse(null);
    }

    public MonHoc addMonHoc(MonHoc monHoc) {
        if (getByMaMH(monHoc.getMaMH()) != null) {
            return null;
        }
        monHocs.add(monHoc);
        return monHoc;
    }

    public MonHoc updateMonHoc(String maMH, MonHoc updated) {
        for (int i = 0; i < monHocs.size(); i++) {
            if (monHocs.get(i).getMaMH().equalsIgnoreCase(maMH)) {
                monHocs.get(i).setTenMH(updated.getTenMH());
                monHocs.get(i).setSoTiet(updated.getSoTiet());
                return monHocs.get(i);
            }
        }
        return null;
    }

    public boolean deleteMonHoc(String maMH) {
        return monHocs.removeIf(m -> m.getMaMH().equalsIgnoreCase(maMH));
    }
}
