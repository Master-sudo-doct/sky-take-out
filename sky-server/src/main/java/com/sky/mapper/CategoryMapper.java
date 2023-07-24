package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    void updateCatalog(Category category);

    Page<Category> PageSelectCatelog(CategoryPageQueryDTO categoryPageQueryDTO);

    void insertCategory(Category category);


    void deleteCatelog(Long id);
}
