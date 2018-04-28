
/**
 *
 * @author vitor silva
 */
import java.io.*;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        File file;
        FileReader stream;
        FileWriter stream_out;
        int numChRead;

        if (args.length != 1) {
            System.out.println("Use only one parameter, the file to be compiled");
        } else {
            file = new File(args[0]);
            if (!file.canRead()) {
                System.out.println("The file " + args[0] + " cannot be read");
                throw new RuntimeException();
            }
            if (!file.exists()) {
                System.out.println("The file " + args[0] + " cannot be exist");
                throw new RuntimeException();
            }
            try {
                stream = new FileReader(file);
                stream_out = new FileWriter(stripExtension(file.getName()) + ".c");
            } catch (FileNotFoundException e) {
                System.out.println("Something wrong: file does not exist anymore");
                throw new RuntimeException();
            }
            // one more character for '\0' at the end that will be added by the
            // compiler
            char[] input = new char[(int) file.length() + 1];

            try {
                numChRead = stream.read(input, 0, (int) file.length());
            } catch (IOException e) {
                System.out.println("Error reading file " + args[0]);
                throw new RuntimeException();
            }

//            if ( numChRead != file.length() ) {
//                System.out.println("Read error");
//                throw new RuntimeException();
//            }
            try {
                stream.close();
            } catch (IOException e) {
                System.out.println("Error in handling the file " + args[0]);
                throw new RuntimeException();
            }

            Compiler compiler = new Compiler();
            compiler.compile(input, stream_out);
            stream_out.close();

        }
    }

    static String stripExtension(String str) {
        // Handle null case specially.

        if (str == null) {
            return null;
        }

        // Get position of last '.'.
        int pos = str.lastIndexOf(".");

        // If there wasn't any '.' just return the string as is.
        if (pos == -1) {
            return str;
        }

        // Otherwise return the string, up to the dot.
        return str.substring(0, pos);
    }

}
