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

public class client {
	protected static Shell shell;
	static String[] path;
	static XMLEstruct parsingData;
	String info;
	static String[] listItems;
	private static Label text;

	public static void main(String[] args) {
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

			Button descargar = new Button(shell, SWT.NONE);
			descargar.setBounds(520, 70, 95, 25);
			descargar.setText("Descargar XLSX");

			Group titleGroup = new Group(shell, SWT.SHADOW_IN);
	    titleGroup.setText("Categorias");
	 		titleGroup.setLayout(new GridLayout(3, true));
			titleGroup.setBounds(10, 35, 350, 50);

					new Button(titleGroup, SWT.RADIO).setText("Misterio");
			    new Button(titleGroup, SWT.RADIO).setText("Amor");
			    new Button(titleGroup, SWT.RADIO).setText("Aventura");

					Group fechagroup = new Group(shell, SWT.SHADOW_IN);
					fechagroup.setText("Fecha de alta");
					fechagroup.setLayout(new GridLayout(3, true));
					fechagroup.setBounds(50, 35, 350, 50);

							new Button(fechagroup, SWT.RADIO).setText("1 anio");
							new Button(fechagroup, SWT.RADIO).setText("3 anios");
							new Button(fechagroup, SWT.RADIO).setText("5 o mas anios");



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
