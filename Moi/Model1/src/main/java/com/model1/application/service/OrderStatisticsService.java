package com.model1.application.service;

import com.model1.application.repository.DonHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.sql.Timestamp;

@Service
public class OrderStatisticsService {

    @Autowired
    private DonHangRepository donHangRepository;

    public Map<Integer, Double> getRevenueByMonth(int year) {
        List<Object[]> data = donHangRepository.getRevenueByMonth(year);
        Map<Integer, Double> result = new TreeMap<>();
        for (Object[] row : data) {
            result.put((Integer) row[0], ((Number) row[1]).doubleValue());
        }
        return result;
    }

    public Map<Integer, Double> getRevenueByDay(int month) {
        List<Object[]> data = donHangRepository.getRevenueByDay(month);
        Map<Integer, Double> result = new TreeMap<>();
        for (Object[] row : data) {
            result.put((Integer) row[0], ((Number) row[1]).doubleValue());
        }
        return result;
    }

    public Double getTodayRevenue() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay(); // 00:00:00
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX); // 23:59:59.999999999

        Timestamp start = Timestamp.valueOf(startOfDay);
        Timestamp end = Timestamp.valueOf(endOfDay);

        Double revenue = donHangRepository.getTodayRevenue(start, end);

        return revenue;
    }

    public Double getMonthRevenue() {
        return donHangRepository.getMonthRevenue();
    }

    public Double getTotalRevenue() {
        return donHangRepository.getTotalRevenue();
    }

    public Map<String, Double> getLast7DaysRevenue() {
        Timestamp sevenDaysAgo = Timestamp.from(LocalDateTime.now().minusDays(6).atZone(java.time.ZoneId.systemDefault()).toInstant());
        List<Object[]> data = donHangRepository.getDailyRevenue(sevenDaysAgo);
        Map<String, Double> result = new TreeMap<>();
        for (Object[] row : data) {
            result.put(row[0].toString(), ((Number) row[1]).doubleValue());
        }
        return result;
    }
}
