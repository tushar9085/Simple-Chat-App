import javax.swing.*;

public class ServerTest {
    public static void main(String[] args) {
        Server tus = new Server();
        tus.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tus.startRunning();
    }
}
