package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface AddressService {


    void insert(AddressBook addressBook);

    List<AddressBook> select(AddressBook addressBook);

    void update(AddressBook book);

    void delete(Long id);
}
