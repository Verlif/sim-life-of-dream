package idea.verlif.lifeofdream.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * @author Verlif
 */
public class FileUtil {

    public static String readFromFile(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8))) {
            char[] b = new char[1024];
            StringBuilder sb = new StringBuilder();
            int length;
            while ((length = reader.read(b)) > 0) {
                sb.append(b, 0, length);
            }
            return sb.toString();
        }
    }

}
