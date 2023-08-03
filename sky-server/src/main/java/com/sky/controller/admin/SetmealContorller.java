package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
@Api(tags = "套餐相关接口")
public class SetmealContorller {
    @Autowired
    SetmealService setmealService;

    @PostMapping
    @ApiOperation("新增套餐")
    @CacheEvict(cacheNames = "setmealCache",key = "#setmealDTO.categoryId")
    public Result add(@RequestBody SetmealDTO setmealDTO) {
        log.info("套餐信息为{}", setmealDTO);
        setmealService.insert(setmealDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("套餐分页查询参数", setmealPageQueryDTO);
        PageResult result = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(result);
    }
    @GetMapping("/{id}")
    @ApiOperation("根据id查询套餐")
    public Result<SetmealVO> selectById(@PathVariable Integer id){
       SetmealVO setmealVO = setmealService.selectById(id);
       return Result.success(setmealVO);
    }
    @PostMapping("/status/{status}")
    @ApiOperation("套餐起售、停售")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result startOrStop(@PathVariable Integer status, Integer id){
        setmealService.startOrStop(status,id);
        return Result.success();
    }
    @PutMapping
    @ApiOperation("修改套餐")
    public Result update(@RequestBody SetmealDTO setmealDTO){
        setmealService.update(setmealDTO);
        return Result.success();
    }
    @DeleteMapping
    @ApiOperation("批量删除套餐")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)//清除setmealCache下所有缓存数据
    public Result delete(Integer[] ids){
        setmealService.delete(ids);
        return Result.success();
    }

}
