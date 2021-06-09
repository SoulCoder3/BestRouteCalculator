package javawork_dao;

import javawork_model.DistanceCost;
import javawork_utils.C3P0Util;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * get distance and cost from database
 */
public class DistanceDao {
    DataSource dataSource = C3P0Util.getDataSource();

    /**
     * query data from distance_cost table and fit them in a list
     *
     * @return A DistanceCost Object list contains all data from distance_cost table
     */
    public List<DistanceCost> queryData() throws SQLException {
        QueryRunner qry = new QueryRunner(dataSource);
        String sql = "SELECT * FROM distance_cost";

        List<DistanceCost> distanceCostList = qry.query(sql, new BeanListHandler<DistanceCost>(DistanceCost.class));
        return distanceCostList;
    }

    /**
     * insert data to database
     * @param list A DistanceCost Object List
     * @throws SQLException
     */
    public void insertData(List<DistanceCost> list) throws SQLException {
        Connection conn = C3P0Util.getConnection();
        String sql = "INSERT INTO distance_cost(ID, distance, cost) VALUES (?, ?, ?)" +
                "ON DUPLICATE KEY UPDATE " +
                "ID=VALUES(ID), distance=VALUES(distance), cost=VALUES(cost)";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        for(int i = 0; i < list.size(); i++) {
            DistanceCost distanceCost = list.get(i);
            preparedStatement.setInt(1, distanceCost.getId());
            preparedStatement.setFloat(2, distanceCost.getDistance());
            preparedStatement.setFloat(3, distanceCost.getCost());
            preparedStatement.executeUpdate();
        }
    }
}
