package ServerTrivia;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Socket service = null;
        DataInputStream sIn = null;
        DataOutputStream sOut = null;

        try (ServerSocket server = new ServerSocket(2000);) {

            service = server.accept();
            sIn = new DataInputStream(service.getInputStream());
            sOut = new DataOutputStream(service.getOutputStream());

            String message = sIn.readUTF();
            System.out.println(message);
            sOut.writeUTF("recibido");
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            try {
                if (sOut != null) sOut.close();
            } catch (IOException e) {
            }
            try {
                if (sIn != null) sIn.close();
            } catch (IOException e) {
            }
            try {
                if (service != null)
                    service.close();
            }catch(IOException e){
            }
        }
    }
}
