import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ThreadPoolExecutor;

public class OneDimensionalSketchCon extends Sketch {
    HashMap<Integer, long[]> con;

    OneDimensionalSketchCon(String _name,
                         String _activeCol,
                         String[] _attrs,
                         String _dbName,
                         int[] _hashSizes,
                         String method,
                         Relation r,
                         ThreadPoolExecutor executor){
        super(_name, _activeCol, _attrs, _dbName, _hashSizes);

        this.con = new HashMap<>();
        this.con.put(0, new long[this.hashSizes[0]]);

        if(method.equals("deserialize")){
            this.deserialize(r, executor);
        }
        else {
            this.populateParallel(r, executor);
        }
    }

    public void populateParallel(Relation r,
                                 ThreadPoolExecutor executor){
        String sql1d1 =
                "SELECT\n" +
                        "    t.ha AS ha,\n" +
                        "    MAX(t.cnt) AS max_degree\n" +
                        "    FROM(\n" +
                        "        SELECT\n" +
                        "            abs(hash_string(%s::text, 'murmur3', 0)%%%d) AS ha,\n" +
                        "            %s AS x,\n" +
                        "            COUNT(*) AS cnt\n" +
                        "        FROM %s AS %s\n";
        String sql1d2 =
                "        GROUP BY ha, x) AS t\n" +
                        "GROUP BY ha\n" +
                        "ORDER BY ha;";

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC driver missing! Include in classpath.");
            e.printStackTrace();
            return;
        }

        StringBuilder queryBuilder;
        Filter f;

        queryBuilder = new StringBuilder(String.format(
                sql1d1,
                this.attrs[0], this.hashSizes[0],
                this.attrs[0],
                r.tableName, r.alias
        ));

        if(r.filters.size() > 0){
            queryBuilder.append("        WHERE\n            ");
            for (int i = 0; i < r.filters.size(); i++) {
                f = r.filters.get(i);
                if (i > 0) {
                    queryBuilder.append("\n            AND ");
                }
                queryBuilder.append(f.getPredicate());
            }
        }

        queryBuilder.append("\n");
        queryBuilder.append(sql1d2);

        String q0 = queryBuilder.toString();


        Populator p0 = new Populator(
                q0,
                r,
                this.dbName,
                this,
                0);

        executor.execute(p0);
    }

    public void printGrid(){
        System.out.println(Arrays.toString(this.con.get(this.g2lIndex.get(3).get(1))));
    }

    private void deserialize(Relation r,
                             ThreadPoolExecutor executor){
        if(r == null){
            this.populateParallel(r, executor);
        }
        else if(r.filters.size() > 0){
            this.populateParallel(r, executor);
        }
        else {
            try {
//                System.out.println(Arrays.toString(this.attrs));
                if(this.activeCol == null) {
                    this.path = String.format("/Users/leshanchen/James/CS/lab-work/2024.12_cardinality_estimation/datasets/pqo-run/BoundSketch/%s_sketches/1d%s_[%s]_[%d]_[%s].txt",
                            this.dbName, this.name, null, this.hashSizes[0],
                            this.attrs[0].split("\\.")[1]);
                }
                else{
                    this.path = String.format("/Users/leshanchen/James/CS/lab-work/2024.12_cardinality_estimation/datasets/pqo-run/BoundSketch/%s_sketches/1d%s_[%s]_[%d]_[%s].txt",
                            this.dbName, this.name, this.activeCol.split("\\.")[1], this.hashSizes[0],
                            this.attrs[0].split("\\.")[1]);
                }
                // FileReader reads text files in the default encoding.
                FileReader fileReader = new FileReader(this.path);

                // Always wrap FileReader in BufferedReader.
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    String[] lineSplit = line.split(",");
                    int i = Integer.parseInt(lineSplit[0]);

                    this.con.get(0)[i] = Integer.parseInt(lineSplit[1]);
                }
                bufferedReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("Unable to open file '" + this.path + "'");
                this.populateParallel(r, executor);
            } catch (IOException e) {
                System.out.println("Error reading file '" + this.path + "'");
                this.populateParallel(r, executor);
            }
        }
    }

    @Override
    public void quickDescribe(){
        System.out.println(this.name);
        System.out.println(Arrays.toString(this.attrs));
        System.out.println(Arrays.toString(this.hashSizes));
    }

    @Override
    public long access(int boundId, int gVarIndex, int[] arr) {
        try {
            return this.con.get(this.g2lIndex.get(boundId).get(gVarIndex))[arr[this.l2gIndex.get(boundId).get(0)]];
        }
        catch(Exception e){
            System.out.println("relation name: " + this.name);
            System.out.println("boundId: " + boundId);
            System.out.println("hash Sizes: " + Arrays.toString(this.hashSizes));
            System.out.println("gVarIndex: " + gVarIndex);
            System.out.println("arr: " + Arrays.toString(arr));
            System.out.println("l2g: " + this.l2gIndex.get(boundId).toString());
            System.out.println("g2l: " + this.g2lIndex.get(boundId).toString());
            this.printGrid();
            e.printStackTrace();
            System.exit(-1);
        }
        return 0;
    }

    @Override
    public void serialize(Relation r){
        if(r != null) {
            if(r.filters.size() == 0) {
                try {
                    if(this.activeCol == null){
                        this.path = String.format("/Users/leshanchen/James/CS/lab-work/2024.12_cardinality_estimation/datasets/pqo-run/BoundSketch/%s_sketches/1d%s_[%s]_[%d]_[%s].txt",
                                this.dbName, this.name, null, this.hashSizes[0],
                                this.attrs[0].split("\\.")[1]);
                    }
                    else{
                        this.path = String.format("/Users/leshanchen/James/CS/lab-work/2024.12_cardinality_estimation/datasets/pqo-run/BoundSketch/%s_sketches/1d%s_[%s]_[%d]_[%s].txt",
                                this.dbName, this.name, this.activeCol.split("\\.")[1], this.hashSizes[0],
                                this.attrs[0].split("\\.")[1]);
                    }
                    System.out.println("serializing:        " + this.path);
                    PrintWriter pr = new PrintWriter(this.path);

                    for (int i = 0; i < this.hashSizes[0]; i++) {
                        pr.println(String.format("%d,%d", i,
                                this.con.get(0)[i]));
                    }
                    pr.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error During Serialization.");
                }
            }
        }
        else{
            try {
                if(this.activeCol == null){
                    this.path = String.format("/Users/leshanchen/James/CS/lab-work/2024.12_cardinality_estimation/datasets/pqo-run/BoundSketch/%s_sketches/1d%s_[%s]_[%d]_[%s].txt",
                            this.dbName, this.name, null, this.hashSizes[0],
                            this.attrs[0].split("\\.")[1]);
                }
                else{
                    this.path = String.format("/Users/leshanchen/James/CS/lab-work/2024.12_cardinality_estimation/datasets/pqo-run/BoundSketch/%s_sketches/1d%s_[%s]_[%d]_[%s].txt",
                            this.dbName, this.name, this.activeCol.split("\\.")[1], this.hashSizes[0],
                            this.attrs[0].split("\\.")[1]);
                }
                System.out.println("serializing:        " + this.path);
                PrintWriter pr = new PrintWriter(this.path);


                for (int i = 0; i < this.hashSizes[0]; i++) {
                    pr.println(String.format("%d,%d", i,
                            this.con.get(0)[i]));
                }
                pr.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error During Serialization.");
            }
        }
    }

}
