package com.model1.application.controller.admin;

import com.model1.application.entity.Category;
import com.model1.application.model.mapper.CategoryMapper;
import com.model1.application.model.request.CreateCategoryRequest;
import com.model1.application.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/admin/categories")
    public String homePage(Model model,
                           @RequestParam(defaultValue = "",required = false) String id,
                           @RequestParam(defaultValue = "",required = false) String name,
                           @RequestParam(defaultValue = "",required = false) String status,
                           @RequestParam(defaultValue = "1",required = false) Integer page){

        Page<Category> categories = categoryService.adminGetListCategory(id,name,status,page);

        Map<Long, String> categoryIdNameMap = categories.getContent()
                .stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));

        model.addAttribute("categories",categories.getContent());
        model.addAttribute("categoryIdNameMap", categoryIdNameMap);
        model.addAttribute("totalPages",categories.getTotalPages());
        model.addAttribute("currentPage", categories.getPageable().getPageNumber() + 1);

        return "admin/category/list";
    }


    @GetMapping("/api/admin/categories")
    public ResponseEntity<Object> adminGetListCategories(@RequestParam(defaultValue = "",required = false) String id,
                                                         @RequestParam(defaultValue = "",required = false) String name,
                                                         @RequestParam(defaultValue = "",required = false) String status,
                                                         @RequestParam(defaultValue = "0",required = false) Integer page){
        Page<Category> categories = categoryService.adminGetListCategory(id,name,status,page);
        return ResponseEntity.ok(categories);

    }
    @GetMapping("/api/admin/categories/{id}")
    public ResponseEntity<Object> getCategoryById(@PathVariable long id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(CategoryMapper.toCategoryDTO(category));
    }

    @PostMapping("/api/admin/categories")
    public ResponseEntity<Object> createCategory(@Valid @RequestBody CreateCategoryRequest createCategoryRequest) {
        Category category = categoryService.createCategory(createCategoryRequest);
        return ResponseEntity.ok(CategoryMapper.toCategoryDTO(category));
    }

    @PutMapping("/api/admin/categories/{id}")
    public ResponseEntity<Object> updateCategory(@Valid @RequestBody CreateCategoryRequest createCategoryRequest, @PathVariable long id) {
        categoryService.updateCategory(createCategoryRequest, id);
        return ResponseEntity.ok("Sửa danh mục thành công!");
    }

    @DeleteMapping("/api/admin/categories/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Xóa danh mục thành công!");
    }

    @PutMapping("/api/admin/categories")
    public ResponseEntity<Object> updateOrderCategory(@RequestBody int[] ids){
        categoryService.updateOrderCategory(ids);
        return ResponseEntity.ok("Thay đổi thứ tự thành công!");
    }
}
