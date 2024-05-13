package com.faker.project.designpattern;

/**
 * 抽象工厂: 创建一组对象(产品族)
 */
@SuppressWarnings("all")
public class AbstractFactory {

    public static class AbstractProduct_yun {}

    public static class AbstractProduct_market {}

    public static class Aliyun extends AbstractProduct_yun {}

    public static class alimarket extends AbstractProduct_market {}

    public static class Tencentyun extends AbstractProduct_yun {}

    public static class Tencentmarket extends AbstractProduct_market {}

    public static abstract class AbstractFactoryClaz {
        abstract AbstractProduct_yun createProduct_yun();
        abstract AbstractProduct_market createProduct_market();
    }

    public static class ConcreteFactory_Alibaba extends AbstractFactoryClaz {
        AbstractProduct_yun createProduct_yun() {
            return new Aliyun();
        }

        AbstractProduct_market createProduct_market() {
            return new alimarket();
        }
    }

    public static class ConcreteFactory_Tencent extends AbstractFactoryClaz {
        AbstractProduct_yun createProduct_yun() {
            return new Tencentyun();
        }

        AbstractProduct_market createProduct_market() {
            return new Tencentmarket();
        }
    }

    public static void main(String[] args) {
        // 创建阿里系产品: aliyun alimarket
        ConcreteFactory_Alibaba alibabaFactory = new ConcreteFactory_Alibaba();
        AbstractProduct_yun aliyun = alibabaFactory.createProduct_yun();
        AbstractProduct_market alimarket = alibabaFactory.createProduct_market();

        // do something with productA and productB
    }


}
