package analyzer;

import analyzer.csv.CSVWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by garik on 01.06.17.
 */
public class StatisticsAnalyzerConsole implements StatisticsAnalyzer {


    public static void main(String[] args) {
        StatisticsAnalyzerConsole analyzer = new StatisticsAnalyzerConsole();
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
                        break;
                    case "write":
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

    private void writeToCVS(String path, List<List<Object>> values) {
        try (
                CSVWriter csvWriter = new CSVWriter
                        (new OutputStreamWriter(new FileOutputStream(path), "UTF-8"));
        ) {
            for (List<Object> value : values) {
                csvWriter.write(value);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> topRequests(int n) {
        List<String> result = new ArrayList<>();
        return result;
    }
}
