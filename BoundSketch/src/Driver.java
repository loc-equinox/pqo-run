import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.sql.ResultSet;
import java.util.*;
import java.io.PrintWriter;

import org.paukov.combinatorics3.Generator;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

public class Driver {
    private String[] candidates;
    private int[] buckets;

    public Driver(){}

    private void drive() {
        this.buckets = new int[]{
                4096,
//                512,
//                64,
//                8,
//                1
        };
        this.candidates = new String[]{
          "02a.sql",
            "02b.sql",
            "02c.sql",
            "02d.sql",
            "03a.sql",
            "03b.sql",
            "03c.sql",
            "05a.sql",
            "05b.sql",
            "05c.sql",
            "06a.sql",
            "06b.sql",
            "06c.sql",
            "06d.sql",
            "06e.sql",
            "06f.sql",
            "07a.sql",
            "07b.sql",
            "07c.sql",
            "08a.sql",
            "08b.sql",
            "08c.sql",
            "08d.sql",
            "09a.sql",
            "09b.sql",
            "09c.sql",
            "09d.sql",
            "10a.sql",
            "10b.sql",
            "10c.sql",
            "11a.sql",
            "11b.sql",
            "11c.sql",
            "11d.sql",
            "15a.sql",
            "15b.sql",
            "15c.sql",
            "15d.sql",
            "16a.sql",
            "16b.sql",
            "16c.sql",
            "16d.sql",
            "17a.sql",
            "17b.sql",
            "17c.sql",
            "17d.sql",
            "17e.sql",
            "17f.sql",
            "19a.sql",
            "19b.sql",
            "19c.sql",
            "19d.sql",
            "20a.sql",
            "20b.sql",
            "20c.sql",
            "21a.sql",
            "21b.sql",
            "21c.sql",
            "23a.sql",
            "23b.sql",
            "23c.sql",
            "24a.sql",
            "24b.sql",
            "27a.sql",
            "27b.sql",
            "27c.sql",
            "29a.sql",
            "29b.sql",
            "29c.sql",
            "32a.sql",
            "32b.sql"
        };


        this.getIMDBBoundPerformance();
        this.getIMDBDefaultPerformance();
    }

