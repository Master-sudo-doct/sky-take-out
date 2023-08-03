package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    DishMapper dishMapper;
    @Autowired
    SetmealMapper setmealMapper;

    @Override
    public void updateCatalog(Category category) {
//        category.setUpdateTime(LocalDateTime.now());
//        category.setUpdateUser(BaseContext.getCurrentId());
        categoryMapper.updateCatalog(category);
    }

    @Override
    public PageResult pageSelectCatelog(CategoryPageQueryDTO categoryPageQueryDTO) {
        int pageSize = categoryPageQueryDTO.getPageSize();
        int page = categoryPageQueryDTO.getPage();
        //开始分页
        PageHelper.startPage(page, pageSize);
        Page<Category> record = categoryMapper.PageSelectCatelog(categoryPageQueryDTO);
        long total = record.getTotal();
        List<Category> result = record.getResult();
        PageResult pageResult = new PageResult(total, result);
        return pageResult;
    }

    @Override
    public void createCategory(Category category) {
//        category.setCreateTime(LocalDateTime.now());
//        category.setCreateUser(BaseContext.getCurrentId());
        categoryMapper.insertCategory(category);
    }

    @Override
    public List<Category> selectCatelog(CategoryPageQueryDTO categoryPageQueryDTO) {
        List<Category> result = categoryMapper.PageSelectCatelog(categoryPageQueryDTO);
        return result;
    }

    @Override
    public void deleteCatelog(Long id) {
        Integer dish_count = dishMapper.countByCategoryId(id);
        Integer setmeal_count = setmealMapper.countByCategoryId(id);
        if (dish_count == 0 && setmeal_count == 0) {
            categoryMapper.deleteCatelog(id);
        } else {
            if (dish_count > 0) {
                //当前分类下有菜品，不能删除
                throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
            }
            if (setmeal_count > 0) {
                //当前分类下有菜品，不能删除
                throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
            }
        }

    }

    @Override
    public List<Category> userSelectCatelog(Category category) {
        List<Category> result = categoryMapper.userSelectByStatus(category);
        return result;
    }
}
