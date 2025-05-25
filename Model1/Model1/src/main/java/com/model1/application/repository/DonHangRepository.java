package com.model1.application.repository;

import com.model1.application.entity.DonHang;
import com.model1.application.entity.Order;
import com.model1.application.model.dto.DonHangDTO;
import com.model1.application.model.dto.OrderInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.NamedNativeQuery;
import java.util.List;

@Repository
public interface DonHangRepository extends JpaRepository<DonHang, Long> {
    List<DonHang> findByStatusAndUserId(int status, long userId);

    // Tìm theo status và userId, sắp xếp theo createdAt giảm dần
    List<DonHang> findByStatusAndUserIdOrderByCreatedAtDesc(int status, long userId);

    @Query(value = "SELECT * FROM donhang " +
            "WHERE (?1 IS NULL OR id LIKE CONCAT('%',?1,'%')) " +
            "AND (?2 IS NULL OR receiver_name LIKE CONCAT('%',?2,'%')) " +
            "AND (?3 IS NULL OR receiver_phone LIKE CONCAT('%',?3,'%')) " +
            "AND (?4 IS NULL OR status = ?4) " +
            "AND (?5 IS NULL OR total_price = ?5)", nativeQuery = true)
    Page<DonHang> adminGetListOrder(String id, String name, String phone, Integer status, Long total, Pageable pageable);
}
