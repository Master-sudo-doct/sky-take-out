package com.sky.controller.admin;

import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.sky.dto.CategoryDTO;
import com.sky.result.PageResult;

import java.util.List;

/**
 * ]
 * 分类管理
 */
@RestController
@Slf4j
@Api("分类管理")
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PutMapping
    @ApiOperation("更新分类")
    public Result updateCatelog(@RequestBody CategoryDTO categoryDTO) {
        log.info("更新后的分类为{}", categoryDTO.toString());
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        categoryService.updateCatalog(category);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("分页查询分类")
    public Result selectCatelog(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("查询的分类为{}", categoryPageQueryDTO.toString());
        PageResult result = categoryService.pageSelectCatelog(categoryPageQueryDTO);
        return Result.success(result);
    }

    @PostMapping
    @ApiOperation("新增分类")
    public Result createCategory(@RequestBody CategoryDTO categoryDTO) {
        log.info("创建的分类为{}", categoryDTO.toString());
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        categoryService.createCategory(category);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用分类")
    public Result startOrstop(@PathVariable Integer status, Long id) {
        log.info("id为{}的分类，当前状态修改为{}", id, status == 1 ? "启用" : "禁用");
        Category category = new Category();
        category.setId(id);
        category.setStatus(status);
        categoryService.updateCatalog(category);
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("根据类型查询分类")
    public Result selectByType(Integer type){
        log.info("查询的类别为{}", type);
        CategoryPageQueryDTO categoryPageQueryDTO = new CategoryPageQueryDTO();
        categoryPageQueryDTO.setType(type);
        List<Category> record = categoryService.selectCatelog(categoryPageQueryDTO);
        return Result.success(record);
    }

    @DeleteMapping
    @ApiOperation("根据id删除分类")
    public Result deleteById(Long id){
        log.info("删除id{}对应类别", id);
        categoryService.deleteCatelog(id);
        return Result.success();
    }
}
