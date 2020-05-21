package iris.playharmony.util;

import java.io.*;

public class FileUtils {

    public static void readFileBinary(File file, InputStreamConsumer consumer) {

        try (FileInputStream inputStream = new FileInputStream(file)) {

            consumer.consume(inputStream);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File writeToFile(File file, InputStream inputStream) {

        try(FileOutputStream outputStream = new FileOutputStream(file)) {

            byte[] buffer = new byte[1024];

            while(inputStream.read(buffer) > 0) {
                outputStream.write(buffer);
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            closeInputStream(inputStream);
        }

        return file;
    }

    public static File writeToTemporalFile(InputStream inputStream) {

        File file = createTmpFile();

        return writeToFile(file, inputStream);
    }

    private static void closeInputStream(InputStream inputStream) {
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static File createTmpFile() {
        try {
            return File.createTempFile("tmp", ".tmp");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private FileUtils() {}

}
