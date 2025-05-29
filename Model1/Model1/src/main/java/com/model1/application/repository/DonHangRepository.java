package com.model1.application.repository;

import com.model1.application.entity.DonHang;
import com.model1.application.entity.Order;
import com.model1.application.model.dto.DonHangDTO;
import com.model1.application.model.dto.OrderInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.NamedNativeQuery;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface DonHangRepository extends JpaRepository<DonHang, Long> {
    List<DonHang> findByStatusAndUserId(int status, long userId);

    // Tìm theo status và userId, sắp xếp theo createdAt giảm dần
    List<DonHang> findByStatusAndUserIdOrderByCreatedAtDesc(int status, long userId);

    @Query(value = "SELECT * FROM DonHang " +
            "WHERE (?1 IS NULL OR id LIKE CONCAT('%',?1,'%')) " +
            "AND (?2 IS NULL OR receiver_name LIKE CONCAT('%',?2,'%')) " +
            "AND (?3 IS NULL OR receiver_phone LIKE CONCAT('%',?3,'%')) " +
            "AND (?4 IS NULL OR status = ?4) " +
            "AND (?5 IS NULL OR total_price = ?5)", nativeQuery = true)
    Page<DonHang> adminGetListOrder(String id, String name, String phone, Integer status, Long total, Pageable pageable);

    // Doanh thu theo tháng trong năm
    @Query("SELECT MONTH(o.createdAt) as month, SUM(o.totalPrice) " +
            "FROM DonHang o WHERE o.status = 3 AND YEAR(o.createdAt) = :year " +
            "GROUP BY MONTH(o.createdAt)")
    List<Object[]> getRevenueByMonth(@Param("year") int year);

    // Doanh thu theo ngày trong tháng
    @Query("SELECT DAY(o.createdAt) as day, SUM(o.totalPrice) " +
            "FROM DonHang o WHERE o.status = 3 AND MONTH(o.createdAt) = :month AND YEAR(o.createdAt) = YEAR(CURRENT_DATE) " +
            "GROUP BY DAY(o.createdAt)")
    List<Object[]> getRevenueByDay(@Param("month") int month);

    // Doanh thu hôm nay
    @Query("SELECT SUM(o.totalPrice) " +
            "FROM DonHang o WHERE o.status = 3 AND DATE(o.createdAt) = CURRENT_DATE")
    Double getTodayRevenue();

    // Doanh thu tháng này
    @Query("SELECT SUM(o.totalPrice) " +
            "FROM DonHang o WHERE o.status = 3 AND MONTH(o.createdAt) = MONTH(CURRENT_DATE) AND YEAR(o.createdAt) = YEAR(CURRENT_DATE)")
    Double getMonthRevenue();

    // Tổng doanh thu
    @Query("SELECT SUM(o.totalPrice) FROM DonHang o WHERE o.status = 3")
    Double getTotalRevenue();

    // Doanh thu theo ngày (7 ngày gần nhất)
//    @Query("SELECT DATE(o.createdAt) as date, SUM(o.totalPrice) " +
//            "FROM donhang o WHERE o.status = 3 AND o.createdAt >= :startDate " +
//            "GROUP BY DATE(o.createdAt)")
//    List<Object[]> getDailyRevenue(@Param("startDate") Timestamp startDate);
    @Query(value = "SELECT DATE(created_at), SUM(total_price) " +
            "FROM DonHang WHERE status = 3 AND created_at >= :startDate " +
            "GROUP BY DATE(created_at)", nativeQuery = true)
    List<Object[]> getDailyRevenue(@Param("startDate") Timestamp startDate);

}
