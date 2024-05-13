package com.faker.project.designpattern;

/**
 * 适配器
 */
public class Adapter {
    public interface Duck { void quack();}
    public interface Turkey { void gobble();}

    public static class WildTurkey implements Turkey {
        @Override
        public void gobble() {
            System.out.println("gobble!");
        }
    }

    // 适配器类
    public static class TurkeyAdapter implements Duck {
        Turkey turkey;

        public TurkeyAdapter(Turkey turkey) {
            this.turkey = turkey;
        }

        @Override
        public void quack() {
            turkey.gobble();
        }
    }

    public static void main(String[] args) {
        Turkey turkey = new WildTurkey();
        Duck duck = new TurkeyAdapter(turkey);
        duck.quack();
    }

}
