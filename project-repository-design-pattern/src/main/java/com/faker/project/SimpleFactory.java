package com.faker.project;

/**
 * 简单工厂
 */
public class SimpleFactory {
    public interface Computer{
    }

    public static class Huawei implements Computer{
    }

    public static class Xiaomi implements Computer{
    }

    public static class Huipu implements Computer{
    }

    public Computer CreateComputer(String type){
        if (type.equalsIgnoreCase("Huawei")) {
            return new Huawei();
        } else if (type.equalsIgnoreCase("Xiaomi")) {
            return new Xiaomi();
        } else if (type.equalsIgnoreCase("Huipu")) {
            return new Huipu();
        }
        return null;
    }

    // 使用
    public static void main(String[] args) {
        Computer computer = new SimpleFactory().CreateComputer("huawei");
    }
}
