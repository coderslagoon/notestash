package com.coderslagoon.tools.notestash;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.crypto.SecretKey;

import org.apache.commons.lang3.ArrayUtils;

public class Main {

    private static String LOG_CONFIG = """
handlers= java.util.logging.ConsoleHandler
.level= DEBUG
java.util.logging.ConsoleHandler.level = DEBUG
java.util.logging.ConsoleHandler.formatter = java.util.logging.SimpleFormatter
java.util.logging.SimpleFormatter.format=%1$tF %1$tT [%4$s] %5$s %n
""";

    public static void main(String[] args) throws Exception {

        if (args.length < 3) {
            System.err.println("usage: notestash [target] [server] [folder] [username] {keystore-password}");
            System.exit(1);
            return;
        }

        if (args.length < 5) {
            System.out.printf("keystore-password > ");
            char[] password = System.console().readPassword();
            args = ArrayUtils.add(args, new String(password));
        }

        String target = args[0];
        String server = args[1];
        String folder = args[2];
        String username = args[3];
        String password = Main.loadPassword(username, args[4].toCharArray());

        LogManager.getLogManager().readConfiguration(new ByteArrayInputStream(LOG_CONFIG.getBytes()));
        Logger logger = Logger.getLogger("main");

        Stash stash = target.endsWith(".zip") ?
            new ZipStash(new File(target), true, true) :
            new FileStash(new File(target));

        try {
            new Note(server, folder, username, password).stash(stash, logger);
        } finally {
            stash.finish();
        }
    }

    protected static String loadPassword(String username, char[] keystorePassword) throws Exception {
        KeyStore keystore = KeyStore.getInstance("PKCS12");
        String keystorePath = System.getenv().get("NOTESTASH_KEYSTORE_PATH");
        if (keystorePath == null) {
            keystorePath = "." + File.separator + "notestash.p12";
        }
        keystore.load(new FileInputStream(keystorePath), keystorePassword);
        SecretKey key = (SecretKey)keystore.getKey(username, keystorePassword);
        return new String(key.getEncoded());
    }
}
