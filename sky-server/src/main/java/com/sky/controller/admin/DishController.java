package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
public class DishController {
    @Autowired
    DishService dishService;

    @PostMapping
    @ApiOperation("新增菜品")
    public Result addDish(@RequestBody DishDTO dishDTO) {
        log.info("dishDTO", dishDTO);
        dishService.addDish(dishDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("dishPageQueryDTO", dishPageQueryDTO);
        PageResult result = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(result);
    }

    @GetMapping("{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> querybyId(@PathVariable Integer id) {
        DishVO dishVO = dishService.selectbyId(id);
        return Result.success(dishVO);
    }

    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> querybycateId(Long categoryId) {
        DishDTO dishDTO = new DishDTO();
        dishDTO.setCategoryId(categoryId);
        List<DishVO> dishVO = dishService.selectbyCateId(dishDTO);
        return Result.success(dishVO);
    }
    @PutMapping
    @ApiOperation("修改菜品")
    public Result update(@RequestBody DishDTO dishDTO) {
        boolean ok = dishService.update(dishDTO);
        return ok == true ? Result.success() : Result.error("更新失败");
    }

    @PostMapping("/status/{status}")
    @ApiOperation("菜品起售停售")
    public Result startOrStop(@PathVariable Integer status,@RequestParam Integer id){
        log.info("status:{},id:{}",status,id);
        boolean ok = dishService.updateDish(status,id);
        return ok == true ? Result.success() : Result.error("更新失败");
    }

    @DeleteMapping
    @ApiOperation("批量删除菜品")
    public Result delete(Integer[] ids){
        boolean ok = dishService.delete(ids);
        return ok == true ? Result.success() : Result.error("删除失败");
    }

}
