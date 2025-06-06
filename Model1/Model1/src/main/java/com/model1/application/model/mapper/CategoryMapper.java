package com.model1.application.model.mapper;

import com.github.slugify.Slugify;
import com.model1.application.entity.Category;
import com.model1.application.model.dto.CategoryDTO;
import com.model1.application.model.request.CreateCategoryRequest;

import java.sql.Timestamp;

public class CategoryMapper {
    public static CategoryDTO toCategoryDTO(Category category){
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setOrder(category.getOrder());
        categoryDTO.setStatus(category.isStatus());
        categoryDTO.setCreatedAt(category.getCreatedAt());
        categoryDTO.setParentId(category.getParentId());
        return categoryDTO;
    }

    public static Category toCategory(CreateCategoryRequest createCategoryRequest){
        Category category = new Category();
        category.setName(createCategoryRequest.getName());
        category.setOrder(0);
        category.setStatus(createCategoryRequest.isStatus());
        category.setCreatedAt(new Timestamp(System.currentTimeMillis()));
//        Slugify slug = new Slugify();
//        category.setSlug(slug.slugify(createCategoryRequest.getName()));
        Slugify slugify = new Slugify();
        // Tùy chỉnh quy tắc để xử lý tiếng Việt
        slugify.withCustomReplacement("đ", "d"); // Đảm bảo "đ" được thay bằng "d" đúng
        slugify.withCustomReplacement("Đ", "d"); // Xử lý chữ hoa
        slugify.withLowerCase(true); // Chuyển về chữ thường
        slugify.withTransliterator(true); // Chuyển đổi ký tự có dấu sang không dấu

        String slug = slugify.slugify(createCategoryRequest.getName());
        category.setSlug(slug);
        category.setParentId(createCategoryRequest.getParentId());

        return category;
    }
}
