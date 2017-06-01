package core;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Server provide access to starting core
 */
public class Server {
    /**
     * Start core
     * @param args have to contain 2 arguments. First is hostname, second is port
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Invalid arguments length. Must be 2: host name and port");
            return;
        }


        final CoreImpl core;

        try {
            int port;
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid second argument, mast be number");
                return;
            }
            try {
                core = new CoreImpl(port);
            } catch (CoreException e) {
                System.err.println("Error during initialization core");
                e.printStackTrace();
                return;
            }
            Registry registry = LocateRegistry.createRegistry(port);
            registry.rebind("//" + args[0] + "/string_manager", core);
        } catch (RemoteException e) {
            System.out.println("Cannot export object: " +
                    e.getMessage());
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
        try {
            while (!Thread.interrupted()) {
                synchronized (core) {
                    core.wait();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
