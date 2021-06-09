package javawork_service;

import javawork_dao.DistanceDao;
import javawork_model.DistanceCost;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * accept process data
 */
public class DistanceService {
    private float[][] distanceArray;
    private int[][] pathArray;
    private float[][] costArray;
    private int[][] costPathArray;
    private static List<DistanceCost> list;
    private static float max = Float.MAX_VALUE;
    public static Map<Integer, String> idToName;
    DistanceDao distanceDao;

    /**
     * constructor. init data
     */
    public DistanceService() {
        distanceDao = new DistanceDao();
        try {
            list = distanceDao.queryData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static{
        idToName = new HashMap<Integer, String>();
        idToName.put(1, "China");
        idToName.put(2, "Russia");
        idToName.put(3, "U.S.");
        idToName.put(4, "Brazil");
        idToName.put(5, "Sudan");
        idToName.put(6, "Australia");
    }

    /**
     * get route information data
     * @return two dimensional array contains route data.
     */
    public static Object[][] getData() {
        DistanceDao distanceDao = new DistanceDao();
        List<DistanceCost> list = null;
        Object[][] data = null;
        try {
            list = distanceDao.queryData();
            data = new Object[list.size()][7];

        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < list.size(); i++) {
            data[i][0] = list.get(i).getId();
            data[i][1] = list.get(i).getStart_id();
            data[i][2] = list.get(i).getStart();
            data[i][3] = list.get(i).getEnd_id();
            data[i][4] = list.get(i).getEnd();
            data[i][5] = list.get(i).getDistance();
            data[i][6] = list.get(i).getCost();
        }
        return data;
    }

    /**
     * init distance array
     */
    public void createDistanceArray() {
        distanceArray = new float[7][7];
        int start_id;
        int end_id;

        for (int i = 1; i < 7; i++) {
            for (int j = 1; j < 7; j++) {
                if(i == j) {
                    distanceArray[i][j] = 0;
                } else {
                    distanceArray[i][j] = max;
                }
            }
        }

        for (int i = 0; i < list.size(); i++) {
            start_id = list.get(i).getStart_id();
            end_id = list.get(i).getEnd_id();

            distanceArray[start_id][end_id] = list.get(i).getDistance();
            distanceArray[end_id][start_id] = list.get(i).getDistance();
        }
    }

    /**
     * init distance path array
     */
    public void createPathArray() {
        pathArray = new int[7][7];
        for (int i = 1; i < 7; i++) {
            for (int j = 1; j < 7; j++) {
                pathArray[i][j] = j;
            }
        }
    }

    /**
     * calculate distance array and return Shortest route
     * @param start_id the start place id
     * @param end_id the end place id
     * @return A string contains shortest distance route
     */
    public String getShortestDistance(int start_id, int end_id) {
        String startName = idToName.get(start_id);
        String endName = idToName.get(end_id);
        StringBuilder stringBuilder = new StringBuilder();
        int place_id;

        createDistanceArray();
        createPathArray();
        //Floyd Warshall Algorithm
        for(int k = 1; k < 7; k++) {
            for(int i = 1; i < 7; i++) {
                for(int j = 1; j < 7; j++) {
                    if(distanceArray[i][k] != max && distanceArray[k][j] != max
                            && distanceArray[i][j] > distanceArray[i][k] + distanceArray[k][j]) {
                        pathArray[i][j] = pathArray[i][k];
                        distanceArray[i][j] = distanceArray[i][k] + distanceArray[k][j];
                    }
                }
            }
        }

        for(int i = 0; i < distanceArray.length; i++) {
            for(int j = 0; j < distanceArray[i].length; j++) {
                System.out.print(distanceArray[i][j] + " ");
            }
            System.out.println();
        }

        stringBuilder.append("The shortest distance from ")
                .append(startName)
                .append(" to ")
                .append(endName)
                .append(" is: ")
                .append(distanceArray[start_id][end_id])
                .append(" km")
                .append("\n")
                .append("The shortest distance path is: ");

        place_id = start_id;
        while(place_id != end_id) {
            stringBuilder.append(idToName.get(place_id)).append(" --> ");
            place_id = pathArray[place_id][end_id];
        }
        stringBuilder.append(idToName.get(end_id));
        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }

    /**
     * get all place id in the shortest distance route
     * @param start_id the start place id
     * @param end_id the end place id
     * @return A string contains shortest distance route
     */
    public String getShortestId(int start_id, int end_id) {
        int place_id = start_id;
        StringBuilder idPath = new StringBuilder();
        while(place_id != end_id) {
            idPath.append(place_id);
            place_id = pathArray[place_id][end_id];
        }
        idPath.append(end_id);
        return idPath.toString();
    }

    /**
     * init cost array
     */
    public void createCostArray() {
        costArray = new float[7][7];
        int start_id;
        int end_id;

        for (int i = 1; i < 7; i++) {
            for (int j = 1; j < 7; j++) {
                if(i == j) {
                    costArray[i][j] = 0;
                } else {
                    costArray[i][j] = max;
                }
            }
        }

        for (int i = 0; i < list.size(); i++) {
            start_id = list.get(i).getStart_id();
            end_id = list.get(i).getEnd_id();

            costArray[start_id][end_id] = list.get(i).getCost();
            costArray[end_id][start_id] = list.get(i).getCost();
        }
    }

    /**
     * init cost path array
     */
    public void createCostPathArray() {
        costPathArray = new int[7][7];
        for (int i = 1; i < 7; i++) {
            for (int j = 1; j < 7; j++) {
                costPathArray[i][j] = j;
            }
        }
    }

    /**
     * calculate cost array and return lowest cost route
     * @param start_id the start place id
     * @param end_id the end place id
     * @return A string contains lowest cost route
     */
    public String getLowestCostPath(int start_id, int end_id) {
        String startName = idToName.get(start_id);
        String endName = idToName.get(end_id);
        StringBuilder stringBuilder = new StringBuilder();
        int place_id;

        createCostArray();
        createCostPathArray();
        for(int k = 1; k < 7; k++) {
            for(int i = 1; i < 7; i++) {
                for(int j = 1; j < 7; j++) {
                    if(costArray[i][k] != max && costArray[k][j] != max
                            && costArray[i][j] > costArray[i][k] + costArray[k][j]) {
                        costPathArray[i][j] = costPathArray[i][k];
                        costArray[i][j] = costArray[i][k] + costArray[k][j];
                    }
                }
            }
        }
        stringBuilder.append("The lowest cost path from ")
                .append(startName)
                .append(" to ")
                .append(endName)
                .append(" is: ")
                .append(" $")
                .append(costArray[start_id][end_id])
                .append("\n")
                .append("The lowest cost path is: ");

        place_id = start_id;
        while(place_id != end_id) {
            stringBuilder.append(idToName.get(place_id)).append(" --> ");
            place_id = costPathArray[place_id][end_id];
        }
        stringBuilder.append(idToName.get(end_id));
        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }

    /**
     * get all place id in the lowest cost route
     * @param start_id the start place id
     * @param end_id the end place id
     * @return A String contains lowest cost route
     */
    public String getLowestId(int start_id, int end_id) {
        int place_id = start_id;
        StringBuilder idPath = new StringBuilder();
        while(place_id != end_id) {
            idPath.append(place_id);
            place_id = costPathArray[place_id][end_id];
        }
        idPath.append(end_id);
        return idPath.toString();
    }

    /**
     * save data to database
     * @param distanceCosts A DistanceCost List
     * @return return true if the save is successful, otherwise returns false
     */
    public static boolean insertData(List<DistanceCost> distanceCosts) {
        DistanceDao distanceDao = new DistanceDao();
        boolean isUpdate = false;
        try {
            distanceDao.insertData(distanceCosts);
            isUpdate = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return isUpdate;
        }
    }
}
