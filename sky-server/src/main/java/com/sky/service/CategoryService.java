package com.sky.service;

import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {

    void updateCatalog(Category category);

    PageResult pageSelectCatelog(CategoryPageQueryDTO categoryPageQueryDTO);

    void createCategory(Category category);

    List<Category> selectCatelog(CategoryPageQueryDTO categoryPageQueryDTO);

    void deleteCatelog(Long id);

    List<Category> userSelectCatelog(Category category);
}
