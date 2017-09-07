package re.study.functionalprogramming.advanced;

import java.util.List;

import java.io.File;
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
            String loadQuery = "LOAD DATA LOCAL INFILE '" + "C:\\upload.csv"
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
        LocalDateTime localDtm = (LocalDateTime) DateMaps.ofNow().get();
        File f = new File(filename);
        List<List<String>> values = CSVReader.toList(f.toPath());
        String sql = "insert into IR_FCST_MOBILE (GC,AP2,AP1,ACCOUNT,MODEL,WEEKNUM,SELLOUT) values "
                + "(?,?,?,?,?,?,?)";
        importData(values, sql);
        log.info("duration SECS: {}", DateMaps.of(localDtm).duration().getSeconds());

    }

}
