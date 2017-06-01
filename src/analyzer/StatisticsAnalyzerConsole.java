package analyzer;

import analyzer.csv.CSVWriter;
import core.Core;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

/**
 * Created by garik on 01.06.17.
 */
public class StatisticsAnalyzerConsole implements StatisticsAnalyzer {
    private Core core;
    private List<String> lastResult = null;

    public static void main(String[] args) {
        if (args == null || args.length < 2) {
            System.out.println("Not enough arguments");
            System.out.println("Use: StatisticsAnalyzerConsole <name> <port>");
            return;
        }
        int port = getInteger(args[1]);
        if (port == -1) {
            System.out.println("Invalid format of port");
        }
        StatisticsAnalyzerConsole analyzer;
        try {
            analyzer = new StatisticsAnalyzerConsole(args[0], port);
        } catch (RemoteException | NotBoundException e) {
            System.out.println(e.getMessage());
            return;
        }

        analyzer.run();
    }


    /**
     * Run StatisticsAnalyzer for user, which can be used from console
     */
    @Override
    public void run() {
        try (
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in, "UTF-8"))
        ) {
            boolean shouldExit = false;
            while (!shouldExit) {
                String inputCommand = input.readLine();
                StringTokenizer tokenizer = new StringTokenizer(inputCommand);
                String command = tokenizer.nextToken();
                switch (command) {
                    case "top":
                        int count = getInteger(tokenizer.nextToken());
                        if (count == -1) {
                            System.out.println("Invalid count");
                            return;
                        }
                        lastResult = topRequests(count);
                        break;
                    case "write":
                        try {
                            write(tokenizer.nextToken());
                        } catch (FileNotFoundException e) {
                            System.out.println("File not found");
                        } catch (UnsupportedEncodingException e) {
                            System.out.println("UTF-8 unsupported");
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case "exit":
                        shouldExit = true;
                        break;
                    default:
                        System.out.println("No such command");
                        System.out.println("Use:");
                        System.out.println("\ttop <n>");
                        System.out.println("\twrite <file>");
                        System.out.println("\texit");
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param name name, where core is registed
     * @param port port, where core is registed
     * @throws RemoteException if the registry could not be exported or if remote communication with the
     * registry failed
     * @throws NotBoundException if <code>name</code> is not currently bound
     */
    public StatisticsAnalyzerConsole(String name, int port) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.createRegistry(port);
        core = (Core) registry.lookup(name);
    }

    private static int getInteger(String arg) {
        int port;
        try {
            port = Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            port = -1;
        }
        return port;
    }

    /**
     * Write last result
     *
     * @param path file, where should be write
     * @throws FileNotFoundException if file not found
     * @throws UnsupportedEncodingException if encoding not support
     * @throws IOException  If an I/O error occurs
     *
     * @see CSVWriter
     * @see String
     * @see FileNotFoundException
     * @see UnsupportedEncodingException
     * @see IOException
     */
    public void write(String path) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        try (
                CSVWriter csvWriter = new CSVWriter
                        (new OutputStreamWriter(new FileOutputStream(path), "UTF-8"));
        ) {
            for (int i = 0; i < lastResult.size(); i++) {
                csvWriter.write(Arrays.asList(i + 1, lastResult.get(i)));
            }
        }
    }

    /**
     * Get last n string from core, which was in requests
     *
     * @param n count of last String
     * @return List of last n String from requests from core
     */
    @Override
    public List<String> topRequests(int n) {
        List<String> result = new ArrayList<>();
        Set<String> resultImpl = new HashSet<>();
        List<String> requests = core.getLastRequests();
        for (int i = 0; resultImpl.size() < n && i < requests.size(); i++) {
            String request = requests.get(i);
            StringTokenizer tokenizer = new StringTokenizer(request);
            //Skip first command
            tokenizer.nextToken();
            while (tokenizer.hasMoreTokens() && resultImpl.size() < n) {
                String s = tokenizer.nextToken();
                if (!resultImpl.contains(s)) {
                    resultImpl.add(s);
                    result.add(s);
                }
            }
        }
        return result;
    }
}
