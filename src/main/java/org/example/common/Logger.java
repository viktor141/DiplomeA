package org.example.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Logger {
    private static Logger instance = null;
    private LogLevel loggingLevel = LogLevel.INFO;
    private List<String> logs = new ArrayList<>();

    public enum LogLevel {
        INFO,
        DEBUG,
        ERROR;
    }

    public static Logger getLogger() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void logging(String log, LogLevel level, Side side) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String date = dateFormat.format(new Date().getTime());
        String resultLogMessage = String.format("[%s] %s %s: %s", date, side.name(), level.name(), log);

        logs.add(resultLogMessage);

        if (loggingLevel.ordinal() >= level.ordinal()) {
            if(side.equals(Side.SERVER) && level.equals(LogLevel.INFO)) {
                System.out.println(resultLogMessage);
            }
            loggingIntoLogFile(side, resultLogMessage);
        }
    }

    public Logger setLogLevel(LogLevel loggingLevel) {
        this.loggingLevel = loggingLevel;
        return this;
    }

    private void loggingIntoLogFile(Side side, String message) {
        Path path = Paths.get("src/main/resources/" + side.name().toLowerCase() + "/file.log");

        try {

            File log = new File(path.toString());

            if (!log.exists())log.createNewFile();


            FileWriter fw = new FileWriter(log, true);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(message);
            bw.newLine();

            bw.close();
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

