package br.com.github.puti.engine.socket.protocol.files;

import br.com.github.puti.engine.socket.protocol.edit.FileKey;

import java.io.*;
import java.util.*;

public class FileEngine {

    private FileType fileType;

    public FileEngine(FileType fileType) {
        this.fileType = fileType;
    }

    public void readDirectory() {
        for (FileType value : FileType.values()) {
            File file = new File(value.getDirectory());
            if (!file.exists())
                file.mkdirs();
        }
    }

    public File[] checkout(String fileName) {
        File directory = new File(fileType.getDirectory());
        List<File> files = new ArrayList<>();

        for (File file : directory.listFiles()) {
            if (file.getName().equals(fileName))
                files.add(file);
        }

        return files.toArray(new File[]{});
    }

    public void write(File file, FileKey messages) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("{\n");

            HashMap<String, String> values = messages.clone();
            values.forEach((key, value) -> {
                try {
                    writer.write("\"" + key + "\":\"" + value + "\"" + (messages.hasSeparator() ? "," : "") + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                messages.values.remove(key, value);
            });

            writer.write("}\n");

            writer.flush();
            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public File[] create(String... fileNames) {
        Set<File> files = new HashSet<>();
        for (String fileName : fileNames) {
            try {
                File file = new File(fileType.toDirectory(fileName));
                if (!file.exists())
                    file.createNewFile();

                files.add(file);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return files.toArray(new File[]{});
    }

    public HashMap<String, String> readAll(File file) {
        HashMap<String, String> values = new HashMap<>();
        try (FileReader reader = new FileReader(file)) {
            BufferedReader bufferedReader = new BufferedReader(reader);


            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.indexOf(":") > 0) {
                    String[] parts = line.split(":");
                    String value1 = parts[0].replaceAll("\"", "");
                    String value2 = parts[1].replaceAll("\"", "").replaceAll(",", "");

                    values.put(value1, value2);
                }
            }
            bufferedReader.close();
            reader.close();
        } catch (Exception ex) {
        }

        return values;
    }


    public enum FileType {
        PLAYER("players"), FILE("files"), DOCUMENT("documents"), TEMP("archiveTemps");

        private String directory = "C:";

        FileType(String directory) {
            this.directory = System.getProperty("user.dir") + "/" + directory + "-cache";
        }

        public String getDirectory() {
            return directory;
        }

        public String setDirectory(String directory) {
            File file = new File(this.directory + directory);
            if (!(file.exists()))
                try {
                    file.mkdirs();
                }catch (Exception ignored){}

            return this.directory + directory;
        }

        public String toDirectory(String file) {
            return this.directory + "/" + file;
        }
    }

}
