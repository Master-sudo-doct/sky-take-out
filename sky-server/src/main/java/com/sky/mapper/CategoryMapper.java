package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    @AutoFill(value = OperationType.UPDATE)
    void updateCatalog(Category category);

    Page<Category> PageSelectCatelog(CategoryPageQueryDTO categoryPageQueryDTO);
    @AutoFill(value = OperationType.INSERT)
    void insertCategory(Category category);


    void deleteCatelog(Long id);
}
