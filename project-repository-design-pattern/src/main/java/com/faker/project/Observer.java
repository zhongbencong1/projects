package com.faker.project;

import java.util.ArrayList;
import java.util.List;

/**
 * 观察者模式
 * 以发布天气预报为类
 */
public class Observer {
    // 被观察者/发布者
    public interface Subject {
        void resisterObserver(ObserverObj o);
        void removeObserver(ObserverObj o);
        void notifyObserver();
    }

    // 观察者/订阅者
    public interface ObserverObj {
        void update(float temp, float humidity, float pressure);
    }

    public static class WeatherInfo implements Subject {
        private final List<ObserverObj> observers = new ArrayList<>();
        private float temperature; //温度
        private float humidity; //湿度
        private float pressure; // 气压

        public void setMeasurements(float temperature, float humidity, float pressure) {
            this.temperature = temperature;
            this.humidity = humidity;
            this.pressure = pressure;
            notifyObserver();
        }

        public void resisterObserver(ObserverObj o) {observers.add(o);}
        public void removeObserver(ObserverObj o) {observers.remove(o);}
        public void notifyObserver() {
            observers.forEach(o -> o.update(temperature, humidity, pressure));// 通知observers中的观察者
        }
    }

    // 观察者类 StatisticsDisplay CurrentConditionsDisplay
    public static class StatisticsDisplay implements ObserverObj {
        public StatisticsDisplay(Subject weatherData) {weatherData.resisterObserver(this);}

        @Override
        public void update(float temp, float humidity, float pressure) {
            System.out.println("StatisticsDisplay.update: " + temp + " " + humidity + " " + pressure);
        }
    }
    public static class CurrentConditionsDisplay implements ObserverObj {
        public CurrentConditionsDisplay(Subject weatherData) {weatherData.resisterObserver(this);}

        @Override
        public void update(float temp, float humidity, float pressure) {
            System.out.println("CurrentConditionsDisplay.update: " + temp + " " + humidity + " " + pressure);
        }
    }

    public static void main(String[] args) {
        WeatherInfo weatherData = new WeatherInfo();
        // 注册订阅者
        CurrentConditionsDisplay currentConditionsDisplay = new CurrentConditionsDisplay(weatherData);
        StatisticsDisplay statisticsDisplay = new StatisticsDisplay(weatherData);

        weatherData.setMeasurements(0, 0, 0);
//        weatherData.setMeasurements(1, 1, 1);
    }
}
