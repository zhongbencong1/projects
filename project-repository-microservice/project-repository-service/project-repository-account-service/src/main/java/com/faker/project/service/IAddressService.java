package com.faker.project.service;

import com.faker.project.account.AddressInfo;
import com.faker.project.common.TableId;

/**
 * 用户地址相关服务接口定义
 * */
public interface IAddressService {
    /** 创建用户地址信息 */
    TableId createAddressInfo(AddressInfo addressInfo);

    /** 获取当前登录的用户地址信息 (threadlocal中获取用户信息) */
    AddressInfo getCurrentAddressInfo();

    /** 通过 id 获取用户地址信息, id 是 ProjectRepositoryAddress 表的主键 */
    AddressInfo getAddressInfoById(Long id);

    /** 通过 TableId 获取用户地址信息 */
    AddressInfo getAddressInfoByTableId(TableId tableId);
}
