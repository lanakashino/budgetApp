package model;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public interface Saveable {

    public void save(String fileName) throws FileNotFoundException, UnsupportedEncodingException;

}
