package com.faker.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Entity {
    private String id;

    private String name;

    private DataInfo data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataInfo {

        private String phone;

        private String email;
    }
}
