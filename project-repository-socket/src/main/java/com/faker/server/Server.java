package com.faker.server;

import cn.hutool.json.JSONObject;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        System.out.println("服务端启动");
        try {
            ServerSocket serverSocket = new ServerSocket(9001);
            while (true) {
                // 接收socket請求
                Socket socket = serverSocket.accept();
                new Thread(new ServerListener(socket)).start();
                new Thread(new ServerSend(socket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
// 监听server socket链接
class ServerListener implements Runnable {

    private Socket socket;

    public ServerListener(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            while (true) {
                // 打印输入
                System.out.println(in.readObject());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
// 对外发送数据
class ServerSend implements Runnable {
    private Socket socket;

    public ServerSend(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("服务端 输入：");
                String str = scanner.nextLine();
                JSONObject obj = new JSONObject();
                obj.set("type", "msg");
                obj.set("content", str);
                oos.writeObject(obj);
                oos.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


