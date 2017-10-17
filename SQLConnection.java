import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*Defines a SQL connection using JDBC to connect to SQLite database*/
public class SQLConnection{
   public static Connection DbConnector(){
      try{
         Connection conn = null;
         Class.forName("org.sqlite.JDBC");
         conn = DriverManager.getConnection("jdbc:sqlite:database/blackjackdb.sqlite");
         return conn;
      }catch(ClassNotFoundException | SQLException e){
         System.out.println(e);      
      }
      return null;
   }



}