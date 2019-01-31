import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static java.lang.System.exit;

class Parser {

    private final File f;
    private java.io.Reader inputStream;
    private BufferedReader inputReader;

    // Initialize
    Parser(File f)
    {
        this.f = f;
        reset_input_stream();
    }

    private void reset_input_stream()
    {

        try{
            this.inputStream = new InputStreamReader(new FileInputStream(this.f));
            this.inputReader = new BufferedReader(this.inputStream);
        }
        catch (FileNotFoundException e)
        {
            System.err.println("File not found");
        }

    }

    String get_next_line()
    {
        String line = null;
        try {
            line = inputReader.readLine();
        }
        catch (IOException e)
        {
            System.err.println("Error while reading file");
        }
        // Reset stream if it has reached the end
        if (line == null)
        {
            reset_input_stream();
            return null;
        }

        return line;

    }
}
