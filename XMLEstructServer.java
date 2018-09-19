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
  private Label obs;
  private Label total;
  private List list;

  public XMLEstructImpl(){
    this.obs = null;
    this.total = null;
    this.list = null;
  }

  public void setORB(ORB orb_val){
    orb = orb_val;
  }
  public void setContent( Label o,  Label t, List l ){
    this.obs = o;
    this.total = t;
    this.list = l;
  }

  public String upload( String fileName, String file ){
    String pathF = file+"\\"+fileName;
    System.out.println( "VAl: "+ pathF );
    try{
        File xmlDocument = new File(pathF);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      	Document doc = dBuilder.parse(xmlDocument);
      	doc.getDocumentElement().normalize();
        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        NodeList nList = doc.getElementsByTagName("author");

	for (int temp = 0; temp < nList.getLength(); temp++) {
		Node nNode = nList.item(temp);
		System.out.println("\nCurrent Element :" + nNode.getNodeName());
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) nNode;

      String myDriver = "org.gjt.mm.mysql.Driver";
      String myUrl = "jdbc:mysql://localhost:3306/distribuido";
      Class.forName("com.mysql.jdbc.Driver").newInstance();
      Connection conn = DriverManager.getConnection(myUrl, "root", "");
      Statement st = conn.createStatement();
      String q = "INSERT INTO authors ( first_name, last_name, email, birthdate, added, category ) values( ?, ?, ?, ?, NOW(), ? )";
      PreparedStatement prep = conn.prepareStatement(q);
      prep.setString(1, eElement.getElementsByTagName("first_name").item(0).getTextContent());
      prep.setString(2, eElement.getElementsByTagName("last_name").item(0).getTextContent());
      prep.setString(3, eElement.getElementsByTagName("email").item(0).getTextContent());
      prep.setString(4, eElement.getElementsByTagName("birthday").item(0).getTextContent());
      prep.setString(5, eElement.getElementsByTagName("category").item(0).getTextContent());

      prep.execute();

			System.out.println("First Name : " + eElement.getElementsByTagName("first_name").item(0).getTextContent());
			System.out.println("Last Name : " + eElement.getElementsByTagName("last_name").item(0).getTextContent());
			System.out.println("Email : " + eElement.getElementsByTagName("email").item(0).getTextContent());
			System.out.println("Birthday : " + eElement.getElementsByTagName("birthday").item(0).getTextContent());
			System.out.println("Category : " + eElement.getElementsByTagName("category").item(0).getTextContent());
      if( this.obs != null ){
        this.obs.setText( "Nuevo autor agregado -  " + eElement.getElementsByTagName("first_name").item(0).getTextContent() + " " +   eElement.getElementsByTagName("last_name").item(0).getTextContent() );
        this.total.setText( getTotal() );
        this.list.setItems( getXML() );
      }
		}
	}

    }catch(Exception e){
      e.printStackTrace();
    }
  //  XE excel = new XE();
  //  excel.generateExcel(xmlDocument);
  //  System.out.println( file );

    return "Hello world !!\n";
  }
  public String getTotal(){
    String dataa = "";
    try{
      String myDriver = "org.gjt.mm.mysql.Driver";
      String myUrl = "jdbc:mysql://localhost:3306/distribuido";
      Class.forName("com.mysql.jdbc.Driver").newInstance();
      Connection conn = DriverManager.getConnection(myUrl, "root", "");
      String query = "SELECT Count(*) total FROM authors";
      Statement st = conn.createStatement();
      ResultSet rs = st.executeQuery(query);
      while( rs.next() ){
        dataa = rs.getString("total");
      }
      st.close();
      return "Total de autores: " + dataa;
    }catch (Exception e){
      System.err.println("Got an exception! ");
      System.err.println(e.getMessage());
    }
    return "Error :c" + dataa;

  }

  public String[] getXML() {
    String[] dataa = new String [20];
    for( int i = 0; i < 20; i++ )
      dataa[i] = "";

    try{
      String myDriver = "org.gjt.mm.mysql.Driver";
      String myUrl = "jdbc:mysql://localhost:3306/distribuido";
      Class.forName("com.mysql.jdbc.Driver").newInstance();
      Connection conn = DriverManager.getConnection(myUrl, "root", "");
      String query = "SELECT concat( first_name, ' ', last_name ) nombre FROM authors order by id desc limit 15";
      Statement st = conn.createStatement();
      ResultSet rs = st.executeQuery(query);
      int cont = 0;
      while (rs.next()){
        dataa[cont] = ( rs.getString("nombre") );
        cont++;
      }
      st.close();
      return dataa;
    }catch (Exception e){
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
    String dataa = "";
    try{
      String myDriver = "org.gjt.mm.mysql.Driver";
      String myUrl = "jdbc:mysql://localhost:3306/distribuido";
      Class.forName("com.mysql.jdbc.Driver").newInstance();
      Connection conn = DriverManager.getConnection(myUrl, "root", "");
      String query = "SELECT concat(first_name, ' ', last_name ) nombre, email, birthdate, added, category FROM authors where concat(first_name, ' ', last_name ) = ?";
      PreparedStatement prep = conn.prepareStatement(query);
      prep.setString(1, name);

      ResultSet rs = prep.executeQuery();
      while( rs.next() ){
        dataa = rs.getString("nombre") + " - " +  rs.getString("email") +
                "\n Cumpleanos " + rs.getString("birthdate") +
                "\n Subido el " + rs.getString("added") +
                "\n Escribe sobre " + rs.getString("category")
        ;
      }
      prep.close();
      return   dataa;
    }catch (Exception e){
      System.err.println("Got an exception! ");
      System.err.println(e.getMessage());
    }
    return "Error :c" + dataa;
  }
  public void shutdown(){
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
    XMLEstructImpl datax = new  XMLEstructImpl( );
    shell = new Shell();
    shell.setSize(653, 503);
    shell.setText("Servidor");

    Group grpInicio = new Group(shell, SWT.NONE);
    grpInicio.setText("Cargas Recientes");
    grpInicio.setBounds(10, 50, 617, 189);

    List list = new List(grpInicio, SWT.BORDER);
    list.setBounds(10, 24, 597, 141);
    list.setItems( datax.getXML() );

    Button loadData = new Button( shell, SWT.PUSH );
    loadData.setText( "Cargar de nuevo");
    loadData.setBounds( 10, 20, 100, 25 );

    Label total = new Label(shell,  SWT.CENTER );
    total.setBounds(150, 20, 200, 25 );
    total.setText( datax.getTotal() );

    loadData.addSelectionListener( new SelectionAdapter(){
       @Override
       public void widgetSelected(SelectionEvent arg0) {
        total.setText( datax.getTotal() );
        list.setItems( datax.getXML() );
       }
    });

    Label observable = new Label(shell,  SWT.CENTER );
    observable.setBounds(10, 400, 200, 25 );
    observable.setText( "" );
    datax.setContent( observable, total, list  );


  }

}
