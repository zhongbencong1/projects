package com.dk.client;

import cn.hutool.json.JSONObject;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static Socket socket;
    public static boolean isConnected = false;
    public static void main(String[] args) {
        while (!isConnected) {
            connect();
            try {
                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void connect() {
        try {
            socket = new Socket("127.0.0.1", 9001);
            isConnected = true;
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            new Thread(new ClientListen(socket, objectInputStream)).start();
            new Thread(new ClientSend(socket, objectOutputStream)).start();
            new Thread(new ClientHeart(socket, objectOutputStream)).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void reconnect(){
        while (true) {
            System.out.println("正在尝试重新链接...");
            connect();
            try {
                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
// 客户端监听
class ClientListen implements Runnable {
    private Socket socket;
    private ObjectInputStream objectInputStream;

    public ClientListen(Socket socket, ObjectInputStream objectInputStream) {
        this.socket = socket;
        this.objectInputStream = objectInputStream;
    }

    @Override
    public void run() {
        try {
            System.out.println(objectInputStream.readObject());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// 客户端发送
class ClientSend implements Runnable {
    private Socket socket;
    private ObjectOutputStream objectOutputStream;

    public ClientSend(Socket socket, ObjectOutputStream objectInputStream) {
        this.socket = socket;
        this.objectOutputStream = objectInputStream;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        try {
            while (true) {
                System.out.println("客户端 输入：");
                String str = scanner.nextLine();
                JSONObject obj = new JSONObject();
                obj.set("type", "msg");
                obj.set("content", str);
                objectOutputStream.writeObject(obj);
                objectOutputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class ClientHeart implements Runnable {

    private Socket socket;
    private ObjectOutputStream objectOutputStream;

    public ClientHeart(Socket socket, ObjectOutputStream objectInputStream) {
        this.socket = socket;
        this.objectOutputStream = objectInputStream;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(5000);
                System.out.println("心跳检测");
                JSONObject obj = new JSONObject();
                obj.set("type", "heart");
                obj.set("content", "心跳...");
                objectOutputStream.writeObject(obj);
                objectOutputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                socket.close();
                Client.isConnected = false;
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
    }
}
