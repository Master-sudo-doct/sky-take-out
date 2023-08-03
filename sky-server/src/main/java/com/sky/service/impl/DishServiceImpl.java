package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
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
    @Autowired
    SetmealDishMapper setmealDishMapper;

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
    public List<DishVO> selectbyCateId(DishDTO dishDTO) {
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dishDTO,dishVO);
        List<DishVO> result = dishMapper.select(dishVO);
        return result;
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
        //菜品是否在套餐表里存在，若存在，则不能删除
        for (Integer dishId : ids) {
            Integer count = setmealDishMapper.countByDishId(Long.valueOf(dishId));
            if (count > 0) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
            }
            //判断菜品是否处于起售中状态，若处于，则不能删除
            DishVO dish = new DishVO();
            dish.setId(Long.valueOf(dishId));
            List<DishVO> dishVOList = dishMapper.select(dish);
            DishVO dishVO = dishVOList.get(0);
            if (dishVO.getStatus().equals(StatusConstant.ENABLE)) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        for (Integer id : ids) {
            dishMapper.delete(Long.valueOf(id));
            dishFlavorMapper.batchDelete(Long.valueOf(id));
        }
        return true;
    }
}
