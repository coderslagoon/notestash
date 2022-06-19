package com.coderslagoon.tools.notestash;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public abstract class FileLikeStash implements Stash {

    protected final static String EXT = ".eml";
    protected final static int MAX_NAME_LENGTH = 224 - EXT.length();

    public OutputStream add(String name) throws IOException {
        name = URLEncoder.encode(name, StandardCharsets.UTF_8);
        if (name.length() > MAX_NAME_LENGTH) {
            name = name.substring(0, MAX_NAME_LENGTH);
        }
        String fileName = name + ".eml";
        return this.addFile(fileName);
    }

    protected abstract OutputStream addFile(String fileName) throws IOException;
}
