import javax.swing.*;
import java.net.HttpURLConnection;

public class ClientTest {
    public static void main(String[] args) {
        String serverIP;

        serverIP = JOptionPane.showInputDialog("give your friends IP : e.g. 127.0.0.1");
        Client tushar;
        tushar = new Client(serverIP);//local host
        tushar.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tushar.startRunning();
    }
}
