
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends JFrame {

    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private String serverIP;
    private Socket connection;
    private JScrollPane scroll;
    private JButton button;

    public Client(String host){
        super("You are a user of Tushar's instant Messenger");
        serverIP = host;
        userText = new JTextField();
        userText.setEditable(false);
        button = new JButton("Send");
        button.setBackground(Color.MAGENTA);
        setLayout(null);
        setResizable(false);
        chatWindow = new JTextArea();
        scroll = new JScrollPane(chatWindow);


        userText.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sendData(e.getActionCommand());
                        userText.setText("");
                    }
                }
        );
        button.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sendData(userText.getText());
                        userText.setText("");
                    }
                }
        );


        add(userText);
        userText.setBounds(0,0,400,30);


        button.setLocation(401,0);
        button.setSize(98,30);
        add(button);



        scroll.setLocation(0,31);
        scroll.setSize(500,600);

        add(scroll);

        setSize(500,700);
        setVisible(true);
    }

    //connecting to server

    public void startRunning(){
        try{

            connectToServer();
            setupStreams();
            whileChating();

        }catch (EOFException eofExeption){
            showMessage("\n Client Ended");
        }catch (IOException ioException){
            ioException.printStackTrace();
        }finally {
            closeCrap();
        }
    }

    //connect To Server

    private void connectToServer() throws IOException{
        showMessage("Attempting connection...\n");
        connection = new Socket(InetAddress.getByName(serverIP),6789);
        showMessage("Connected to:"+connection.getInetAddress().getHostName());
    }

    //setup Streams

    private void setupStreams() throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\n your streams are good to go\n");
    }


    //while Chatting

    private void whileChating() throws IOException{
        ableToType(true);
        do{
            try{
                message = (String)input.readObject();
                showMessage("\n" + message);
            }catch (ClassNotFoundException classNotFoundException){
                showMessage("\n I dont know");
            }
        }while (!message.equals("HE - END"));
    }

    //close all

    private void closeCrap(){
        showMessage("\n Closing All...");
        ableToType(false);
        try{
            output.close();
            input.close();
            connection.close();
        }catch (IOException ioException){
            ioException.printStackTrace();
        }
    }


    //send message

    private void sendData(String message){
        try{
            output.writeObject("HE - "+message);
            output.flush();
            showMessage("\nYOU - " +message);
        }catch (IOException ioException){
            chatWindow.append("\n something messed up!");
        }
    }

    //show message

    private void showMessage(final String text){
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        chatWindow.append(text);
                    }
                }
        );
    }

    //able to type

    private void ableToType(final boolean tof){
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        userText.setEditable(tof);
                    }
                }
        );
    }
}
