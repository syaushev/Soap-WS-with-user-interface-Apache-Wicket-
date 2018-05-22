package databases;




import tests.WeatherPage;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;

import java.sql.SQLException;



public class myDB {
    private static Connection conn;
    private static InitialContext ctx;
    private static DataSource ds;
    static  final String url="jdbc:postgresql://localhost:5432/weather";
    static   final String name="postgres";
    static  final String password="100987";
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(myDB.class);

    private myDB() {



        try {

            ctx=new InitialContext();
        } catch (NamingException e) {
            log.warn("Naming exception during creating new Initial Context ");
            e.printStackTrace();
        }
    }
    private static myDB instance;

    public static myDB getInstance() {
        if (instance == null) {
            instance = new myDB();
        }

        return instance;
    }

    public Connection getConnection(){
        try {

         Context context=(Context) ctx.lookup("java:comp/env");
        DataSource dataSource=(DataSource) context.lookup("jdbc/weather");
        return dataSource.getConnection();
        } catch (NamingException | SQLException e) {
            e.printStackTrace();
            log.warn("SQL exception during getting connection");
        }
        return null;

    }


    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
//                System.out.println("Connection is closed");

            } catch (SQLException ex) {
              ex.printStackTrace();
            }
        }
    }

}
