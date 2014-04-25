package javafxapp.db;

import javafxapp.adapter.domain.Adapter;
import javafxapp.adapter.domain.Settings;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: vmaksimov
 */
public class DatabaseUtil {

    private static Connection connection;
    private static Statement statement;
    private static final String status_ready = "На отправку";
    private static String status_sent = "Отправлено";

    public static void createDB() {
        if (connection == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:autosend.db");
            } catch (Exception e) {
                System.out.println("Ошибка в создании нового соединения " + e.getLocalizedMessage());
            }
            String query = "CREATE TABLE adapter (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "id210fz VARCHAR(32), " +
                    "numReq INTEGER , " +
                    "requestXml VARCHAR(32), " +
                    "responseXml VARCHAR(32), " +
                    "responseStatus VARCHAR(32)," +
                    "FOREIGN KEY(id210fz) REFERENCES adapterDetails(id210fz));" +

                    "CREATE TABLE adapterDetails (id210fz VARCHAR(32) PRIMARY KEY, " +
                    "smevAddress VARCHAR(32), " +
                    "foiv VARCHAR(32), " +
                    "adapterName VARCHAR(32));" +

                    "CREATE TABLE smevfield (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name VARCHAR(32), " +
                    "value VARCHAR(32), " +
                    "foiv VARCHAR(32));" +

                    "CREATE TABLE settings (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "pathFile VARCHAR(32), " +
                    "key_alias VARCHAR(32), " +
                    "cert_alias VARCHAR(32)," +
                    "password VARCHAR(32));" +

                    "INSERT INTO adapterDetails (id210fz, smevAddress, foiv, adapterName) VALUES ('07', 'http://192.168.100.96:7777/gateway/services/SID0003245', 'ФНС','(Сведения из ЕГРИП)');" +
                    "INSERT INTO adapterDetails (id210fz, smevAddress, foiv, adapterName) VALUES ('07_2', 'http://192.168.100.96:7777/gateway/services/SID0003245', 'ФНС','(Сведения из ЕГРЮЛ)');" +

                    "INSERT INTO settings (pathFile, key_alias, cert_alias, password) VALUES ('C:\\', 'RaUser-2908cdc2-4aff-47c6-9636-d2a98ba3d2b5','RaUser-2908cdc2-4aff-47c6-9636-d2a98ba3d2b5','1234567890');";
            try {
                statement = connection.createStatement();
                statement.executeUpdate(query);
                statement.close();
            } catch (SQLException e) {
                e.getMessage();
            }
        }
    }

    public static void insertRequests(String foiv, List<String> requestsIp, List<String> requestsUl) {
        try {
            clearTable("ADAPTER");

            statement = connection.createStatement();
            String query = "";
            for (int i = 0; i < requestsIp.size(); i++) {
                query += "INSERT INTO adapter (id210fz, numReq, requestXml) VALUES ('07', " + i + ", '" + requestsIp.get(i) + "');";
            }
            for (int i = 0; i < requestsUl.size(); i++) {
                query += "INSERT INTO adapter (id210fz, numReq, requestXml) VALUES ('07_2', " + i + ", '" + requestsIp.get(i) + "');";
            }
            statement.executeUpdate(query);
            statement.close();
        } catch (SQLException e) {
            System.out.println("Ошибка при сохранении запросов " + e.getMessage());
        }
    }

    public static void savePathFile(String path) {
        try {
            statement = connection.createStatement();
            String query = "UPDATE settings SET pathFile='" + path + "');";
            statement.executeUpdate(query);
            statement.close();
        } catch (SQLException e) {
            System.out.println("Ошибка при сохранение пути к файлу " + e.getMessage());
        }
    }

    public static void saveSmevFields(String foiv, HashMap<String, String> smevFileds) {
        try {
            clearTable("smevfield");

            statement = connection.createStatement();
            String query = "";
            for (Map.Entry<String, String> entry : smevFileds.entrySet()) {
                query += "INSERT INTO smevfield (name, value, foiv) VALUES ('" + entry.getKey() + "', '" + entry.getValue() + "', '" + foiv + "');";

            }
            statement.executeUpdate(query);
            statement.close();
        } catch (SQLException e) {
            System.out.println("Ошибка при сохранение служебных полей " + e.getMessage());
        }
    }

    public static Settings getSettings() {
        Settings settings = new Settings();
        try {
            statement = connection.createStatement();
            String query = "SELECT pathFile, key_alias, cert_alias, password FROM settings";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                settings.setPathFile(resultSet.getString(1));
                settings.setKeyAlias(resultSet.getString(2));
                settings.setCertAlias(resultSet.getString(3));
                settings.setPassword(resultSet.getString(4));
            }
            statement.close();
        } catch (SQLException e) {
            System.out.println("Ошибка при сохранение служебных полей " + e.getMessage());
        }
        return settings;
    }

    public static HashMap<String, String> getSmevFields(String foiv) {
        HashMap<String, String> hashMap = null;
        try {
            statement = connection.createStatement();
            String query = "SELECT name, value FROM smevfield WHERE foiv = '" + foiv + "'";
            ResultSet resultSet = statement.executeQuery(query);
            hashMap = new HashMap<>();
            while (resultSet.next()) {
                hashMap.put(resultSet.getString(1), resultSet.getString(2));
            }
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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

    public static void close() {
        try {
            statement = connection.createStatement();
            statement.execute("SHUTDOWN");
            statement.close();
        } catch (SQLException e) {
            System.out.println("Ошибка в закрытии соединения " + e.getMessage());
        }

    }

    public static List<Adapter> getRequest(String id210fz) {
        List<Adapter> adapters = new ArrayList<>();
        try {
            statement = connection.createStatement();
            String query = "SELECT numReq, requestXml FROM adapter WHERE id210fz = '" + id210fz + "'";
            ResultSet resultSet = statement.executeQuery(query);
            Adapter adapter;
            while (resultSet.next()) {
                adapter = new Adapter();
                adapter.setNumReq(resultSet.getInt(1));
                adapter.setRequestXml(resultSet.getString(2));
                adapters.add(adapter);
            }
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return adapters;
    }

    public static void saveResponse(String responseXml, String respStatus, int numReq, String id210fz) {
        try {
            statement = connection.createStatement();
            String query = "SELECT count(*) FROM adapter WHERE numReq = '" + numReq + "' and id210fz = '" + id210fz + "'";
            ResultSet resultSet = statement.executeQuery(query);
            String isExist = "";
            while (resultSet.next()) {
                isExist = resultSet.getString(1);
            }
            if (!isExist.equals("")) {
                query = "UPDATE adapter SET responseXml = '" + responseXml + "', responseStatus = '" + respStatus + "';";
            }
            statement.executeUpdate(query);
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static List<Adapter> findReqReadyToSend() {
        List<Adapter> adapterList = new ArrayList<>();
        try {
            statement = connection.createStatement();
            String query = "SELECT ID, numReq, requestXml, adapterName FROM adapter WHERE responseStatus NOT LIKE 'ACCEPT';";
            ResultSet resultSet = statement.executeQuery(query);
            Adapter adapter;
            while (resultSet.next()) {
                adapter = new Adapter();
                adapter.setId(resultSet.getInt(1));
                adapter.setNumReq(resultSet.getInt(2));
                adapter.setRequestXml(resultSet.getString(3));
//                adapter.setAdapterName(resultSet.getString(4));
                adapterList.add(adapter);
            }
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return adapterList;
    }

    public static void saveSettings(Settings settings) {
        try {
            statement = connection.createStatement();
            String query = "UPDATE settings SET key_alias='" + settings.getKeyAlias() + "', password ='" + settings.getPassword() + "';";
            statement.executeUpdate(query);
            statement.close();
        } catch (SQLException e) {
            System.out.println("Ошибка при сохранение пути к файлу " + e.getMessage());
        }
    }
}
