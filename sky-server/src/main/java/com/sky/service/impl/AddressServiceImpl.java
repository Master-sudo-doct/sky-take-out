package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.AddressMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.AddressService;
import com.sky.service.ShoppingCartService;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressMapper addressMapper;
    @Override
    public void insert(AddressBook addressBook) {
        addressMapper.insert(addressBook);
    }

    @Override
    public List<AddressBook> select(AddressBook addressBook) {

        List<AddressBook> addressBookList = addressMapper.select(addressBook);
        return addressBookList;
    }

    @Override
    public void update(AddressBook book) {
        addressMapper.update(book);
    }

    @Override
    public void delete(Long id) {
        addressMapper.delete(id);
    }
}
