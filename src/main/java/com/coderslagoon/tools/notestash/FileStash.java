package com.coderslagoon.tools.notestash;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileStash extends FileLikeStash {

    protected File dir;

    public FileStash(File dir) throws IOException {
        this.dir = dir;
        Files.createDirectories(Paths.get(this.dir.toString()));
    }

    public void finish() {
    }

    @Override
    protected OutputStream addFile(String fileName) throws IOException {
        String filePath = new File(this.dir, fileName).toString();
        return new FileOutputStream(filePath);
    }
}
