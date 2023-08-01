package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {


    void batchInsert(List<DishFlavor> flavors);

    void batchUpdate(List<DishFlavor> flavors);

    void batchDelete(Long ids);

    void Update(DishFlavor flavor);

    List<DishFlavor> selectById(Long dishId);
}
