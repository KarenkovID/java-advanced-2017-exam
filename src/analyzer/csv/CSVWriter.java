package analyzer.csv;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by garik on 01.06.17.
 */
public class CSVWriter implements AutoCloseable {
    private static final String[] reserved = new String[]{
            "\"", ",", "\n", "\r"
    };
    private Writer writer;

    /**
     * Create CSV writer, which will write csv information to writer
     *
     * @param writer where should be write csv information
     * @see Writer
     */
    public CSVWriter(Writer writer) {
        this.writer = writer;
    }


    /**
     * Get values and write in one row all values toString
     * @param values which values should be write
     * @throws IOException when writer throw IOException
     * @see IOException
     * @see List
     * @see Object#toString()
     */
    public void write(List<Object> values) throws IOException {
        for (Object o : values) {
            String result = o.toString();
            if (Arrays.stream(reserved).anyMatch(result::contains)) {
                result = "\"" + result.replaceAll("\"", "\"\"") + "\"";
            }
            writer.write(result);
            writer.write(",");
        }
        writer.write("\n");
    }

    /**
     * Flushes the stream.  If the stream has saved any characters from the
     * various write() methods in a buffer, write them immediately to their
     * intended destination.  Then, if that destination is another character or
     * byte stream, flush it.  Thus one flush() invocation will flush all the
     * buffers in a chain of Writers and OutputStreams.
     * <p>
     * <p> If the intended destination of this stream is an abstraction provided
     * by the underlying operating system, for example a file, then flushing the
     * stream guarantees only that bytes previously written to the stream are
     * passed to the operating system for writing; it does not guarantee that
     * they are actually written to a physical device such as a disk drive.
     *
     * @throws IOException when writer throw IOException
     * @see IOException
     * @see Writer
     */
    public void flush() throws IOException {
        this.writer.flush();
    }

    /**
     * Closes the stream, flushing it first. Once the stream has been closed,
     * further write() or flush() invocations will cause an IOException to be
     * thrown. Closing a previously closed stream has no effect.
     *
     * @throws IOException when writer throw IOException
     * @see IOException
     * @see Writer
     */
    public void close() throws IOException {
        this.writer.close();
    }


}