    private void getIMDBBoundPerformance(){
        String cmd;
        String dbName = "imdb";

        String dir = "/Users/leshanchen/James/CS/lab-work/pqo-opensource/join-order-benchmark/";
        java.sql.ResultSet rs;
        long t, start, stop;
        double boundAvgSeconds;

        int repetitions = 5;

        try {
            for(int b: this.buckets) {
                for (String candidate : this.candidates) {
                    System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                    System.out.println("CURRENT QUERY: " + candidate + ". CURRENT HASH SIZE: " + b);

                    PrintWriter logWriter = new PrintWriter(new FileOutputStream(
                            new File("/Users/leshanchen/James/CS/lab-work/pqo-opensource/output/log.txt"),
                            true));
                    logWriter.println("NEW QUERY: " + candidate +  "|CURRENT HASH SIZE: " + b);
                    logWriter.close();

                    cmd = new String(readAllBytes(get(dir + candidate)));

                    QueryGraph qg = new QueryGraph(dbName, candidate, b);
                    qg.setup(cmd);

//                    qg.process(new BoundIndexBuilder(0), new HashMap<>());
//                    qg.describe();
//                    qg.describeBoundFormulae();
//                    System.out.println(qg.getBound());

                    start = System.nanoTime();
                    qg.getSubgraphs();
                    stop = System.nanoTime();

                    PrintWriter sketchTimingWriter = new PrintWriter(new FileOutputStream(
                            new File("/Users/leshanchen/James/CS/lab-work/pqo-opensource/output/results/" + dbName + "/sketch_preprocessing_" + b + ".txt"),
                            true));
                    sketchTimingWriter.println((stop - start) / 1000000000.0);
                    sketchTimingWriter.close();

                    rs = this.executeQuery("EXPLAIN\n" + cmd, dbName);
                    while (rs.next())
                    {
                        System.out.println(rs.getString(1));
                    }

                    /* warm start the engine and get explanation */
                    PrintWriter rawBoundWriter = new PrintWriter(new FileOutputStream(
                            new File("/Users/leshanchen/James/CS/lab-work/pqo-opensource/output/raw/" + dbName + "/bound_" + b + ".txt"),
                            true));
                    rawBoundWriter.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                    rawBoundWriter.println("CURRENT QUERY: " + candidate);
                    rawBoundWriter.println(cmd);
                    rs = this.executeQuery("EXPLAIN ANALYZE\n" + cmd, dbName);

                    if(rs == null){
                        rawBoundWriter.println("TIME LIMIT REACHED");
                    }
                    else {
                        while (rs.next()) {
                            rawBoundWriter.println(rs.getString(1));
                        }
                    }
                    rawBoundWriter.close();

                    boolean timeOut = false;
                    t = System.currentTimeMillis();
                    for (int j = 0; j < repetitions; j++) {
                        rs = this.executeQuery(cmd, dbName);
                        if(rs == null){
                            timeOut = true;
                            break;
                        }
                    }
                    if(!timeOut){
                        boundAvgSeconds = (System.currentTimeMillis() - t) / 1000.0 / repetitions;
                    }
                    else{
                        boundAvgSeconds = 60.0 * 60.0;
                    }

                    PrintWriter planTimerWriter = new PrintWriter(new FileOutputStream(
                            new File("/Users/leshanchen/James/CS/lab-work/pqo-opensource/output/results/" + dbName + "/plan_execution_time_" + b + ".txt"),
                            true));
                    planTimerWriter.println(boundAvgSeconds);
                    planTimerWriter.close();

                    System.out.println(String.format(
                            "BOUND RUNTIME (seconds): %f",
                            boundAvgSeconds));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void getIMDBSketchPreprocessingTime() {
        String cmd;
        String dbName = "imdb";

        String dir = "/Users/leshanchen/James/CS/lab-work/pqo-opensource/join-order-benchmark/";
        Timer timer;

        try {
            for (int b : this.buckets) {
                for (String candidate : this.candidates) {
                    cmd = new String(readAllBytes(get(dir + candidate)));

                    QueryGraph qg = new QueryGraph(dbName, candidate, b);
                    qg.setup(cmd);

                    System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                    System.out.println("CURRENT QUERY: " + candidate + ". CURRENT HASH SIZE: " + b);

                    timer = new Timer();
//                    qg.getSubgraphsTiming(timer);
                    qg.getSubgraphs();
                    timer.fullLog();
                    timer.report();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void getIMDBDefaultPerformance(){
        String cmd;

        String dbName = "imdb";
        String dir = "/Users/leshanchen/James/CS/lab-work/pqo-opensource/join-order-benchmark/";
        java.sql.ResultSet rs;
        long t;
        double defaultAvgSeconds;

        int repetitions = 5;

        try {
            /* clear /Users/leshanchen/James/CS/lab-work/pqo-opensource/output/info.txt */
            PrintWriter infoWriter = new PrintWriter("/Users/leshanchen/James/CS/lab-work/pqo-opensource/output/info.txt");
            infoWriter.close();

            for (String candidate : this.candidates) {
                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                System.out.println("CURRENT QUERY: " + candidate + ". DEFAULT.");

                cmd = new String(readAllBytes(get(dir + candidate)));

                rs = this.executeQuery("EXPLAIN\n" + cmd, dbName);
                while (rs.next()) {
                    System.out.println(rs.getString(1));
                }

                /* warm start the engine and get explanation */
                PrintWriter rawDefaultWriter = new PrintWriter(new FileOutputStream(
                        new File("/Users/leshanchen/James/CS/lab-work/pqo-opensource/output/raw/" + dbName + "/default.txt"),
                        true));
                rawDefaultWriter.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                rawDefaultWriter.println("CURRENT QUERY: " + candidate);
                rawDefaultWriter.println(cmd);

                rs = this.executeQuery("EXPLAIN ANALYZE\n" + cmd, dbName);
                if(rs == null){
                    rawDefaultWriter.println("TIME LIMIT REACHED");
                    defaultAvgSeconds = 60.0 * 60.0;
                    PrintWriter resultWriter = new PrintWriter(new FileOutputStream(
                            new File("/Users/leshanchen/James/CS/lab-work/pqo-opensource/output/results/" + dbName + "/default.txt"),
                            true));
                    resultWriter.println(defaultAvgSeconds);
                    resultWriter.close();

                    System.out.println(String.format(
                            "DEFAULT RUNTIME (seconds): %f",
                            defaultAvgSeconds));
                    rawDefaultWriter.close();
                    continue;
                }
                else {
                    while (rs.next()) {
                        rawDefaultWriter.println(rs.getString(1));
                    }
                }
                rawDefaultWriter.close();

                boolean timeOut = false;
                t = System.currentTimeMillis();
                for (int j = 0; j < repetitions; j++) {
                    rs = this.executeQuery(cmd, dbName);
                    if(rs == null){
                        timeOut = true;
                        break;
                    }
                }
                if(!timeOut){
                    defaultAvgSeconds = (System.currentTimeMillis() - t) / 1000.0 / repetitions;
                }
                else{
                    defaultAvgSeconds = 60.0 * 60.0;
                }

                PrintWriter resultWriter = new PrintWriter(new FileOutputStream(
                        new File("/Users/leshanchen/James/CS/lab-work/pqo-opensource/output/results/" + dbName + "/default.txt"),
                        true));
                resultWriter.println(defaultAvgSeconds);
                resultWriter.close();

                System.out.println(String.format(
                        "DEFAULT RUNTIME (seconds): %f",
                        defaultAvgSeconds));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void getGPBoundPerformance(){
        String cmd;
        String dbName = "gp";

        String dir = "googleplus/scripts/microbenchmark/";
        java.sql.ResultSet rs;
        long t, start, stop;
        double boundAvgSeconds;

        int repetitions = 5;

        File f = new File(dir);
        ArrayList<String> filesList = new ArrayList<>(Arrays.asList(f.list()));
        ArrayList<String> filesListPruned = new ArrayList<>();

        for (String s : filesList) {
            if (s.contains(".sql")) {
                filesListPruned.add(s);
            }
        }
        this.candidates = filesListPruned.toArray(new String[0]);
        Arrays.sort(this.candidates);

        try {
            for(int b: this.buckets) {
                for (String candidate : this.candidates) {
                    System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                    System.out.println("CURRENT QUERY: " + candidate + ". CURRENT HASH SIZE: " + b);

                    PrintWriter logWriter = new PrintWriter(new FileOutputStream(
                            new File("/Users/leshanchen/James/CS/lab-work/pqo-opensource/output/log.txt"),
                            true));
                    logWriter.println("NEW QUERY: " + candidate +  "|CURRENT HASH SIZE: " + b);
                    logWriter.close();

                    cmd = new String(readAllBytes(get(dir + candidate)));

                    QueryGraph qg = new QueryGraph(dbName, candidate, b);
                    qg.setup(cmd);

//                    qg.process(new BoundIndexBuilder(0), new HashMap<>());
//                    qg.describe();
//                    qg.describeBoundFormulae();
//                    System.out.println(qg.getBound());

                    start = System.nanoTime();
                    qg.getSubgraphs();
                    stop = System.nanoTime();

                    PrintWriter sketchTimingWriter = new PrintWriter(new FileOutputStream(
                            new File("/Users/leshanchen/James/CS/lab-work/pqo-opensource/output/results/" + dbName + "/sketch_preprocessing_" + b + ".txt"),
                            true));
                    sketchTimingWriter.println((stop - start) / 1000000000.0);
                    sketchTimingWriter.close();

                    rs = this.executeQuery("EXPLAIN\n" + cmd, dbName);
                    while (rs.next())
                    {
                        System.out.println(rs.getString(1));
                    }

                    /* warm start the engine and get explanation */
                    PrintWriter rawBoundWriter = new PrintWriter(new FileOutputStream(
                            new File("/Users/leshanchen/James/CS/lab-work/pqo-opensource/output/raw/" + dbName + "/bound_" + b + ".txt"),
                            true));
                    rawBoundWriter.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                    rawBoundWriter.println("CURRENT QUERY: " + candidate);
                    rawBoundWriter.println(cmd);
                    rs = this.executeQuery("EXPLAIN ANALYZE\n" + cmd, dbName);

                    if(rs == null){
                        rawBoundWriter.println("TIME LIMIT REACHED");
                        boundAvgSeconds = 60.0 * 60.0;
                        PrintWriter planTimerWriter = new PrintWriter(new FileOutputStream(
                                new File("/Users/leshanchen/James/CS/lab-work/pqo-opensource/output/results/" + dbName + "/plan_execution_time_" + b + ".txt"),
                                true));
                        planTimerWriter.println(boundAvgSeconds);
                        planTimerWriter.close();

                        System.out.println(String.format(
                                "BOUND RUNTIME (seconds): %f",
                                boundAvgSeconds));
                        rawBoundWriter.close();
                        continue;
                    }
                    else {
                        while (rs.next()) {
                            rawBoundWriter.println(rs.getString(1));
                        }
                    }
                    rawBoundWriter.close();

                    if (repetitions > 0) {
                        boolean timeOut = false;
                        t = System.currentTimeMillis();
                        for (int j = 0; j < repetitions; j++) {
                            rs = this.executeQuery(cmd, dbName);
                            if (rs == null) {
                                timeOut = true;
                                break;
                            }
                        }
                        if (!timeOut) {
                            boundAvgSeconds = (System.currentTimeMillis() - t) / 1000.0 / repetitions;
                        } else {
                            boundAvgSeconds = 60.0 * 60.0;
                        }

                        PrintWriter planTimerWriter = new PrintWriter(new FileOutputStream(
                                new File("/Users/leshanchen/James/CS/lab-work/pqo-opensource/output/results/" + dbName + "/plan_execution_time_" + b + ".txt"),
                                true));
                        planTimerWriter.println(boundAvgSeconds);
                        planTimerWriter.close();

                        System.out.println(String.format(
                                "BOUND RUNTIME (seconds): %f",
                                boundAvgSeconds));
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void getGPDefaultPerformance(){
        String cmd;

        String dbName = "gp";
        String dir = "googleplus/scripts/microbenchmark/";
        java.sql.ResultSet rs;
        long t;
        double defaultAvgSeconds;

        int repetitions = 5;

        File f = new File(dir);
        ArrayList<String> filesList = new ArrayList<>(Arrays.asList(f.list()));
        ArrayList<String> filesListPruned = new ArrayList<>();

        for (String s : filesList) {
            if (s.contains(".sql")) {
                filesListPruned.add(s);
            }
        }
        this.candidates = filesListPruned.toArray(new String[0]);
        Arrays.sort(this.candidates);

        try {
            /* clear /Users/leshanchen/James/CS/lab-work/pqo-opensource/output/info.txt */
            PrintWriter infoWriter = new PrintWriter("/Users/leshanchen/James/CS/lab-work/pqo-opensource/output/info.txt");
            infoWriter.close();

            for (String candidate : this.candidates) {
                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                System.out.println("CURRENT QUERY: " + candidate + ". DEFAULT.");

                cmd = new String(readAllBytes(get(dir + candidate)));

                rs = this.executeQuery("EXPLAIN\n" + cmd, dbName);
                while (rs.next()) {
                    System.out.println(rs.getString(1));
                }

                /* warm start the engine and get explanation */
                PrintWriter rawDefaultWriter = new PrintWriter(new FileOutputStream(
                        new File("/Users/leshanchen/James/CS/lab-work/pqo-opensource/output/raw/" + dbName + "/default.txt"),
                        true));
                rawDefaultWriter.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                rawDefaultWriter.println("CURRENT QUERY: " + candidate);
                rawDefaultWriter.println(cmd);

                rs = this.executeQuery("EXPLAIN ANALYZE\n" + cmd, dbName);
                if(rs == null){
                    rawDefaultWriter.println("TIME LIMIT REACHED");
                    defaultAvgSeconds = 60.0 * 60.0;
                    PrintWriter resultWriter = new PrintWriter(new FileOutputStream(
                            new File("/Users/leshanchen/James/CS/lab-work/pqo-opensource/output/results/" + dbName + "/default.txt"),
                            true));
                    resultWriter.println(defaultAvgSeconds);
                    resultWriter.close();

                    System.out.println(String.format(
                            "DEFAULT RUNTIME (seconds): %f",
                            defaultAvgSeconds));
                    rawDefaultWriter.close();
                    continue;
                }
                else {
                    while (rs.next()) {
                        rawDefaultWriter.println(rs.getString(1));
                    }
                }
                rawDefaultWriter.close();

                if (repetitions > 0) {

                    boolean timeOut = false;
                    t = System.currentTimeMillis();
                    for (int j = 0; j < repetitions; j++) {
                        rs = this.executeQuery(cmd, dbName);
                        if(rs == null){
                            timeOut = true;
                            break;
                        }
                    }
                    if(!timeOut){
                        defaultAvgSeconds = (System.currentTimeMillis() - t) / 1000.0 / repetitions;
                    }
                    else{
                        defaultAvgSeconds = 60.0 * 60.0;
                    }

                    PrintWriter resultWriter = new PrintWriter(new FileOutputStream(
                            new File("/Users/leshanchen/James/CS/lab-work/pqo-opensource/output/results/" + dbName + "/default.txt"),
                            true));
                    resultWriter.println(defaultAvgSeconds);
                    resultWriter.close();

                    System.out.println(String.format(
                            "DEFAULT RUNTIME (seconds): %f",
                            defaultAvgSeconds));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private java.sql.ResultSet executeQuery(String command, String dbName){
        try {
            Connection conn;
            conn = DriverManager.getConnection(
                    String.format("jdbc:postgresql://127.0.0.1:5432/%s", dbName),
                    "leshanchen",
                    "");
            Statement st;
            java.sql.ResultSet rs;

            PreparedStatement ps = conn.prepareStatement(command);
            ps.setQueryTimeout(60 * 60);
            rs = ps.executeQuery();
            conn.close();
            return rs;
        }
        catch (org.postgresql.util.PSQLException e) {
            System.out.println("QUERY TIMEOUT");
            return null;
        }
        catch (SQLException e) {
            System.out.println("Connection/Querying Failed!");
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String argv[]) {
        Driver d = new Driver();
        d.drive();
    }
}
