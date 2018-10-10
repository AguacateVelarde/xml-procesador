import XmlFinal.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;
import java.util.Scanner;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;

import org.w3c.dom.*;
import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class client {
	protected static Shell shell;
	static String[] path;
	static XMLEstruct parsingData;
	String info;
	static String[] listItems;
	private static Label text;


	public static void main(String[] args) {
		final Button	b1;
		final Button	b2;
		final Button	b3;
		final Button	b4;
		try {
			ORB orb = ORB.init(args, null);
			org.omg.CORBA.Object objRef =
			orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			String name = "XMLEstruct";
			parsingData = XMLEstructHelper.narrow(ncRef.resolve_str(name));
			System.out.println("Obtained a handle on server object: " + parsingData);
			client window = new client();
			window.saveData( parsingData.getXML() ) ;

			//parsingData.shutdown();
			Display display = Display.getDefault();
			shell = new Shell();
			shell.setSize(653, 503);
			shell.setText("SuperXML - Cliente");

			Group grpInicio = new Group(shell, SWT.NONE);
			grpInicio.setText("Cargas Recientes");
			grpInicio.setBounds(10, 106, 617, 189);

			List list = new List(grpInicio, SWT.BORDER);
			list.setBounds(10, 24, 597, 141);
			list.setItems( listItems );

			list.addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent event) {
					text.setText( parsingData.getXMLDetail( list.getSelection()[0] ) );
				}

				public void widgetDefaultSelected(SelectionEvent event) {
					text.setText( parsingData.getXMLDetail( list.getSelection()[0] ) );
				}
			});

			Group grpVisor = new Group(shell, SWT.NONE);
			grpVisor.setText("Visor");
			grpVisor.setBounds(10, 316, 617, 138);

			text = new Label(grpVisor,  SWT.CENTER );
			text.setBounds(10, 21, 597, 107);



			Button btnCargarXml = new Button(shell, SWT.NONE);
			btnCargarXml.setBounds(520, 35, 95, 25);
			btnCargarXml.setText("CARGAR XML");

			btnCargarXml.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected( SelectionEvent e ) {
					FileDialog fileDialog = new FileDialog( shell, SWT.OPEN );
					fileDialog.setText("Seleccione su XML");
					String[] filterExt = { "*.xml;*.XML" };
					String[] filterNames = { "Solo XML" };
					fileDialog.setFilterExtensions(filterExt);
					fileDialog.setFilterNames(filterNames);
					fileDialog.open();
					String path =  fileDialog.getFilterPath();
					if( path != null ){
						System.out.println( fileDialog.getFilterPath() );
						System.out.print( fileDialog.getFileName() );
						parsingData.upload( fileDialog.getFileName(), fileDialog.getFilterPath()  );
						list.setItems( parsingData.getXML() );
					}

				}
			});





			final Group titleGroup = new Group(shell, SWT.SHADOW_IN);
	    titleGroup.setText("Categorias");
	 		titleGroup.setLayout(new GridLayout(3, true));
			titleGroup.setBounds(10, 35, 350, 50);

			 b1 =	new Button(titleGroup, SWT.RADIO);
			 b1.setText("Misterio");
			 b1.setBounds(10, 20, 75, 15);
			 b2 = new Button(titleGroup, SWT.RADIO);
			 b2.setText("Amor");
			 b2.setBounds(100, 20, 75, 15);
			 b3 = new Button(titleGroup, SWT.RADIO);
			 b3.setText("Aventura");
			 b3.setBounds(190, 20, 75, 15);
			 b4 = new Button(titleGroup, SWT.RADIO);
			 b4.setText("Todos");
			 b4.setBounds(280, 20, 75, 15);

			 Button descargar = new Button(shell, SWT.NONE);
			 descargar.setBounds(520, 70, 95, 25);
			 descargar.setText("Descargar XLSX");

			 descargar.addSelectionListener( new SelectionAdapter() {
				 public void widgetSelected( SelectionEvent e ) {
					 FileDialog fileDialog = new FileDialog( shell, SWT.SAVE );
					 fileDialog.setText("Guarde su XLSX");
					 String[] filterExt = { "*.xlsx;*.XLSX" };
					 String[] filterNames = { "Excel" };
					 fileDialog.setFilterExtensions(filterExt);
					 fileDialog.setFilterNames(filterNames);
					 fileDialog.open();
					 String path =  fileDialog.getFilterPath();
					 if( path != null ){
						 String send = "data";
						 if( b1.getSelection() ) send = "misterio";
						 if( b2.getSelection() ) send = "amor";
						 if( b3.getSelection() ) send = "aventura";

						 final String res =  (parsingData.getXMLDetail(send));
						 ArrayList<String> escritores= new ArrayList(Arrays.asList(res.split(";")));
						 System.out.println(  escritores.get(0).toString() );
						 int cont = 1;
						 HSSFWorkbook workbook = new HSSFWorkbook();
						 HSSFSheet sheet = workbook.createSheet("Escritores");

						 HSSFRow  row = sheet.createRow(0);
						 HSSFCell  cell = row.createCell((short)0);
						 cell.setCellValue("Nombre");
							 row = sheet.createRow(0);
							 cell = row.createCell((short)1);
						 cell.setCellValue("Correo Electronico");
							 row = sheet.createRow(0);
							 cell = row.createCell((short)2);
						 cell.setCellValue("Cumpleanios");
							 row = sheet.createRow(0);
							 cell = row.createCell((short)3);
						 cell.setCellValue("Fecha de alta");
							 row = sheet.createRow(0);
							 cell = row.createCell((short)4);
						 cell.setCellValue("Categoria");

						 for( String escritor : escritores ){
							 ArrayList esc= new ArrayList(Arrays.asList(escritor.split(",")));
							 System.out.println(  esc.get(0).toString() );
								 row = sheet.createRow(cont);
								 cell = row.createCell((short)0);
							 cell.setCellValue( esc.get(0).toString() );
								 row = sheet.createRow(cont);
								 cell = row.createCell((short)1);
							 cell.setCellValue( esc.get(1).toString() );
								 row = sheet.createRow(cont);
								 cell = row.createCell((short)2);
							 cell.setCellValue( esc.get(2).toString() );
								 row = sheet.createRow(cont);
								 cell = row.createCell((short)3);
							 cell.setCellValue( esc.get(3).toString() );
								 row = sheet.createRow(cont);
								 cell = row.createCell((short)4);
							 cell.setCellValue( esc.get(4).toString() );
							 ++cont;
						 }
						 try {
							 String fileNombre =  fileDialog.getFileName();
							 FileOutputStream out =
							 new FileOutputStream(new File( path + "\\" + fileNombre ));
							 System.out.println(  path + "\\" + fileNombre );
							 workbook.write(out);
							 out.close();
							 System.out.println("Excel written successfully..");

						 } catch (FileNotFoundException e1) {
							 e1.printStackTrace();
						 } catch (IOException e2) {
							 e2.printStackTrace();
						 }
					 }

				 }
			 });


			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}

			//while(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	* Open the window.
	*/
	public void saveData( String[] items ) {
		listItems = items;
	}
	public void open() {

	}

	/**
	* Create contents of the window.
	*/
	protected void createContents() {


	}
}
