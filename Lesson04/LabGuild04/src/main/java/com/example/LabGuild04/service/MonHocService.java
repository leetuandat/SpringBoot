/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: LabGuild04
 * @date: 3/6/2025
 * @time: 05:46 PM
 * @package: com.example.LabGuild04.service
 */

package com.example.LabGuild04.service;

import com.example.LabGuild04.entity.MonHoc;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MonHocService {
    private final List<MonHoc> monHocList = new ArrayList<>();

    public MonHocService() {
        monHocList.add(new MonHoc("MH01", "Lập trình Java", 60));
        monHocList.add(new MonHoc("MH02", "Cơ sở dữ liệu", 45));
        monHocList.add(new MonHoc("MH03", "Mạng máy tính", 50));
        monHocList.add(new MonHoc("MH04", "Hệ điều hành", 55));
        monHocList.add(new MonHoc("MH05", "Trí tuệ nhân tạo", 70));
    }

    //Lấy toàn bộ danh sách môn học
    public List<MonHoc> getAllMonHoc() {
        return monHocList;
    }

    //Lấy danh sách môn học theo mã
    public Optional<MonHoc> getMonHocById(String mamh) {
        return monHocList.stream().filter(k -> k.getMamh().equalsIgnoreCase(mamh)).findFirst();
    }

    //Thêm mới một môn học
    public MonHoc addMonHoc(MonHoc monHoc) {
        monHocList.add(monHoc);
        return monHoc;
    }

    //Sửa đổi thông tin môn học theo mã
    public boolean updateMonHoc(String mamh, String tenmhMoi, int soTietMoi) {
        return monHocList.stream().filter(k -> k.getMamh().equalsIgnoreCase(mamh)).findFirst().map(k -> {
            k.setTenmh(tenmhMoi);
            k.setSotiet(soTietMoi);
            return true;
        }).orElse(false);
    }

    //Xóa môn học theo mã
    public boolean deleteMonHoc(String mamh) {
        return monHocList.removeIf(k -> k.getMamh().equals(mamh));
    }
}
