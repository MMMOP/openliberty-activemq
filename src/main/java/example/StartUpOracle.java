package example;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@Singleton
@Startup
public class StartUpOracle {

    @Resource(lookup = "jdbc/oracle")
    private DataSource dataSource;

    @PostConstruct
    public void init() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT 'temp1' as name from dual " +
                                                    "UNION ALL " +
                                                    "SELECT 'temp2' from dual " +
                                                    "UNION ALL " +
                                                    "SELECT 'temp3' from dual ")) {

            while (rs.next()) {
                System.out.println("User: " + rs.getString( "name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}