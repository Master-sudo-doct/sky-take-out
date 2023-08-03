package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.User;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import com.sky.vo.DishVO;
import com.sky.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    //微信服务接口地址
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";
    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(UserLoginDTO userLoginDTO) {
        //向wx服务端发送请求获取openid，若获取不到，说明用户信息有误
        String openid = getOpenid(userLoginDTO.getCode());
        //获取openid，在user表查询，若存在则返回token，若不存在则对用户进行注册，然后返回token
        if (openid == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        User user =  userMapper.selectByOpenId(openid);
        if (user==null){
            user = new User();
            BeanUtils.copyProperties(userLoginDTO, user);
            user.setOpenid(openid);
            user.setCreateTime(LocalDateTime.now());
            userMapper.insert(user);
        }
        return user;
    }


    /**
     * 调用微信接口服务，获取微信用户的openid
     *
     * @param code
     * @return
     */
    private String getOpenid(String code) {
        //调用微信接口服务，获得当前微信用户的openid
        Map<String, String> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN, map);

        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");
        return openid;
    }
}
