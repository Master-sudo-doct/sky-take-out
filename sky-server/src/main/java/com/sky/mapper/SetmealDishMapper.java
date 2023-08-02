package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    @Select("select COUNT(id) from setmeal_dish where dish_id=#{dishId}")
    Integer countByDishId(Long id);


    void insertBatch(List<SetmealDish> setmealDishes);
    @Select("select * from setmeal_dish where setmeal_id=#{id}")
    List<SetmealDish> selectBySetmealId(Integer id);

    void deleteBatch(Long setmealId);
}
