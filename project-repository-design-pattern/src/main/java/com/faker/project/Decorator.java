package com.faker.project;

/**
 * 装饰器模式
 */
public class Decorator {
    public interface Beverage { double cost();}

    public static class HouseBlend implements Beverage {
        public double cost() {return 1;}
    }

    public static abstract class CondimentDecorator implements Beverage {
        protected Beverage beverage;
    }

    public static class Milk extends CondimentDecorator {
        public Milk(Beverage beverage) {this.beverage = beverage;}
        public double cost() {return 1 + beverage.cost();}
    }

    public static class Mocha extends CondimentDecorator {
        public Mocha(Beverage beverage) {this.beverage = beverage;}
        public double cost() {return 1 + beverage.cost();}
    }

    // 对象放入另外一个对象, 进行包装, 需要写同一个方法
    public static void main(String[] args) {
        Beverage beverage = new Milk(new Mocha(new HouseBlend()));
        System.out.println(beverage.cost());
    }
}
