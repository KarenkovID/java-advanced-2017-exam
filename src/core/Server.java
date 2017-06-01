package core;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by karen on 01.06.2017.
 */
public class Server {
    public static void main(String[] args) {
        Core core = new CoreImpl();
        try {
            UnicastRemoteObject.exportObject(core);
            Naming.rebind("//localhost/string_manager", core);
        } catch (RemoteException e) {
            System.out.println("Cannot export object: " +
                    e.getMessage());
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL");
        }
    }
}
