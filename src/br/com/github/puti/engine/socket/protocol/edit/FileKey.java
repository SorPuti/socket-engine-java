package br.com.github.puti.engine.socket.protocol.edit;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FileKey {

    /*
    @Auhtor=SrPuti_
     */
    public HashMap<String, String> values = new HashMap<>();

    public FileKey() {

    }

    public FileKey(HashMap<String, String> values) {
        this.values = values;
    }


    public FileKey append(String key, Object value) {
        values.put(key, value + "");
        return this;
    }

    public FileKey append(String key, List<?> value) {
        values.put(key, Arrays.toString(value.toArray(new Object[]{})) + "");
        return this;
    }

    public HashMap<String, String> clone() {
        return new HashMap<>(this.values);
    }

    public boolean hasSeparator() {
        return values.size() > 1;
    }


}
