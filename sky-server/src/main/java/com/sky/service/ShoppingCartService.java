package com.sky.service;

import com.sky.dto.CategoryPageQueryDTO;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Category;
import com.sky.entity.ShoppingCart;
import com.sky.result.PageResult;

import java.util.List;

public interface ShoppingCartService {


    void insert(ShoppingCartDTO shoppingCartDTO);

    List<ShoppingCart> select(ShoppingCart shoppingCart);

    void updateNum(ShoppingCartDTO shoppingCartDTO);

    void deleteByid(ShoppingCart shoppingCart);

    void deleteNumber(ShoppingCart shoppingCart);
}
