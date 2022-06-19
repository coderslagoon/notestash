# Intro

Simple command line tool written in Java to archive all notes of an IMAP backed account into a ZIP file.

## Building

Happens with Maven:
```
mvn install
mvn package
```

## Running

On the command line:
```
java -jar target/notestash-1.0-SNAPSHOT-jar-with-dependencies.jar <ZIP-FILE-PATH> <IMAP-SERVER> <IMAP-FOLDER> <IMAP-EMAIL> {KEYSTORE-PASSWORD}
```

The IMAP folder is usually called _Notes_. The keystore password is optional (will be prompted if not given).

## Keystore

The account credentials are kept in a key store with the name _notestash.p12_. To create it:
```
keytool -importpass -storetype pkcs12 -alias <IMAP-EMAIL> -keystore notestash.p12
```

To view it:
```
keytool -list -v -keystore notestash.p12
```
