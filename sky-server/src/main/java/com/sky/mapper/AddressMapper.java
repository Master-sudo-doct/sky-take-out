package com.sky.mapper;

import com.sky.entity.AddressBook;
import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AddressMapper {

    void insert(AddressBook addressBook);

    List<AddressBook> select(AddressBook addressBook);

    void update(AddressBook book);
    @Delete("delete from address_book where id = #{id}")
    void delete(Long id);
}
