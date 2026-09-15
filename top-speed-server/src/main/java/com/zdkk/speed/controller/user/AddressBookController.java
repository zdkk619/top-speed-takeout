package com.zdkk.speed.controller.user;

import com.zdkk.speed.constant.MessageConstant;
import com.zdkk.speed.entity.AddressBook;
import com.zdkk.speed.result.Result;
import com.zdkk.speed.service.AddressBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/user/addressBook")
@RestController
@Slf4j
@Tag(name = "地址簿管理")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;
    @PostMapping
    @Operation(summary = "保存地址")
    public Result<String> save(@RequestBody AddressBook addressBook) {
        addressBookService.save(addressBook);
        return Result.success();
    }

    @GetMapping("/list")
    @Operation(summary = "地址簿列表")
    public Result<List<AddressBook>> list() {
        return Result.success(addressBookService.list());
    }

    @GetMapping("/default")
    @Operation(summary = "默认地址")
    public Result<AddressBook> getDefault() {
        AddressBook addressBook = addressBookService.getDefault();
        if (addressBook == null) {
            return Result.error(MessageConstant.DEFAULT_ADDRESS_NOT_EXISTS);
        }
        return Result.success(addressBook);
    }

    @PutMapping
    @Operation(summary = "修改地址")
    public Result<String> update(@RequestBody AddressBook addressBook) {
        addressBookService.update(addressBook);
        return Result.success();
    }

    @DeleteMapping
    @Operation(summary = "删除地址")
    public Result<String> delete(Long id) {
        log.info("删除地址：{}", id);
        addressBookService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据id查询地址")
    public Result<AddressBook> getById(@PathVariable Long id) {
        return Result.success(addressBookService.getById(id));
    }

    @PutMapping("/default")
    @Operation(summary = "设置默认地址")
    public Result<String> setDefault(@RequestBody AddressBook addressBook) {
        addressBookService.setDefault(addressBook);
        return Result.success();
    }
}
