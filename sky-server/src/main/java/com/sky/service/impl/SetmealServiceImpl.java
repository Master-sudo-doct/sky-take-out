package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CommonService;
import com.sky.service.SetmealService;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    SetmealMapper setmealMapper;
    @Autowired
    SetmealDishMapper setmealDishMapper;

    @Autowired
    DishMapper dishMapper;

    @Transactional
    public void insert(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        //将setmeal插入，并返回套餐的id
        setmealMapper.insert(setmeal);
        Long setmealId = setmeal.getId();//获取套餐id
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes != null && setmealDishes.size() > 0) {
            setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealId));
            setmealDishMapper.insertBatch(setmealDishes);
        }
    }

    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> record = setmealMapper.select(setmealPageQueryDTO);
        return new PageResult(record.getTotal(), record.getResult());
    }

    @Override
    public SetmealVO selectById(Integer id) {
        SetmealVO setmealVO = setmealMapper.selectById(id);
        List<SetmealDish> setmealDishList = setmealDishMapper.selectBySetmealId(id);
        setmealVO.setSetmealDishes(setmealDishList);
        return setmealVO;
    }

    @Override
    public void startOrStop(Integer status, Integer id) {
        Setmeal setmeal = new Setmeal();
        setmeal.setStatus(status);
        setmeal.setId(Long.valueOf(id));
        //判断套餐中是否存在未启售商品
        if (status.equals(StatusConstant.ENABLE)){
            List<SetmealDish> setmealDishList = setmealDishMapper.selectBySetmealId(id);
            for (SetmealDish setmealDish : setmealDishList) {
                Long dishId = setmealDish.getDishId();
                DishVO dishVO = new DishVO();
                dishVO.setId(dishId);
                List<DishVO> dishVOList = dishMapper.select(dishVO);
                for (DishVO dishvo : dishVOList) {
                    if (Objects.equals(dishvo.getStatus(), StatusConstant.DISABLE)) {
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                }
            }
        }
        setmealMapper.update(setmeal);
    }

    @Transactional
    public void update(SetmealDTO setmealDTO) {
        //更改setmeal表数据
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.update(setmeal);
        //更改对应setmeal_dish数据
        Long setmealId = setmeal.getId();
        //删除对应菜品
        setmealDishMapper.deleteBatch(setmealId);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealId));
        setmealDishMapper.insertBatch(setmealDishes);
    }

    @Transactional
    public void delete(Integer[] ids) {
        for (Integer id : ids) {
            //查询套餐是否起售中，若起售中，则不能删除
            SetmealVO setmealVO = setmealMapper.selectById(id);
            if (setmealVO.getStatus().equals(StatusConstant.ENABLE)) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
            setmealMapper.delete(id);
            setmealDishMapper.deleteBatch(Long.valueOf(id));
        }
    }
}
