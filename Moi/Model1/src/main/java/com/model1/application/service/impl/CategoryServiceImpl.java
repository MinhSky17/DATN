package com.model1.application.service.impl;

import com.github.slugify.Slugify;
import com.model1.application.entity.Category;
import com.model1.application.exception.BadRequestException;
import com.model1.application.exception.InternalServerException;
import com.model1.application.exception.NotFoundException;
import com.model1.application.model.mapper.CategoryMapper;
import com.model1.application.model.request.CreateCategoryRequest;
import com.model1.application.repository.CategoryRepository;
import com.model1.application.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.*;

import static com.model1.application.config.Contant.LIMIT_CATEGORY;

@Component
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Page<Category> adminGetListCategory(String id, String name, String status, int page) {
        page--;
        if (page <= 0) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, LIMIT_CATEGORY, Sort.by("created_at").descending());
        return categoryRepository.adminGetListCategory(id, name, status, pageable);
    }

    @Override
    public void updateOrderCategory(int[] ids) {

        for (int id: ids){
            Optional<Category> category = categoryRepository.findById((long) id);
            category.get().setOrder(0);
            categoryRepository.save(category.get());
        }
    }

    @Override
    public long getCountCategories() {
        return categoryRepository.count();
    }

    @Override
    public List<Category> getListCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            throw new NotFoundException("Danh mục không tồn tại!");
        }
        return category.get();
    }

//    @Override
//    public Category createCategory(CreateCategoryRequest createCategoryRequest) {
//        Category category = categoryRepository.findByName(createCategoryRequest.getName());
//        if (category != null) {
//            throw new BadRequestException("Tên danh mục đã tồn tại trong hệ thống. Vui lòng chọn tên khác!");
//        }
//        category = CategoryMapper.toCategory(createCategoryRequest);
//        categoryRepository.save(category);
//        return category;
//    }

    @Override
    public Category createCategory(CreateCategoryRequest createCategoryRequest) {
        String categoryName = createCategoryRequest.getName().trim();

        // Kiểm tra xem có danh mục nào cùng tên (phân biệt dấu)
        List<Category> allCategories = categoryRepository.findAll();
        for (Category cat : allCategories) {
            if (cat.getName().equals(categoryName)) {
                throw new BadRequestException("Tên danh mục đã tồn tại trong hệ thống. Vui lòng chọn tên khác!");
            }
        }

        Category category = CategoryMapper.toCategory(createCategoryRequest);
        categoryRepository.save(category);
        return category;
    }

    @Override
    public void updateCategory(CreateCategoryRequest createCategoryRequest, long id) {
        Optional<Category> result = categoryRepository.findById(id);
        if (result.isEmpty()) {
            throw new NotFoundException("Danh mục không tồn tại!");
        }
        Category category = result.get();
        category.setName(createCategoryRequest.getName());
        Slugify slugify = new Slugify();
        // Tùy chỉnh quy tắc để xử lý tiếng Việt
        slugify.withCustomReplacement("đ", "d"); // Đảm bảo "đ" được thay bằng "d" đúng
        slugify.withCustomReplacement("Đ", "d"); // Xử lý chữ hoa
        slugify.withLowerCase(true); // Chuyển về chữ thường
        slugify.withTransliterator(true); // Chuyển đổi ký tự có dấu sang không dấu
        String slug = slugify.slugify(createCategoryRequest.getName());
        category.setSlug(slug);
        category.setStatus(createCategoryRequest.isStatus());
        category.setParentId(createCategoryRequest.getParentId());
        category.setModifiedAt(new Timestamp(System.currentTimeMillis()));
        try {
            categoryRepository.save(category);
        } catch (Exception e) {
            throw new InternalServerException("Lỗi khi chỉnh sửa danh mục");
        }
    }

    @Override
    public void deleteCategory(long id) {
        Optional<Category> result = categoryRepository.findById(id);
        if (result.isEmpty()) {
            throw new NotFoundException("Danh mục không tồn tại!");
        }

        //Check product in category
        long count = categoryRepository.checkProductInCategory(id);
        if (count > 0) {
            throw new BadRequestException("Có sản phẩm thuộc danh mục không thể xóa!");
        }

        try {
            categoryRepository.deleteById(id);
        } catch (Exception ex) {
            throw new InternalServerException("Lỗi khi xóa danh mục!");
        }
    }


    @Override
    public List<Category> getRootCategories() {
        //return categoryRepository.findByParentIdEqualsZero();
        return categoryRepository.findByParentId(0L);
    }

}
