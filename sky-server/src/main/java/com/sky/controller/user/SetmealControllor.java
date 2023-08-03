package com.sky.controller.user;

import com.sky.entity.SetmealDish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.service.SetmealService;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/user/setmeal")
@RestController("userSetmealControllor")
@Api(tags = "用户套餐相关接口")
@Slf4j
public class SetmealControllor {
    @Autowired
    SetmealService setmealService;
    @GetMapping("/list")
    @ApiOperation("根据id查询套餐分类下的套餐")
    @Cacheable(cacheNames = "setmealCache",key = "#categoryId")
    public Result<List<SetmealVO>> selectById(Integer categoryId){
        List<SetmealVO> setmealVO = setmealService.selectByCategoryId(categoryId);
        return Result.success(setmealVO);
    }

    @GetMapping("/dish/{id}")
    @ApiOperation("根据套餐id查询包含的菜品")
    public Result<List<SetmealDish>> selectBySetmealId(@PathVariable Integer id){
        List<SetmealDish> setmealDishList = setmealService.selectBySetmealId(id);
        return Result.success(setmealDishList);
    }
    
}
