package com.faker.project.designpattern;

import lombok.Data;

/**
 * builder 建造者模式 复杂对象的创建过程拆分成多个简单对象的创建过程
 * 1.普通建造者, 将对象信息组装
 * 2.建造者: 链式编程 参考 Phone类 (推荐) @Accessors(chain = true) @Accessors(fluent=true) @Builder
 */
public class Builder {
    @Data
    public static class Meal {
        //汉堡包
        private String burger;
        //薯条
        private String fries;
    }

    public static abstract class MealBuilder {
        protected Meal meal = new Meal();
        //构建汉堡
        public abstract void buildBurger();
        //构建薯条
        public abstract void buildFries();

        public Meal getMeal() {
            return meal;
        }
    }

    public static class ChickenMealBuilder extends MealBuilder {
        @Override
        public void buildBurger() {meal.setBurger("鸡肉汉堡");}

        @Override
        public void buildFries() {meal.setFries("中份薯条");}
    }

    public static class ShrimpMealBuilder extends MealBuilder {
        @Override
        public void buildBurger() {meal.setBurger("虾肉汉堡");}

        @Override
        public void buildFries() {meal.setFries("小份薯条");}
    }

    public static class MealDirector {
        private MealBuilder mealBuilder;
        public void setMealBuilder(MealBuilder mealBuilder) {this.mealBuilder = mealBuilder;}
        public Meal getMeal() {return mealBuilder.getMeal();}
        //制作套餐
        public void constructMeal() {
            mealBuilder.buildBurger();
            mealBuilder.buildFries();
        }
    }

    public static void main(String[] args) {
        //创建指导者
        MealDirector director=new MealDirector();

        //鸡肉套餐
        director.setMealBuilder(new ChickenMealBuilder());
        director.constructMeal();
        Meal meal2 = director.getMeal();
        System.out.println("鸡肉套餐："+meal2.toString());

        //虾肉套餐
        director.setMealBuilder(new ShrimpMealBuilder());
        director.constructMeal();
        Meal meal3 = director.getMeal();
        System.out.println("虾肉套餐："+meal3.toString());


        //创建手机对象,通过构建者对象获取手机对象, 链式编程
        Phone phone = new Phone.Builder_Ext()
                .cpu("intel")
                .screen("三星屏幕")
                .build();
        System.out.println(phone);
    }

    public static class Phone {
        private String cpu;
        private String screen;

        //私有构造方法
        private Phone(Builder_Ext builer) {
            this.cpu = builer.cpu;
            this.screen = builer.screen;
        }

        public static class Builder_Ext{
            private String cpu;
            private String screen;

            public Builder_Ext cpu(String cpu) {
                this.cpu = cpu;
                return this;
            }

            public Builder_Ext screen(String screen) {
                this.screen = screen;
                return this;
            }

            //使用构建者创建Phone对象
            public Phone build() {
                return new Phone(this);
            }
        }
    }
}
