package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Category;
import com.sky.entity.ShoppingCart;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    void insert(ShoppingCart shoppingCart);

    List<ShoppingCart> select(ShoppingCart shoppingCart);
    void update(ShoppingCart shoppingCart);

    void deleteByid(ShoppingCart shoppingCart);

}
