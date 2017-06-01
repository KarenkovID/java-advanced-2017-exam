package analyzer.csv;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

/**
 * Created by garik on 01.06.17.
 */
public class CSVWriter implements AutoCloseable {
    private static final String[] reserved = new String[] {
            "\"", ",", "\n", "\r"
    };
    private Writer writer;

    public CSVWriter(Writer writer) {
        this.writer = writer;
    }

    public void write(List<Object> values) throws IOException {
        for (Object o: values) {
            String result = o.toString();
            if (Arrays.stream(reserved).anyMatch(result::contains)) {
                result = "\"" + result.replaceAll("\"", "\"\"") + "\"";
            }
            writer.write(result);
            writer.write(",");
        }
        writer.write("\n");
    }

    public void flush() throws IOException {
        this.writer.flush();
    }

    public void close() throws IOException {
        this.writer.close();
    }



}
