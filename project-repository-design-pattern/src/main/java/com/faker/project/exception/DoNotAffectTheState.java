package com.faker.project.exception;

import cn.hutool.db.ds.pooled.PooledConnection;
import cn.hutool.db.ds.pooled.PooledDataSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 异常发生时尽量避免影响系统的状态: 不对对象的信息进行修改
 */
@SuppressWarnings("unused")
@Slf4j
public class DoNotAffectTheState {

    // 参数是不可变的, 状态就不会变
    public void printDataInfo(final DataInfo dataInfo) {
        log.info("printDataInfo: {}", dataInfo.getName());
        // 由于参数是final, 所以, 不可改变
//        dataInfo = new DataInfo("faker");
        dataInfo.setAge(19);
    }

    // 如果对象可变, 保持好状态:1.在执行操作之前检查参数的有效性 2.调整计算处理过程中的顺序 3.副本上进行操作,最后进行赋值, 4.编写恢复代码, 回滚到之前的状态
    /** 1.在执行操作之前检查参数的有效性 */
    public static void append(List<Integer> source, List<Integer> target) {
        assert null != source && null != target;
        // 参数有效性校验: Objects::nonNull
        target.addAll(source.stream().filter(Objects::nonNull).collect(Collectors.toList()));
    }

    /** 2.调整计算处理过程中的顺序 */
    public static void computeAge(DataInfo dataInfo, String birthday) {
        assert null != dataInfo;

        LocalDate today = LocalDate.now();
        LocalDate playerDate = LocalDate.from(DateTimeFormatter.ofPattern("yyyy-MM-dd").parse(birthday));
        long years = ChronoUnit.YEARS.between(playerDate, today);

        dataInfo.setBirthday(birthday);
        dataInfo.setAge((int) years);
    }

    /** 3.副本上操作 */
    public static void copy() {
        Collections.sort(new ArrayList<Integer>()); // ArrayList中先进行克隆, 后排序, 最后赋值 toArray:a.clone();
    }

    /** 4.编写恢复代码, 回滚到之前的状态: 事务 */
    public static void transaction() throws SQLException {
        Connection conn = new PooledConnection(new PooledDataSource());
        try {
            conn.setAutoCommit(false);
            // 执行很多 SQL 语句
            conn.commit();
        } catch (SQLException ex) {
            // 回滚事物
            conn.rollback();
        } finally {
//            conn.setAutoCommit(true);
            conn.close();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataInfo {
        private String name;
        private String birthday;  // yyyy-MM-dd
        private int age;

        public DataInfo(String name) {
            this.name = name;
        }
    }
}
