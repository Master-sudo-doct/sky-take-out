package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    DishMapper dishMapper;
    @Autowired
    DishFlavorMapper dishFlavorMapper;

    @Transactional
    public void addDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.addDish(dish);
        //获取菜品id
        Long dishId = dish.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(dishId));
            //mysql批量插入
            dishFlavorMapper.batchInsert(flavors);
        }
    }

    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> result = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(result.getTotal(), result.getResult());
    }

    @Override
    public DishVO selectbyId(Integer id) {
        DishVO dishVO = new DishVO();
        dishVO.setId(Long.valueOf(id));
        List<DishVO> result = dishMapper.select(dishVO);
        DishVO dish = null;
        if (result != null && result.size() > 0) {
            dish = result.get(0);
        }
        return dish;
    }

    @Override
    public DishVO selectbyCateId(Long categoryId) {
        DishVO dishVO = new DishVO();
        dishVO.setCategoryId(categoryId);
        List<DishVO> result = dishMapper.select(dishVO);
        DishVO dish = null;
        if (result != null && result.size() > 0) {
            dish = result.get(0);
        }
        return dish;
    }

    @Transactional
    public boolean update(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        List<DishFlavor> flavors = dishDTO.getFlavors();
        dishMapper.update(dish);
        Long dishId = dishDTO.getId();
        //删除原来的口味列表
        dishFlavorMapper.batchDelete(dishId);
        //插入新增的口味列表
        if (flavors != null && flavors.size() != 0) {
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(dishId));
            dishFlavorMapper.batchInsert(flavors);
        }
        return true;
    }

    @Override
    public boolean updateDish(Integer status, Integer id) {
        Dish dish = new Dish();
        dish.setStatus(status);
        dish.setId(Long.valueOf(id));
        dishMapper.update(dish);
        return true;
    }

    @Transactional
    public boolean delete(Integer[] ids) {
        for (Integer id : ids) {
            dishMapper.delete(Long.valueOf(id));
            dishFlavorMapper.batchDelete(Long.valueOf(id));
        }
        return true;
    }
}
