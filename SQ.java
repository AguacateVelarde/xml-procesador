import java.sql.*;

/**
 * A Java MySQL SELECT statement example.
 * Demonstrates the use of a SQL SELECT statement against a
 * MySQL database, called from a Java program.
 * 
 * Created by Alvin Alexander, http://alvinalexander.com
 */
public class SQ
{

  public static void main(String[] args)
  {
    try
    {
      // create our mysql database connection
      String myDriver = "org.gjt.mm.mysql.Driver";
      String myUrl = "jdbc:mysql://localhost/distribuido";
      Class.forName(myDriver);
      Connection conn = DriverManager.getConnection(myUrl, "distribuido", "");
      String data[] = new String [10];
      
      // our SQL SELECT query. 
      // if you only need a few columns, specify them by name instead of using "*"
      String query = "SELECT * FROM archivos";
      // create the java statement
      Statement st = conn.createStatement();
      
      // execute the query, and get a java resultset
      ResultSet rs = st.executeQuery(query);
      
      // iterate through the java resultset
      while (rs.next())
      {
    	data[0]=Integer.toString(rs.getInt("id_archivos"));
    	data[1]=rs.getString("nombre");
    	
        
        
        // print the results
        System.out.println(data[0]);
        System.out.println(data[1]);
       
      }
      st.close();
    }
    catch (Exception e)
    {
      System.err.println("Got an exception! ");
      System.err.println(e.getMessage());
    }
  }
}