package com.coderslagoon.tools.notestash;

import java.io.OutputStream;

public interface Stash {

    public OutputStream add(String name) throws Exception;
    public void finish() throws Exception;
}
