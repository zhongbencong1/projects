package com.faker.project;

/**
 * 模板方法
 * 抽象类中定义方法集合, 部分方法实现, 部分抽象方法交由子类实现
 */
public abstract class TemplateMethod {
    final void prepareRecipe() {
        boilWater();
        brew();
        pourInCup();
        addCondiments();
    }
    abstract void brew();
    abstract void addCondiments();
    void boilWater() {System.out.println("boilWater");}
    void pourInCup() {System.out.println("pourInCup");}

    public static class Coffee extends TemplateMethod {
        void brew() {System.out.println("Coffee.brew");}
        void addCondiments() {System.out.println("Coffee.addCondiments");}
    }

    public static class Tea extends TemplateMethod {
        void brew() {System.out.println("Tea.brew");}
        void addCondiments() {System.out.println("Tea.addCondiments");}
    }

    public static void main(String[] args) {
        TemplateMethod caffeineBeverage = new Coffee();
        caffeineBeverage.prepareRecipe();
        System.out.println("-----------");
        caffeineBeverage = new Tea();
        caffeineBeverage.prepareRecipe();
    }
}
