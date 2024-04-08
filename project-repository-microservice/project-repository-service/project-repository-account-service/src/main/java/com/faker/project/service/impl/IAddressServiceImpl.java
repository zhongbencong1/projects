package com.faker.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.faker.project.account.AddressInfo;
import com.faker.project.common.TableId;
import com.faker.project.entity.ProjectRepositoryAddress;
import com.faker.project.filter.AccessContext;
import com.faker.project.mapper.ProjectAddressMapper;
import com.faker.project.service.IAddressService;
import com.faker.project.vo.LoginUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户地址相关服务接口实现
 * */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class IAddressServiceImpl extends ServiceImpl<ProjectAddressMapper, ProjectRepositoryAddress> implements IAddressService {

    @Autowired
    private ProjectAddressMapper projectAddressMapper;

    @Override
    public TableId createAddressInfo(AddressInfo addressInfo) {
        // 不能直接从参数中获取用户的 id 信息
        LoginUserInfo loginUserInfo = AccessContext.getLoginUserInfo();
        // 将传递的参数转换成实体对象
        List<ProjectRepositoryAddress> ecommerceAddresses = addressInfo.getAddressItems().stream()
                .map(a -> ProjectRepositoryAddress.to(loginUserInfo.getId(), a))
                .collect(Collectors.toList());
        // 保存到数据表并把返回记录的 id 给调用方
        boolean saveResult = this.saveBatch(ecommerceAddresses);
        TableId result = null;
        if (saveResult) {
            List<TableId.Id> collect = ecommerceAddresses.stream()
                    .map(ProjectRepositoryAddress::getId).map(TableId.Id::new)
                    .collect(Collectors.toList());
            result = new TableId(collect);
        }
        log.info("create address info: [{}], [{}]", loginUserInfo.getId(), JSON.toJSONString(result != null ? result.getIds() : null));
        return result;
    }

    @Override
    public AddressInfo getCurrentAddressInfo() {
        LoginUserInfo loginUserInfo = AccessContext.getLoginUserInfo();

        // 根据 userId 查询到用户的地址信息, 再实现转换
        LambdaQueryWrapper<ProjectRepositoryAddress> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProjectRepositoryAddress::getId, loginUserInfo.getId());
        List<ProjectRepositoryAddress> projectRepositoryAddresses = projectAddressMapper.selectList(queryWrapper);
        // 转换
        List<AddressInfo.AddressItem> addressItems = projectRepositoryAddresses.stream()
                .map(ProjectRepositoryAddress::toAddressItem)
                .collect(Collectors.toList());
        return new AddressInfo(loginUserInfo.getId(), addressItems);
    }

    @Override
    public AddressInfo getAddressInfoById(Long id) {
        ProjectRepositoryAddress projectRepositoryAddress = projectAddressMapper.selectById(id);
        if (null == projectRepositoryAddress) {
            throw new RuntimeException("address is not exist");
        }
        return new AddressInfo(projectRepositoryAddress.getUserId(),
                Collections.singletonList(projectRepositoryAddress.toAddressItem())
        );
    }

    @Override
    public AddressInfo getAddressInfoByTableId(TableId tableId) {
        List<Long> ids = tableId.getIds().stream()
                .map(TableId.Id::getId).collect(Collectors.toList());
        log.info("get address info by table id: [{}]", JSON.toJSONString(ids));

        LambdaQueryWrapper<ProjectRepositoryAddress> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ProjectRepositoryAddress::getId, ids);
        List<ProjectRepositoryAddress> ecommerceAddresses = projectAddressMapper.selectList(queryWrapper);

        if (CollectionUtils.isEmpty(ecommerceAddresses)) {
            return new AddressInfo(-1L, Collections.emptyList());
        }

        List<AddressInfo.AddressItem> addressItems = ecommerceAddresses.stream()
                .map(ProjectRepositoryAddress::toAddressItem)
                .collect(Collectors.toList());

        return new AddressInfo(ecommerceAddresses.get(0).getUserId(), addressItems);
    }
}
