package com.sky.service;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.enumeration.OperationType;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    void addDish(DishDTO dishDTO);

    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    DishVO selectbyId(Integer id);

    List<DishVO> selectbyCateId(Long categoryId);

    boolean update(DishDTO dishDTO);
    boolean updateDish(Integer status,Integer id);

    boolean delete(Integer[] ids);
}
