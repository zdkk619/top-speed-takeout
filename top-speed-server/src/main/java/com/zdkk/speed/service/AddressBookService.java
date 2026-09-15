package com.zdkk.speed.service;

import com.zdkk.speed.entity.AddressBook;

import java.util.List;

public interface AddressBookService {
    /**
     * 保存地址簿
     * @param addressBook
     */
    void save(AddressBook addressBook);

    /**
     * 列出所有地址簿
     * @return
     */
    List<AddressBook> list();


    /**
     * 获取默认地址簿
     * @return
     */
    AddressBook getDefault();

    /**
     * 更新地址簿
     * @param addressBook
     */
    void update(AddressBook addressBook);

    /**
     * 删除地址簿
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据id获取地址簿
     * @param id
     * @return
     */
    AddressBook getById(Long id);

    /**
     * 设置默认地址簿
     * @param addressBook
     */
    void setDefault(AddressBook addressBook);
}
