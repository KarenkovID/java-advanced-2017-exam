package querytool;

import core.Core;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.util.stream.IntStream;



public class QueryTool {

    /**
     * Query tool, which can show what list include in kernel's data and what don't exist
     *
     * @author Toropin Konstantin
     *         impy.bian@gmail.com
     *         ITMO University, 2017
     **/


    /**
     *  length of colon of table
     */
    final static int MAXLENGTH = 20;
    private int port;
    private String name;
    private Core core;
    public QueryTool(int port, String name) {
        this.port = port;
        this.name = name;
    }
    /**
     * Create query for server by RMI and it return
     * @param data List of String, what we want to acknowledge inclusion
     * @return array of inclusion:
     * True - String exist in kernel's data
     * False - String not exist in kernel's data
     */
    public  boolean[] queryBool(List<String> data) throws RemoteException, NotBoundException {
        if (core == null) {
            Registry registry = LocateRegistry.getRegistry(port);
            core = (Core) registry.lookup("//" + name + "/string_manager");
        }
        return core.includes(data);
    }

    /**
     * Show all information about inclusion List of String <code> data</code>
     * @param data List of String, what we want to acknowledge inclusion
     */
    public void showInclusion(List<String> data) throws RemoteException, NotBoundException {
        showQuery(data, queryBool(data));
    }

    /**
     * Print one line in our table
     * @param exist first String
     * @param notExist second String
     * @return string
     */
    private static String ExistNotExist(String exist, String notExist) {
        StringBuilder stringBuilder = new StringBuilder().append(
                "|" + exist).append(
                String.join("", Collections.nCopies(MAXLENGTH - exist.length(), " "))).append(
                "|" + notExist).append(
                String.join("", Collections.nCopies(MAXLENGTH - notExist.length(), " "))).append(
                "|");
        return stringBuilder.toString();
    }

    /**
     *  Just print in console line with symbol '_'
     */
    private static void printHorizontal() {
        System.out.println(String.join("", Collections.nCopies(MAXLENGTH * 2 + 2,"_")));
    }

    /**
     * Show all information about include our string <code>data</code>
     * by arrays <code>exist</code>
     * @param data List of String
     * @param exist array of boolean, what we exist or not exist
     */
    private static void showQuery(List<String> data, boolean[] exist) {
        if (data.size() != exist.length) {
            System.err.print("Sorry, we don't have all information about our strings");
        }
        int j = 0;
        while(j < data.size() && exist[j]) j++;

        printHorizontal();
        System.out.println(ExistNotExist("exist", "not exist"));
        for (int i = 0; i < data.size() || j < data.size(); i++) {
            if (i < data.size() && !exist[i]) {
                continue;
            }

            printHorizontal();
            String stringEx = "";
            if (i < data.size()) {
                stringEx = data.get(i);
            }
            String stringNotEx = "";
            if (j < data.size()) {
                stringNotEx = data.get(j);
                j++;
                while(j < data.size() && exist[j]) j++;
            }
            int k = 0;
            while (k < Math.max(stringEx.length(), stringNotEx.length())) {
                System.out.println(ExistNotExist(
                        stringEx.substring(Math.min(k,stringEx.length()), Math.min(stringEx.length(), k + MAXLENGTH)),
                        stringNotEx.substring(Math.min(k,stringNotEx.length()), Math.min(stringNotEx.length(), k + MAXLENGTH)))
                );
                k += MAXLENGTH;
            }
        }
        printHorizontal();
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

    public static void main(String[] args) {
        if (args == null || args.length < 2) {
            System.out.println("Not enough arguments");
            System.out.println("Use: QueryTool <name> <port>");
            return;
        }
        int port = getInteger(args[1]);
        if (port == -1) {
            System.out.println("Invalid format of port");
        }
        QueryTool tool = new QueryTool(port, args[0]);

        System.out.println("Print number of strings");
        try (BufferedReader input = new BufferedReader(new InputStreamReader(System.in, "UTF-8"))) {
            int n = Integer.parseInt(input.readLine());
            List<String> list = new ArrayList<>();
            IntStream.range(0, n).forEach((int i) -> {
                try {
                    list.add(input.readLine());
                } catch (IOException e) {
                    System.err.println("Wrong format Strings");
                }
            });
            tool.showInclusion(list);
        } catch (IOException e) {
            System.err.println("Not enough strings");

        } catch (NotBoundException e) {
            System.err.println("Not bound");
        }
    }

}
