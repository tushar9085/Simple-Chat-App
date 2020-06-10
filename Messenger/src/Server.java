import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends JFrame {

    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;
    private JButton button;
    private JScrollPane scroll;


    //Constructor

    public Server(){
        super("Tushar's Instant Messenger");
        userText = new JTextField();
        userText.setEditable(false);     //before connection msg deya waste of time
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
                        sendMessage(e.getActionCommand());
                        userText.setText("");
                    }
                }
        );

        button.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sendMessage(userText.getText());
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

    //Setup and run the server

    public void startRunning(){
        try{
            server = new ServerSocket(6789,100);
            while(true){
                try{
                    //connection and have conversation
                    waitForConnection();
                    setupStreams();
                    whileChatting();
                }catch (EOFException eofException){
                    showMessage("\nServer ended the connection");
                }finally {
                    closeCrap();
                }
            }
        }catch (IOException ioException){
            ioException.printStackTrace();
        }
    }

    //wait For Connection,then display connection info

    private void waitForConnection() throws IOException{
        showMessage("Waitiong for someone to connect...");
        connection = server.accept();
        showMessage("Now connected to"+ connection.getInetAddress().getHostName());
    }

    //get stream to seand & recieve data

    private void setupStreams() throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\n Streams are now setup\n");
    }

    //while chating

    private void whileChatting() throws IOException{
        String message = "You are now connected!";
        sendMessage(message);
        ableToType(true);
        do{
            try{
                message = (String) input.readObject();
                showMessage("\n "+message);
            }catch (ClassNotFoundException classNotFoundException){
                showMessage("\n Idk wtf that user send!");
            }
        }while (!message.equals("HE - END"));
    }

    //Close all

    private void closeCrap(){
        showMessage("\n Clossing Connection \n");
        ableToType(false);
        try{
            output.close();
            input.close();
            connection.close();
        }catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    //send message

    private void sendMessage(String message){
        try{
            output.writeObject("HE - " + message);
            output.flush();
            showMessage("\nYOU -" + message);
        }catch (IOException ioException){
            chatWindow.append("\n ERROR : DUDE I CANT SEND!");
        }
    }

    //showMessage

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

    //ableToType

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
