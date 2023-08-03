package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.Category;
import com.sky.entity.User;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/user/category")
@RestController("userCategoryControllor")
@Api(tags = "用户分类相关接口")
@Slf4j
public class CategoryControllor {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/list")
    @ApiOperation("根据类型查询分类")
    public Result selectByType(Integer type){
        log.info("查询的类别为{}", type);
        Category category = new Category();
        category.setType(type);
        category.setStatus(StatusConstant.ENABLE);
        List<Category> record = categoryService.userSelectCatelog(category);
        return Result.success(record);
    }

}
