import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnector {

    private static Connection conn=null;
    private static String url = "jdbc:mysql://localhost:3306/seriesticker";
    private static String user = "root";//Username of database
    private static String pass = null;//Password of database

    public static Connection connect() throws SQLException{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        }catch(ClassNotFoundException cnfe){
            System.err.println("Error: "+cnfe.getMessage());
        }catch(InstantiationException ie){
            System.err.println("Error: "+ie.getMessage());
        }catch(IllegalAccessException iae){
            System.err.println("Error: "+iae.getMessage());
        }
        catch(Exception e){
            System.err.println(e);
        }
        conn = DriverManager.getConnection(url,user,pass);
        //createDatabase(conn);
        //createTable(conn);
        return conn;
    }

    public static Connection getConnection() throws SQLException, ClassNotFoundException{
        if(conn !=null && !conn.isClosed()) {
            return conn;
        }
        connect();
        return conn;
    }

    public static void createDatabase(Connection conn) {
        String sql_stmt = "CREATE DATABASE IF NOT EXISTS  `seriesticker`;";
        executeSQL(sql_stmt);
    }


    public static void createTable(Connection conn) {

        String sqlTable = "CREATE TABLE IF NOT EXISTS SeriesTicker.`TVSeries` (\n"
                + "    `id` SMALLINT(5) UNSIGNED NOT NULL AUTO_INCREMENT,\n"
                + "    `name` VARCHAR(45) NOT NULL,\n"
                + "    `current_season` INTEGER,\n"
                + "    `current_episode` INTEGER,\n"
                + "    `nextEpisodeDate` VARCHAR(100),\n"
                + "    `lastUpdate` VARCHAR(100),\n"
                + "    PRIMARY KEY (`id`)\n"
                + ");";

        executeSQL(sqlTable);
    }

    public static void executeSQL(String sql) {
        if (conn == null) {
            try {
                System.out.println("Connecting");
                connect();
            }catch (Exception e) {
                System.out.println("Connection failed: "+e);
            }
        }
        try {
            Statement stmt1=conn.createStatement();
            stmt1.executeUpdate(sql);
        }        catch (Exception e) {
            System.out.println(e);
        }
        //catch (SQLException e1) {
        //   e1.printStackTrace();
        //}


    }








}
