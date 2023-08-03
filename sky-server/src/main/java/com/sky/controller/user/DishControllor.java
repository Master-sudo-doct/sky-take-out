package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.service.SetmealService;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/user/dish")
@RestController("userDishControllor")
@Api(tags = "用户菜品相关接口")
@Slf4j
public class DishControllor {
    @Autowired
    DishService dishService;
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> querybycateId(Long categoryId) {
        DishDTO dishDTO = new DishDTO();
        dishDTO.setStatus(StatusConstant.ENABLE);
        dishDTO.setCategoryId(categoryId);
        List<DishVO> dishVO = dishService.selectbyCateId(dishDTO);
        return Result.success(dishVO);
    }

    
}
