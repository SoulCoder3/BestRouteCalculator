package javawork_utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * C3P0 Util connect database
 */
public class C3P0Util {
    //static variable
    private static ComboPooledDataSource dataSource = new ComboPooledDataSource();

    /**
     * get database source
     * @return databse source
     */
    public static DataSource getDataSource() {
        return dataSource;
    }

    /**
     * get database connect
     * @return database connect
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
