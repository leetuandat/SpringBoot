/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: LabGuide03
 * @date: 2/25/2025
 * @time: 09:22 PM
 * @package: com.example.LabGuide03.service
 */

package com.example.LabGuide03.service;

import com.example.LabGuide03.entity.Khoa;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class KhoaService {
    private final List<Khoa> khoaList = new ArrayList<>();

    //Khởi tạo danh sách 5 phần tử

    public KhoaService() {
        khoaList.addAll(Arrays.asList(
                new Khoa("K001", "Công nghệ thông tin"),
                new Khoa("K002", "Khoa Kinh tế"),
                new Khoa("K003", "Khoa Ngôn ngữ Anh"),
                new Khoa("K004", "Khoa Xây dựng"),
                new Khoa("K005", "Khoa Điện - Điện tử")
        ));
    }

    //Lấy toàn bộ danh sách
    public List<Khoa> getAll() {
        return khoaList;
    }

    //Lấy danh sách theo mã khoa
    public Khoa getByMaKhoa(String maKhoa) {
        return khoaList.stream()
                .filter(k -> k.getMaKH().equalsIgnoreCase(maKhoa))
                .findFirst()
                .orElse(null);
    }

    //Thêm mới một khoa
    public Khoa addKhoa(Khoa khoa) {
        khoaList.add(khoa);
        return khoa;
    }

    //Sửa đổi thông tin khoa theo mã
    public Khoa updateKhoa(String maKhoa, Khoa updatedKhoa) {
        for (int i = 0; i < khoaList.size(); i++) {
            if (khoaList.get(i).getMaKH().equalsIgnoreCase(maKhoa)) {
                khoaList.get(i).setTenKH(updatedKhoa.getTenKH());
                return khoaList.get(i);
            }
        }
        return null;
    }

    //Xóa thông tin khoa theo mã
    public boolean deleteKhoa(String maKhoa) {
        return khoaList.removeIf(k -> k.getMaKH().equalsIgnoreCase(maKhoa));
    }
}
