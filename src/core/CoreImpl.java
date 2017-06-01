package core;

import com.sun.istack.internal.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * implementation of {@link Core}
 */
public class CoreImpl implements Core, Serializable {
    private static final String FILE_NAME = "string_base.txt";

    @NotNull
    private List<String> lastRequests;
    @NotNull
    private TreeSet<String> stringsSet;

    CoreImpl() throws CoreException {
        stringsSet = getDataFromFile();
        lastRequests = new LinkedList<>();
    }

    @Override
    public void addString(String str) {
        log("addString", str);
        stringsSet.add(str);
    }

    @Override
    public void removeString(String str) {
        log("removeString", str);
        stringsSet.remove(str);
    }

    @Override
    public List<String> getAllStrings() {
        log("getAllStrings");
        return new ArrayList<>(stringsSet);
    }

    @Override
    public boolean[] includes(@NotNull List<String> strings) {
        log("includes");
        boolean[] res = new boolean[strings.size()];
        int i = 0;
        for (String stringForChecking : strings) {
            if (stringsSet.contains(stringForChecking)) {
                res[i] = true;
            }
            i++;
        }
        return res;
    }

    @Override
    public List<String> getLastRequests() {
        log("getLastRequests");
        return Collections.unmodifiableList(lastRequests);
    }

    private void log(String methodName, String arg) {
        lastRequests.add(methodName + " " + arg);
    }

    private void log(String methodName) {
        lastRequests.add(methodName);
    }

    @NotNull
    private TreeSet<String> getDataFromFile() throws CoreException {
        Path path = Paths.get(FILE_NAME);

        if (Files.notExists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                System.err.println("Can't create new file in path: " + path);
                throw new CoreException(e);
            }
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(FILE_NAME), StandardCharsets.UTF_8))){
            TreeSet<String> res = new TreeSet<>();
            String line;
            while ((line = br.readLine()) != null) {
                res.add(line);
            }
            return res;
        } catch (FileNotFoundException e) {
            System.err.println("Can't get access to file in path: " + path);
            throw new CoreException(e);
        } catch (IOException e) {
            throw new CoreException(e);
        }
    }

    private void saveStrings() throws CoreException {
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(FILE_NAME), StandardCharsets.UTF_8))){
            for (String str : stringsSet) {
                pw.println(str);
            }
        } catch (FileNotFoundException e) {
            throw  new CoreException(e);
        }
    }

    public void saveChanges() throws CoreException {
        saveStrings();
    }

}
