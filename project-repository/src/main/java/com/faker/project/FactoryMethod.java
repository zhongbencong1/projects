package com.faker.project;

/**
 * 工厂方法: 将对象实例化交给子类
 * 新加入产品，就无需修改抽象工厂类和抽象产品类，也无需修改其他具体工厂和产品，而只用添加一个具体工厂和具体产品即可
 */
@SuppressWarnings("all")
public class FactoryMethod {
    // 抽象工厂
    public static abstract class AbstractFactory {
        public abstract Product abstractFactoryfunc();//抽象工厂方法.

        // 对实际调用代码进行了一次封装 简化line 58 factoryA.abstractFactoryfunc().productFunc(); 的调用方式
        public void productFunc(){
            Product product = this.abstractFactoryfunc();//在工厂类中直接调用生产产品方法
            product.productFunc();
        }
    }
    
    public static abstract class Product {
        public abstract void productFunc();//抽象产品方法
    }

    public static class QiaokeliFactory extends AbstractFactory {
        @Override
        public Product abstractFactoryfunc() {
            Product qiaokeli = new Qiaokeli();//创建巧克力
            return qiaokeli;
        }
    }

    public static class Qiaokeli extends Product {
        @Override
        public void productFunc() {
            System.out.println("生成巧克力"); // dosomething
        }
    }


    public static class CandyFactory extends AbstractFactory {
        @Override
        public Product abstractFactoryfunc() {
            Product candy = new Candy();//创建糖果
            return candy;
        }
    }

    public static class Candy extends Product {
        @Override
        public void productFunc() {
            System.out.println("宝马车跑的快..."); // dosomething
        }
    }

    // 实际使用方式
    public static void main(String[] args) {
        AbstractFactory factoryA = new QiaokeliFactory();
        factoryA.abstractFactoryfunc().productFunc();

        AbstractFactory factoryB = new CandyFactory();
        factoryB.abstractFactoryfunc().productFunc();

        // 对应line 15-16 ，可以快速调用工厂方法生产对象的方法
        // 相当于对factoryA.abstractFactoryfunc().productFunc();的封装
        new QiaokeliFactory().productFunc();
    }

}
