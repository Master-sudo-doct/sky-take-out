package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/user/addressBook")
@Slf4j
@RestController
@Api(tags = "地址簿接口")
public class AddressControllor {
    @Autowired
    private AddressService addressService;

    @PostMapping
    @ApiOperation("新增地址")
    public Result add(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(0);
        addressService.insert(addressBook);
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("查询当前登录用户的所有地址信息")
    public Result<List<AddressBook>> list() {
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(BaseContext.getCurrentId());
        List<AddressBook> addressBookList = addressService.select(addressBook);
        return Result.success(addressBookList);
    }

    @GetMapping("/default")
    @ApiOperation("查询默认地址")
    public Result<AddressBook> getDefault() {
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(1);//获取默认地址
        List<AddressBook> addressBookList = addressService.select(addressBook);
        if (addressBookList != null && addressBookList.size() > 0) {
            return Result.success(addressBookList.get(0));
        }
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询地址")
    public Result<AddressBook> getAddressByid(@PathVariable Long id) {
        AddressBook addressBook = new AddressBook();
        addressBook.setId(id);
        addressBook = addressService.select(addressBook).get(0);
        return Result.success(addressBook);
    }

    @PutMapping("/default")
    @ApiOperation("设置默认地址")
    public Result setDefault(@RequestBody AddressBook addressBook) {
        //查找是否存在默认，若存在则修改
        AddressBook ad = new AddressBook();
        ad.setIsDefault(1);
        ad.setUserId(BaseContext.getCurrentId());
        List<AddressBook> addressBooks = addressService.select(ad);
        if (addressBooks != null && addressBooks.size() > 0) {
            for (AddressBook book : addressBooks) {
                book.setIsDefault(0);
                addressService.update(book);
            }
        }
        addressBook.setIsDefault(1);//设置为默认
        addressService.update(addressBook);
        return Result.success();
    }

    @PutMapping
    @ApiOperation("根据id修改地址")
    public Result update(@RequestBody AddressBook addressBook) {
        addressService.update(addressBook);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("根据id删除地址")
    public Result delete(Long id) {
        addressService.delete(id);
        return Result.success();
    }
}
