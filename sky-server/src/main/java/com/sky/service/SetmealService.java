package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.SetmealDish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    void insert(SetmealDTO setmealDTO);

    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    SetmealVO selectById(Integer id);

    void startOrStop(Integer status, Integer id);

    void update(SetmealDTO setmealDTO);

    void delete(Integer[] ids);

    List<SetmealVO> selectByCategoryId(Integer categoryId);

    List<SetmealDish> selectBySetmealId(Integer id);
}
