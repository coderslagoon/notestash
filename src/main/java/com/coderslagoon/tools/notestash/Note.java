package com.coderslagoon.tools.notestash;

import javax.mail.*;
import java.util.logging.Logger;
import java.io.OutputStream;
import java.util.Properties;

class Imap {
    public final static int DEFAULT_PORT = 993;
}

public class Note {

    protected String host;
    protected String folderName;
    protected String username;
    protected String password;

    public Note(
        String host,
        String folderName,
        String username,
        String password
    ) {
        this.host = host;
        this.folderName = folderName;
        this.username = username;
        this.password = password;
    }

    public void stash(Stash stash, Logger logger) throws Exception
    {
        Properties props = new Properties();
        props.put("mail.imap.ssl.enable", "true");
        props.put("mail.imap.ssl.protocols", "TLSv1.2");

        Session session = Session.getDefaultInstance(props, null);

        Store store = session.getStore("imap");
        store.connect(this.host, Imap.DEFAULT_PORT, this.username, this.password);

        Folder folder = store.getFolder(this.folderName);
        folder.open(Folder.READ_ONLY);

        Message[] messages = folder.getMessages();

        for (Message message : messages) {

            String subject = message.getSubject();

            logger.info("reading message '" + subject + "' ...");

            String[] messageIds = message.getHeader("Message-Id");
            if (messageIds.length == 0) {
                throw new Exception("no message ID found for: " + subject);
            }

            byte[] data = message.getInputStream().readAllBytes();
            logger.info(data.length + " bytes (from: " + message.getFrom()[0] + "), stashing ...");

            try (OutputStream os = stash.add(messageIds[0] + " " + subject)) {
                message.writeTo(os);
            }
        }

        folder.close(false);
        store.close();

        logger.info(String.format("stashed %d message(s)", messages.length));
    }
}
