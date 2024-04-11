package com.faker.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.faker.project.common.TableId;
import com.faker.project.entity.RepositoryGoods;
import com.faker.project.goods.DeductGoodsInventory;
import com.faker.project.goods.GoodsInfo;
import com.faker.project.goods.SimpleGoodsInfo;
import com.faker.project.mapper.GoodsServiceMapper;
import com.faker.project.service.IGoodsService;
import com.faker.project.vo.PageSimpleGoodsInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class IGoodsServiceImpl extends ServiceImpl<GoodsServiceMapper, RepositoryGoods> implements IGoodsService {
    @Override
    public List<GoodsInfo> getGoodsInfoByTableId(TableId tableId) {
        return null;
    }

    @Override
    public PageSimpleGoodsInfo getSimpleGoodsInfoByPage(int page) {
        return null;
    }

    @Override
    public List<SimpleGoodsInfo> getSimpleGoodsInfoByTableId(TableId tableId) {
        return null;
    }

    @Override
    public Boolean deductGoodsInventory(List<DeductGoodsInventory> deductGoodsInventories) {
        return null;
    }

    /** 在表中查出是否有相同信息的商品 */
    public Optional<RepositoryGoods> findFirstByGoodsCategoryAndBrandCategoryAndGoodsName(String goodsCategory, String brandCategory, String goodsName) {
        LambdaQueryWrapper<RepositoryGoods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RepositoryGoods::getGoodsCategory, goodsCategory);
        queryWrapper.eq(RepositoryGoods::getBrandCategory, brandCategory);
        queryWrapper.eq(RepositoryGoods::getGoodsName, goodsName);
        return Optional.ofNullable(this.getOne(queryWrapper));
    }
}
