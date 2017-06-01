package core;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by karen on 01.06.2017.
 */
public class Server {
    public static void main(String[] args) {

        final CoreImpl core;
        try {
            core = new CoreImpl();
        } catch (CoreException e) {
            System.err.println("Error during initialization core");
            e.printStackTrace();
            return;
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                core.saveChanges();
            } catch (CoreException e) {
                System.err.println("Error during saving data");
            }

        }, "Shutdown-thread"));
        if (args.length != 2) {
            System.err.println("Invalid arguments length. Must be 2: host name and port");
            return;
        }
        try {
            int port;
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid second argument, mast be number");
                return;
            }
            Registry registry = LocateRegistry.createRegistry(port);
            registry.rebind("//" + args[0] + "/string_manager", core);
        } catch (RemoteException e) {
            System.out.println("Cannot export object: " +
                    e.getMessage());
            e.printStackTrace();
        }
    }
}
