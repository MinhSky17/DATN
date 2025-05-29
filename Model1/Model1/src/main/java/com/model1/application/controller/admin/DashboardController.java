package com.model1.application.controller.admin;

import com.model1.application.model.dto.ChartDTO;
import com.model1.application.model.dto.StatisticDTO;
import com.model1.application.model.request.FilterDayByDay;
import com.model1.application.repository.*;
import com.model1.application.service.*;
import com.model1.application.repository.*;
import com.model1.application.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Year;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

    @Autowired
    private PostService postService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private StatisticRepository statisticRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderStatisticsService orderStatisticsService;

    @GetMapping("/admin")
    public String dashboard(Model model){
        Double totalRevenue = orderStatisticsService.getTotalRevenue();

        // Doanh thu tháng hiện tại
        Double monthRevenue = orderStatisticsService.getMonthRevenue();

        // Doanh thu hôm nay
        Double todayRevenue = orderStatisticsService.getTodayRevenue();

        // Doanh thu 7 ngày gần nhất (theo ngày)
        Map<String, Double> last7DaysRevenue = orderStatisticsService.getLast7DaysRevenue();

        // Doanh thu theo tháng trong năm hiện tại
        int currentYear = Year.now().getValue();
        Map<Integer, Double> revenueByMonth = orderStatisticsService.getRevenueByMonth(currentYear);


        // Đưa dữ liệu sang Thymeleaf
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("monthRevenue", monthRevenue);
        model.addAttribute("todayRevenue", todayRevenue);
        model.addAttribute("last7DaysRevenue", last7DaysRevenue);
        model.addAttribute("revenueByMonth", revenueByMonth);
        model.addAttribute("currentYear", currentYear);

        //return "admin/dashboard"; // Trỏ tới dashboard.html trong templates/admin/
        return "admin/index";
    }

    @GetMapping("/api/admin/revenue-by-month/{year}")
    public ResponseEntity<Map<Integer, Double>> getRevenueByMonth(@PathVariable int year) {
        Map<Integer, Double> revenueByMonth = orderStatisticsService.getRevenueByMonth(year);
        return ResponseEntity.ok(revenueByMonth);
    }

    @GetMapping("/api/admin/count/posts")
    public ResponseEntity<Object> getCountPost(){
        long countPosts = postService.getCountPost();
        return ResponseEntity.ok(countPosts);
    }

    @GetMapping("/api/admin/count/products")
    public ResponseEntity<Object> getCountProduct(){
        long countProducts = productService.getCountProduct();
        return ResponseEntity.ok(countProducts);
    }

    @GetMapping("/api/admin/count/orders")
    public ResponseEntity<Object> getCountOrders(){
        long countOrders = orderService.getCountOrder();
        return ResponseEntity.ok(countOrders);
    }

    @GetMapping("/api/admin/count/categories")
    public ResponseEntity<Object> getCountCategories(){
        long countCategories = categoryService.getCountCategories();
        return ResponseEntity.ok(countCategories);
    }

    @GetMapping("/api/admin/count/brands")
    public ResponseEntity<Object> getCountBrands(){
        long countBrands = brandService.getCountBrands();
        return ResponseEntity.ok(countBrands);
    }

    @GetMapping("/api/admin/count/users")
    public ResponseEntity<Object> getCountUsers(){
        long countUsers = userRepository.count();
        return ResponseEntity.ok(countUsers);
    }

    @GetMapping("/api/admin/statistics")
    public ResponseEntity<Object> getStatistic30Day(){
        List<StatisticDTO> statistics = statisticRepository.getStatistic30Day();
        return ResponseEntity.ok(statistics);
    }

    @PostMapping("/api/admin/statistics")
    public ResponseEntity<Object> getStatisticDayByDay(@RequestBody FilterDayByDay filterDayByDay){
        List<StatisticDTO> statisticDTOS = statisticRepository.getStatisticDayByDay(filterDayByDay.getToDate(),filterDayByDay.getFromDate());
        return ResponseEntity.ok(statisticDTOS);
    }

    @GetMapping("/api/admin/product-order-categories")
    public ResponseEntity<Object> getListProductOrderCategories(){
        List<ChartDTO> chartDTOS = categoryRepository.getListProductOrderCategories();
        return ResponseEntity.ok(chartDTOS);
    }

    @GetMapping("/api/admin/product-order-brands")
    public ResponseEntity<Object> getProductOrderBrands(){
        List<ChartDTO> chartDTOS = brandRepository.getProductOrderBrands();
        return ResponseEntity.ok(chartDTOS);
    }

    @GetMapping("/api/admin/product-order")
    public ResponseEntity<Object> getProductOrder(){
        Pageable pageable = PageRequest.of(0,10);
        Date date = new Date();
        List<ChartDTO> chartDTOS = productRepository.getProductOrders(pageable, date.getMonth() +1, date.getYear() + 1900);
        return ResponseEntity.ok(chartDTOS);
    }
}