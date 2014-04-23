package javafxapp.db;

import org.apache.commons.codec.binary.Base64;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: vmaksimov
 */
public class DatabaseUtil {

    private static Connection connection;
    private static Statement statement;

    public static void createDB() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:autosend.db");
        } catch (Exception e) {
            System.out.println("Ошибка в создании нового соединения " + e.getMessage());
        }
        String query = "CREATE TABLE adapter (id IDENTITY AUTO_INCREMENT, adapterName VARCHAR(32), request VARCHAR(32));" +
                        "CREATE TABLE smevfield (id IDENTITY AUTO_INCREMENT, name VARCHAR(32), value VARCHAR(32), foiv VARCHAR(32));";
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
            statement.close();
        } catch (SQLException ignored) {}
    }

    public static void insertRequests(String adapterName, List<String> requests)  {
        try {
            clearTable("ADAPTER");

            statement = connection.createStatement();
            String query = "";
            for (String request: requests){
                query += "INSERT INTO adapter (adapterName, request) VALUES ('"+ adapterName + "', '" + new String(Base64.encodeBase64(request.getBytes())) + "');";
            }
            statement.executeUpdate(query);
            statement.close();
        } catch (SQLException e) {
            System.out.println("Ошибка при сохранении запросов " + e.getMessage());
        }
    }

    public static void saveSmevFields(String foiv, HashMap<String, String> smevFileds)  {
        try {
            clearTable("smevfield");

            statement = connection.createStatement();
            String query = "";
            for(Map.Entry<String, String> entry : smevFileds.entrySet()) {
                query += "INSERT INTO smevfield (name, value, foiv) VALUES ('"+ entry.getKey() + "', '" + entry.getValue() + "', '" + foiv + "');";

            }
            statement.executeUpdate(query);
            statement.close();
        } catch (SQLException e) {
            System.out.println("Ошибка при сохранение служебных полей " + e.getMessage());
        }
    }

    public static HashMap<String, String> getSmevFields(String foiv)  {
        HashMap<String, String> hashMap = null;
        try {
            statement = connection.createStatement();
            String query = "SELECT name, value FROM smevfield WHERE foiv = '"+ foiv +"'";
            ResultSet resultSet = statement.executeQuery(query);
            hashMap = new HashMap<>();
            while (resultSet.next()) {
                hashMap.put(resultSet.getString(1), resultSet.getString(2));
            }
            statement.close();
        } catch (SQLException e) {
            System.out.println("Ошибка при сохранение служебных полей " + e.getMessage());
        }
        return hashMap;
    }

    private static void clearTable(String nameTable) throws SQLException {
        try {
            statement = connection.createStatement();
            String dropQuery = "delete from " + nameTable;
            statement.executeUpdate(dropQuery);
            statement.close();
        } catch (SQLException e) {
            System.out.println("Не может очистить таблицу: " + nameTable + " Message:" + e.getMessage());
        }
    }

    public static void select(String adapterName)  {
        try {
            statement = connection.createStatement();
            String query = "SELECT id, adapterName, request  FROM adapter";
            ResultSet resultSet = statement.executeQuery(query);

            /*while (resultSet.next()) {
                System.out.println(resultSet.getInt(1) + " "
                        + resultSet.getString(2));
            }*/
            statement.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close()  {
        try {
            statement = connection.createStatement();
            statement.execute("SHUTDOWN");
            statement.close();
        } catch (SQLException e) {
            System.out.println("Ошибка в закрытии соединения " + e.getMessage());
        }

    }

}
