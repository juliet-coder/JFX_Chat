package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    static ArrayList<Socket> clients= new ArrayList<>();
    static ArrayList<String> users = new ArrayList<>();
    public static void main(String[] args) {
        Socket socket = null;
        try{
            ServerSocket serverSocket = new ServerSocket(8189);
            System.out.println("Сервер запущен");
            while(true) {
                socket = serverSocket.accept();
                clients.add(socket);
                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                System.out.println("Клиент подключился");
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while (true) {
                                String str = in.readUTF();
                                if (str.split("//")[0].equals("userNick")){
                                    users.add(str.split("//")[1]);
                                    str = "userList";
                                    for (String userNick:users) str += "//"+userNick;
                                    broadcastMsg(str);
                                }else {
                                    broadcastMsg(str);
                                }
                                System.out.println("Клиент прислал сообщение: " + str);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    public static void broadcastMsg(String str) throws IOException {
        DataOutputStream out;
        for (Socket socket: clients){
            out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(str);
        }
    }
}