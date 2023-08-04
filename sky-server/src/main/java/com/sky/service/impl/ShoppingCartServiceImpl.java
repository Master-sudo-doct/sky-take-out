package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.entity.ShoppingCart;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.service.ShoppingCartService;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    public static final int INDEX = 0;
    @Autowired
    DishMapper dishMapper;
    @Autowired
    SetmealMapper setmealMapper;
    @Autowired
    ShoppingCartMapper shoppingCartMapper;

    @Override
    public void insert(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = null;
        //添加的为菜品
        if (shoppingCartDTO.getDishId() != null) {
            DishVO dishVO = new DishVO();
            dishVO.setId(shoppingCartDTO.getDishId());
            DishVO dishvo = dishMapper.select(dishVO).get(0);
            shoppingCart =
                    ShoppingCart.builder().amount(dishvo.getPrice()).createTime(LocalDateTime.now()).dishFlavor(shoppingCartDTO.getDishFlavor()).dishId(shoppingCartDTO.getDishId()).userId(BaseContext.getCurrentId()).name(dishvo.getName()).image(dishvo.getImage()).number(1).build();
        }
        //添加的为套餐
        if (shoppingCartDTO.getSetmealId() != null) {
            SetmealVO setmealVO = setmealMapper.selectById(Math.toIntExact(shoppingCartDTO.getSetmealId()));
            shoppingCart =
                    ShoppingCart.builder().number(1).image(setmealVO.getImage()).name(setmealVO.getName()).amount(setmealVO.getPrice()).userId(BaseContext.getCurrentId()).createTime(LocalDateTime.now()).setmealId(shoppingCartDTO.getSetmealId()).dishFlavor(shoppingCartDTO.getDishFlavor()).build();
        }
        shoppingCartMapper.insert(shoppingCart);
    }

    @Override
    public List<ShoppingCart> select(ShoppingCart shoppingCart) {
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.select(shoppingCart);
        return shoppingCartList;
    }

    @Override
    public void updateNum(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart sc = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, sc);
        ShoppingCart shoppingCart = shoppingCartMapper.select(sc).get(0);
        Integer number = shoppingCart.getNumber();
        number += 1;
        shoppingCart.setNumber(number);
        shoppingCartMapper.update(shoppingCart);
    }

    @Override
    public void deleteByid(ShoppingCart shoppingCart) {
        shoppingCartMapper.deleteByid(shoppingCart);
    }

    @Override
    public void deleteNumber(ShoppingCart shoppingCart) {
        Integer number = shoppingCart.getNumber();
        number--;
        shoppingCart.setNumber(number);
        shoppingCartMapper.update(shoppingCart);
    }
}
