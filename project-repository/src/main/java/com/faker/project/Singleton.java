package com.faker.project;

/**
 * 单例模式：推荐4
 * 1.饿汉式
 * 2.懒汉式 双重校验锁
 * 3.静态内部类
 * 4.枚举类实现
 */
@SuppressWarnings("all")
public class Singleton {
    // 1.饿汉式
    // private static Singleton unique = new Singleton();

    public volatile static Singleton UNIQUE;

    public Singleton() {
    }

    // 2.懒汉式 双重检验锁
    public static Singleton getUniqueInstance() {
        if (UNIQUE == null) {
            synchronized (Singleton.class) {
                if (UNIQUE == null) {
                    UNIQUE = new Singleton();
                }
            }
        }
        return UNIQUE;
    }

    // 3.静态内部类 Singleton加载时StaticSingleton并没有加载，只有调用getInstance方法才会触发StaticSingleton加载来初始化UNIQUE
    private static class StaticSingleton{
        private static final Singleton UNIQUE = new Singleton();
    }

    public static Singleton getInstance() {
        return StaticSingleton.UNIQUE;
    }

    // 4.枚举 防止序列化和反射攻击时多次实例化
    public enum EnumSingletonObject{
        uniqueInstance;

        public void method() {
            // do something
        }

        public static void main(String[] args) {
            // 其他方法调用方式
            EnumSingletonObject.uniqueInstance.method();
        }
    }
}
