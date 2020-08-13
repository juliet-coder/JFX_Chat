package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Controller {
    public static Socket socket;
    public static DataOutputStream out;
    public static DataInputStream in;
    @FXML
    TextField textField;
    @FXML
    TextArea textArea;
    @FXML
    TextField userNick;
    @FXML
    TextArea userListTextArea;
    @FXML
    Button connectBtn;
    public void send() {
        String str = textField.getText();
        try {
            out= new DataOutputStream(socket.getOutputStream());
            out.writeUTF(userNick.getText()+": "+str);
            //textArea.appendText(str+"\n");
            textField.clear();
            textField.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void startClient(){
        connectBtn.setDisable(true);
        try{
            socket = new Socket("localhost",8189);
            in = new DataInputStream(socket.getInputStream());
            out= new DataOutputStream(socket.getOutputStream());
            out.writeUTF("userNick//"+userNick.getText());
            Thread responseThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        try {
                            String strFromSerer = in.readUTF();
                            if(strFromSerer.split("//")[0].equals("userList")){
                                String[] users = strFromSerer.split("//");
                                userListTextArea.setText("");
                                for(int i=1; i<users.length; i++){
                                    userListTextArea.appendText(users[i]+"\n");
                                }
                            }else
                                textArea.appendText(strFromSerer+"\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
            responseThread.start();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}