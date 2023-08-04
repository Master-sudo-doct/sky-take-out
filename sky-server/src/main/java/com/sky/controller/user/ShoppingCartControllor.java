package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/user/shoppingCart")
@Api("购物车接口")
public class ShoppingCartControllor {
    @Autowired
    ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    @ApiOperation("添加购物车")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        //判断购物车中是否有该商品，若没有则插入，若有则number++
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(BaseContext.getCurrentId());
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        List<ShoppingCart> shoppingCartList = shoppingCartService.select(shoppingCart);
        if (shoppingCartList != null && shoppingCartList.size() > 0) {
            shoppingCartService.updateNum(shoppingCartDTO);
        } else {
            shoppingCartService.insert(shoppingCartDTO);
        }
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("查看购物车")
    public Result<List<ShoppingCart>> list() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> select = shoppingCartService.select(shoppingCart);
        return Result.success(select);
    }

    @PostMapping("/sub")
    @ApiOperation("删除购物车中一个商品")
    public Result deleteByid(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(BaseContext.getCurrentId());
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart = shoppingCartService.select(shoppingCart).get(0);
        //若只有一份则直接删除
        if (shoppingCart.getNumber() == 1) {
            shoppingCartService.deleteByid(shoppingCart);
        }else{
            //否则将number--
            shoppingCartService.deleteNumber(shoppingCart);
        }

        return Result.success();
    }

    @DeleteMapping("/clean")
    @Transactional
    @ApiOperation("清空购物车")
    public Result clean() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> select = shoppingCartService.select(shoppingCart);
        for (ShoppingCart cart : select) {
            shoppingCartService.deleteByid(cart);
        }
        return Result.success();
    }
}
