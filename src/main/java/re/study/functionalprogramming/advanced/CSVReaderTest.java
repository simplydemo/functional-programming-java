package re.study.functionalprogramming.advanced;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CSVReaderTest {

    private static Connection getConnection() throws SQLException, ClassNotFoundException {
        // Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.56.11:3306/SAMPLES", "aider",
                "aider1234");
        return connection;
    }

    protected void importFromFile() {
        try (Connection connection = getConnection()) {
            String loadQuery = "LOAD DATA LOCAL INFILE '" + "C:/upload.csv"
                    + "' INTO TABLE txn_tbl FIELDS TERMINATED BY ','"
                    + " LINES TERMINATED BY '\n' (txn_amount, card_number, terminal_id) ";
            System.out.println(loadQuery);
            Statement stmt = connection.createStatement();
            stmt.execute(loadQuery);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void closeQuitly(Connection conn, PreparedStatement pstmt) {
        try {
            pstmt.close();
        }
        catch (Exception e2) {

        }
        try {
            conn.close();
        }
        catch (Exception e3) {

        }
    }

    protected void importData(List<List<String>> values, final String sql) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            int rows = 0;
            for (List<String> list : values) {
                int index = 0;
                for (String v : list) {
                    pstmt.setString(index + 1, v);
                    ++index;
                }
                pstmt.addBatch();
                if (rows % 1999 == 0) {
                    pstmt.executeBatch();
                }
                ++rows;
            }
            pstmt.executeBatch();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            closeQuitly(conn, pstmt);
        }
    }

    @Test
    public void testCSVReader() throws Exception {
        final String filename = "/samples.csv";
        File f = new File(CSVReaderTest.class.getResource(filename).toURI());
        log.info("f.exists(): {}", f.exists());
        List<List<String>> values = CSVReader.toList(f.toPath(), ",");
        log.info("values.size(): {}", values.size());
    }

    @Test
    public void test_importFcstTrainningData() throws Exception {
        final String filename = "C:/Users/seonbo.shim/Documents/Working/AWS-ML-TRAINNING/FCST_SELLOUT_DATA.csv";
        LocalDateTime localDtm = (LocalDateTime) DateMaps.ofNow().get();
        File f = new File(filename);
        List<List<String>> values = CSVReader.toList(f.toPath());

        String sql = "insert into IR_FCST_DATA (GC,AP2,AP1,ACCOUNT,MODEL,WEEKNUM,SELLOUT) values " + "(?,?,?,?,?,?,?)";
        importData(values, sql);
        log.info("duration SECS: {}", DateMaps.of(localDtm).duration().getSeconds());

    }

    @Test
    public void test_importFcstPromotionData() throws Exception {
        final String filename = "C:/Users/seonbo.shim/Documents/Working/AWS-ML-TRAINNING/FCST_PROMOTION_DATA.csv";
        LocalDateTime localDtm = (LocalDateTime) DateMaps.ofNow().get();
        File f = new File(filename);
        List<List<String>> values = CSVReader.toList(f.toPath());
        String sql = "insert into IR_PROMOTION (ACCOUNT,MODEL,WEEKNUM,PTYPE,VALUE) values " + "(?,?,?,?,?)";
        importData(values, sql);
        log.info("duration SECS: {}", DateMaps.of(localDtm).duration().getSeconds());
    }

    @Test
    public void test_importFcstMobileTrainningData() throws Exception {
        final String filename = "C:/Users/seonbo.shim/Documents/Working/AWS-ML-TRAINNING/FCST_MOBILE_SELLOUT.csv";
        File f = new File(filename);
        List<List<String>> values = CSVReader.toList(f.toPath());
        String sql = "insert into IR_FCST_MOBILE (GC,AP2,AP1,ACCOUNT,MODEL,WEEKNUM,SELLOUT) values "
                + "(?,?,?,?,?,?,?)";
        importData(values, sql);
        LocalDateTime localDtm = (LocalDateTime) DateMaps.ofNow().get();
        log.info("duration SECS: {}", DateMaps.of(localDtm).duration().getSeconds());

    }

    protected void importArrayData(List<List<String[]>> values, final String sql) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            int rows = 0;
            for (List<String[]> list : values) {
                for (String[] arr : list) {
                    for (int i = 0; i < arr.length; i++) {
                        pstmt.setString(i + 1, arr[i]);
                    }
                    pstmt.addBatch();
                }
                if (rows % 1999 == 0) {
                    pstmt.executeBatch();
                }
                ++rows;
            }
            pstmt.executeBatch();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            closeQuitly(conn, pstmt);
        }
    }

    /**
     * @param keys 사전에정의된 HeaderNames
     * @return List<Arrays> 데이터
     */
    private final Function<String[], List<String[]>> buildListMapFunc(final String[] keys) {
        return v -> {
            final List<String[]> list = new ArrayList<>();
            final String account = v[0];
            final String mapping = v[1];
            final String ptype = v[2];
            for (int i = 3; i < v.length; i++) {
                String[] data = new String[5];
                data[0] = account;
                data[1] = mapping;
                data[2] = ptype;
                data[3] = keys[i]; // week
                data[4] = v[i]; // value of week
                // String c = Arrays.toString(data);
                // System.out.printf("\n %d ==> %s ", i, c);
                list.add(data);
            }
            return list;
        };
    };

    @Test
    public void ut1002_Map() throws Exception {
        final String filename = "/samples-2.csv";
        File f = new File(CSVReaderTest.class.getResource(filename).toURI());
        log.info("f.exists(): {}", f.exists());
        List<List<String[]>> values = CSVReader.toListWithFunction(f.toPath(), ",");
        log.info("values.size(): {}", values.size());
        for (List<String[]> rows : values) {
            for (String[] arrs : rows) {
                System.out.println(Arrays.toString(arrs));
            }
        }
    }

    @Test
    public void ut1003_importIRPromotion() throws Exception {
        final String filepath = "C:/Users/seonbo.shim/Documents/Working/FCST AWS-ML PoC/Promotion 20170822.csv";
        final Path path = new File(filepath).toPath();
        System.out.println("path.toFile().exists(): " + path.toFile().exists());
        final String[] keys = Files.lines(path).findFirst().map(s -> s.split(",")).get();
        List<List<String[]>> values = CSVReader.toListWithFunction(path, ",", buildListMapFunc(keys));
        // log.info("values.size(): {}", values.size());
        String sql = "insert into IR_PROMOTION (ACCOUNT,MODEL,PTYPE,WEEKNO,VALUE) values " + "(?,?,?,?,?)";
        LocalDateTime localDtm = (LocalDateTime) DateMaps.ofNow().get();
        importArrayData(values, sql);
        log.info("duration SECS: {}", DateMaps.of(localDtm).duration().getSeconds());

    }

}
