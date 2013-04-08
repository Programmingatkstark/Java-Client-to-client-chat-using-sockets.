import java.io.*;
import java.util.*;
import java.net.*;
import static java.lang.System.out;

public class ChatServer {

    public static void main(String... args) throws Exception {
        new ChatServer().createserver();
    } 
    
    Vector<String> users = new Vector<String>();
    Vector<Manageuser> clients = new Vector<Manageuser>();

    public void createserver() throws Exception {
        ServerSocket server = new ServerSocket(80, 10);
        out.println("Now Server Is Running");
        while (true) {
            Socket client = server.accept();
            Manageuser c = new Manageuser(client);
            clients.add(c);
        }
    }

    

    public void sendtoall(String user, String message) {

        for (Manageuser c : clients) {
            if (!c.getchatusers().equals(user)) {
                c.sendMessage(user, message);
            }
        }
    }

    class Manageuser extends Thread {

        String gotuser = "";
        BufferedReader input;
        PrintWriter output;

        public Manageuser(Socket client) throws Exception {

            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            output = new PrintWriter(client.getOutputStream(), true);

            gotuser = input.readLine();
            users.add(gotuser); 
            start();
        }

        public void sendMessage(String chatuser, String chatmsg) {
            output.println(chatuser + " Says:" + chatmsg);
        }

        public String getchatusers() {
            return gotuser;
        }

        @Override
        public void run() {
            String line;
            try {
                while (true) {
                    line = input.readLine();
                    if (line.equals("end")) {
                        clients.remove(this);
                        users.remove(gotuser);
                        break;
                    }
                    sendtoall(gotuser, line); 
                }
            } 
            catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        } 
    } 
} 
