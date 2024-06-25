package com.faker.project.exception;

import lombok.extern.slf4j.Slf4j;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * 忽略异常
 */
@Slf4j
public class IgnoreException {

    @lombok.Data
    public static class Data {
        private String name;
        private String gender;
    }

    // 1. for 循环中大批量的处理数据, 一般都不会让异常直接抛出
    public void batchProcess(List<Data> dataList) {
        int num = 0;
        for (Data data : dataList) {
            try {
                num += (data.getGender().equals("m") ? 0 : 1);
            } catch (Exception ex) {
                // 记录下异常情况
            }
        }
        log.info("female num is: {}", num);
    }

    // 2. 存在网络请求(RPC), 允许一定次数的失败重试, 即忽略掉偶发性的异常
    private static void invokeRpc() throws Exception {
        URL obj = new URL("www.baidu.com");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        log.info("Response Code: {}", responseCode);
    }

    // 3. 不影响业务的整体逻辑情况, 例如手机验证码发送失败
}
