package editor;

import core.Core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by Nik on 01.06.2017.
 */
public class Editor {
    private BufferedReader br;
    private Core core;

    /**
     * Executes editor that connects to core and implements requests "add", "rm" and "print"
     * @param url is url of core
     * @param port is port of core
     */
    public void run(String url, String port) {
        br = new BufferedReader(new InputStreamReader(System.in));
        try {
            core = (Core) Naming.lookup("//" + url + ":" + port + "/string_manager");
        } catch (NotBoundException | RemoteException e) {
            System.err.println("Cannot connect to core");
            return;
        } catch (MalformedURLException e) {
            System.err.println("Core URL is invalid");
            return;
        }
        System.err.print("Enter \"add $s\" to add string $s\n" +
                "Enter \"rm $s\" to remove string $s\n" +
                "Enter \"print\" to print all strings\n" +
                "Enter \"exit\" to exit this window\n");
        while (true) {
            try {
                String line = br.readLine();

                if (line.startsWith("add ")) {
                    core.addString(line.substring("add ".length()));
                } else if (line.startsWith("rm ")) {
                    core.removeString(line.substring("rm ".length()));
                } else if (line.equals("print")) {
                    List<String> list = core.getAllStrings();
                    for (String s : list) {
                        System.out.println(s);
                    }
                } else if (line.equals("exit")) {
                    break;
                } else {
                    System.err.println("What do you mean?");
                }
            } catch (IOException e) {
                System.err.println("Input has broken");
                return;
            }
        }
    }

    /**
     * args[0] is url of core
     * args[1] is port of core
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        if (args.length == 2) {
            new Editor().run(args[0], args[1]);
        } else {
            new Editor().run("localhost", "4445");
        }
    }
}
