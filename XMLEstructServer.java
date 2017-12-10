import XmlFinal.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;
import java.io.*; 
import javax.servlet.*; 
import javax.servlet.http.*;



import java.util.Properties;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Menu;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import org.w3c.dom.*;
import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

 class XMLEstructImpl extends XMLEstructPOA{
	private ORB orb;	
	
	  public void setORB(ORB orb_val)
	  {
	    orb = orb_val;
	  }
	  
	   public String upload( String fileName, String file )
	  {
		   String pathF = file+"\\"+fileName;
		   System.out.println( "VAl: "+ pathF );
		   
			File xmlDocument = new File(pathF);

			XE excel = new XE();
			excel.generateExcel(xmlDocument);
			
		   System.out.println( file );
		   
		   try{
		   String myDriver = "org.gjt.mm.mysql.Driver";
		      String myUrl = "jdbc:mysql://localhost:3306/distribuido";
		      Class.forName("com.mysql.jdbc.Driver").newInstance();
		      Connection conn = DriverManager.getConnection(myUrl, "distribuido", "password");
		      
		   Statement st = conn.createStatement();
		      String q = "INSERT INTO archivos ( id_archivos, nombre )"+ "values ( ?, ?)";
		      PreparedStatement prep = conn.prepareStatement(q);
		      prep.setString(1, "9");
		      prep.setString(2, pathF);
		      prep.execute();
		   }
		    catch (Exception e){
		    	System.out.print( e );
		    }
		   return "Hello world !!\n";
	  }
	   
	   
	   public String[] getXML() {
		   String[] dataa = new String [20];
		   for( int i = 0; i < 20; i++ )
			   dataa[i] = "";
		      
		   try{
		      String myDriver = "org.gjt.mm.mysql.Driver";
		      String myUrl = "jdbc:mysql://localhost:3306/distribuido";
		      Class.forName("com.mysql.jdbc.Driver").newInstance();
		      Connection conn = DriverManager.getConnection(myUrl, "distribuido", "password");
		      String query = "SELECT * FROM archivos";
		      Statement st = conn.createStatement();
		      ResultSet rs = st.executeQuery(query);
		      int cont = 0;
		      while (rs.next()){
		    	
		    	dataa[cont] = ( rs.getString("nombre") );
		    	cont++;
	
		      }
		      
		      st.close();
		      return dataa;
		    }
		    catch (Exception e){
		      System.err.println("Got an exception! ");
		      System.err.println(e.getMessage());
		    }
		   return dataa;
		  /* String[] data = new String[2];
		   data[0] = "Archivo1";
		   data[1] = "Archivo2";
		   return data;*/
	   }
	   
	   public String getXMLDetail( String name ) {
		   return "<element>>/element>";
	   }
	   public void shutdown()
	  {
	    orb.shutdown(false);
	  }
}

public class XMLEstructServer {
	protected Shell shell;
	
	public static void main(String[] args) {
		try {
			ORB orb = ORB.init(args, null);
			POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
		      rootpoa.the_POAManager().activate();
		      
		      XMLEstructImpl xmlImpl = new XMLEstructImpl();
		      xmlImpl.setORB(orb);
		      
		      org.omg.CORBA.Object ref = rootpoa.servant_to_reference(xmlImpl);
		      XMLEstruct href = XMLEstructHelper.narrow(ref);
		      
		      org.omg.CORBA.Object objRef =
		    	      orb.resolve_initial_references("NameService");
		      
		      NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
		      
		      String name = "XMLEstruct";
		      NameComponent path[] = ncRef.to_name( name );
		      ncRef.rebind(path, href);
		      
		      System.out.println("XMLServer ready and waiting ...");
		      XMLEstructServer window = new XMLEstructServer();
			window.open();
		      orb.run();
		      
		      
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("XMLoServer Exiting ...");
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		XMLEstructImpl datax = new  XMLEstructImpl();
		
		//System.out.println( items[0] );
		shell = new Shell();
		shell.setSize(653, 503);
		shell.setText("Servidor");
		Group grpInicio = new Group(shell, SWT.NONE);
		grpInicio.setText("Cargas Recientes");
		grpInicio.setBounds(10, 106, 617, 189);
		
		List list = new List(grpInicio, SWT.BORDER);
		list.setBounds(10, 24, 597, 141);
		list.setItems( datax.getXML() );
		
		
		
		

	}

}
