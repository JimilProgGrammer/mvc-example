package com.arch.mvc.utils;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class helps to read property file
 * that is hosted at a secure location in
 * the server.
 *
 * @author jimil
 */
public class PropertyFileReader {

    // contains path where all properties file related to this app are hosted
    private final String propFileLoc;

    private static Properties properties = null;
    private WatchService watchService = null;

    private PropertyFileReader() {

        final String propFileLoc = System.getProperty("propFileLoc");

        if ((propFileLoc != null) && propFileLoc.trim().length() > 0) {
            this.propFileLoc = propFileLoc;
        } else {
            this.propFileLoc = "/usr/local/src/properties/arjun";
        }

        System.out.println("loading properties from location: "
                + this.propFileLoc);

        loadProperties();
        configWatcher();

        final ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.execute(new Runnable() {
            public void run() {
                doRounds();
            }
        });
    }

    /**
     *
     * @param key
     *            property key
     * @return property value
     */
    public static String getValue(String key) {
        if (properties == null) {
            new PropertyFileReader();
        }

        return properties.getProperty(key);
    }

    // loading properties from property file
    private void loadProperties() {

        final File folder = new File(propFileLoc);
        final File[] listFiles = folder.listFiles();

        for (File file : listFiles) {
            final String propFile = file.getAbsolutePath();
            if ((file.isFile()) && (propFile.endsWith("properties"))) {

                try (FileInputStream fis = new FileInputStream(propFile);) {
                    final Properties prop = new Properties(properties);
                    prop.load(fis);
                    properties = new Properties(prop);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                    System.err.println("Error while loading properties: " + ex);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.err.println("Error while loading properties: " + ex);
                }
            }
        }
    }

    // if property file has been modified on fly, then update the values
    private void refreshProperty(String propFile) {
        propFile = propFileLoc + propFile;
        try (FileInputStream fis = new FileInputStream(propFile);) {

            final Properties prop = new Properties(properties);
            prop.load(fis);
            properties = new Properties(prop);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // configuring watcher to check if property files have been modified
    private void configWatcher() {
        try {
            watchService = FileSystems.getDefault().newWatchService();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            final Path path = Paths.get(propFileLoc);
            System.out.println("registering propFile: " + propFileLoc);

            path.register(watchService, ENTRY_MODIFY);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // checking for property file changes
    private void doRounds() {
        WatchKey key = null;
        System.out.println("Starting watcher");
        while (true) {

            try {
                key = watchService.take();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                final WatchEvent.Kind<?> kind = event.kind();

                if (kind == OVERFLOW) {
                    System.out.println("Watcher Overflow");
                    continue;
                }
                System.out.println("Event on " + event.context().toString()
                        + " is " + kind);
                refreshProperty(event.context().toString());
            }
            key.reset();
        }
    }

}
