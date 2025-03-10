/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: LabGuild04
 * @date: 3/5/2025
 * @time: 06:01 PM
 * @package: com.example.LabGuild04.service
 */

package com.example.LabGuild04.service;

import com.example.LabGuild04.entity.Khoa;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

@Service

public class KhoaService {
    private final List<Khoa> khoaList = new ArrayList<>();

    public KhoaService() {
        khoaList.add(new Khoa("KH01", "Công nghệ thông tin"));
        khoaList.add(new Khoa("KH02", "Kinh tế"));
        khoaList.add(new Khoa("KH03", "Luật"));
        khoaList.add(new Khoa("KH04", "Ngoại ngữ"));
        khoaList.add(new Khoa("KH05", "Kỹ thuật"));
    }

    //Lấy toàn bộ danh sách khoa
    public List<Khoa> getAllKhoa() {
        return khoaList;
    }

    //Lấy danh sách khoa theo mã
    public Optional<Khoa> getKhoaById(String makh) {
        Stream<Khoa> result = khoaList.stream().filter(k -> k.getMakh().equalsIgnoreCase(makh));
        return result.findFirst();
    }

    //Thêm mới một khoa
    public Khoa addKhoa(Khoa khoa) {
        khoaList.add(khoa);
        return khoa;
    }

    //Sửa đổi thông tin khoa theo mã
    public boolean updateKhoa(String makh, String tenkhMoi) {
        return khoaList.stream().filter(k -> k.getMakh().equalsIgnoreCase(makh)).findFirst().map(k -> {
            k.setTenkh(tenkhMoi);
            return true;
        }).orElse(false);
    }

    //Xóa thông tin theo mã khoa
    public boolean deleteKhoa(String makh) {
        return khoaList.removeIf(k -> k.getMakh().equals(makh));
    }
}
