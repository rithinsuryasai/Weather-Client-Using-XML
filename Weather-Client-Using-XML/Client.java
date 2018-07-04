import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis.AxisFault;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import gov.weather.graphical.xml.DWMLgen.schema.DWML_xsd.ProductType;
import gov.weather.graphical.xml.DWMLgen.schema.DWML_xsd.UnitType;
import gov.weather.graphical.xml.DWMLgen.schema.DWML_xsd.WeatherParametersType;
import gov.weather.graphical.xml.DWMLgen.wsdl.ndfdXML_wsdl.NdfdXMLBindingStub; 

/**
 * 
 * description - This is a client program which takes inputs as latitude and longitude and returns the weather parameters
 * maximum temperature or minimum or dew point as specified in response xml and wind speed, wind direction, cloud amount and 12 hr probability.
 */

public class Client {
	//declaring everything needed for GUI purpose
	private static HttpURLConnection connection ;
	
	private static URL url ; //to provide the server request via this url
	
	private static JFrame frame = new JFrame();  //the main frame of gui declaration
	
	private static JLabel latitudeLabel = new JLabel("Input Latitude:"); //latitude label declaration
	private static JLabel longitudeLabel = new JLabel("Input Longitude:");//longitude label declaration
	
	private static JTextField latitudefield = new JTextField(); //text fields for latitude and longitude
	private static JTextField longitudefield = new JTextField();
	
	private static JButton refresh = new JButton("Refresh");  //refresh button declaration
	
	public static JTextArea Area = new JTextArea();
	
	private static JButton info = new JButton("Request current update"); //request button declaration

	public static JScrollPane ScrollPane = new JScrollPane();
	
	public static Calendar startTime, endTime ;
	
	Date dateformat  ; 
	
	public static ProductType productType ;  
	public static UnitType unitType ;
	public static WeatherParametersType weatherParameterType ;
	
	
	/**
	 * constructor - called whenever a new object is created.
	 * which automatically calls GUI which we can enter inputs latitude and longitude 
	 * then request for weather updates or refresh them.
	 */
	public Client(){
		String title = "A Weather Client";	//GUI title
		frame.setVisible(true);
		frame.setSize(800,400);
		frame.getContentPane().setLayout(null);
		frame.setTitle(title);	
		
		frame.getContentPane().add(latitudeLabel);	
		latitudeLabel.setBounds(10,10,80,20);		

		
		frame.getContentPane().add(longitudeLabel); 
		longitudeLabel.setBounds(200,10,80,20);		

		
		frame.getContentPane().add(latitudefield);	
		latitudefield.setBounds(90,10,80,20);

		
		frame.getContentPane().add(longitudefield);	
		longitudefield.setBounds(300,10,80,20);

		frame.getContentPane().add(refresh); 
		refresh.setBounds(15,350,200,25);
		
		frame.getContentPane().add(info);
		info.setBounds(13,300,200,25);

		Area.setColumns(100);
		Area.setRows(20);
		Area.setEditable(false);

		ScrollPane.setViewportView(Area);
		frame.getContentPane().add(ScrollPane);
		ScrollPane.setBounds(10,90,330,180);
		frame.getContentPane().add(ScrollPane);
		
		startTime = endTime = Calendar.getInstance() ;
		dateformat = new Date(System.currentTimeMillis());
		startTime.setTime(dateformat);
		endTime.setTime(dateformat);
		
		productType = ProductType.fromValue("time-series") ;
		unitType = UnitType.fromValue("e") ;
		weatherParameterType = new WeatherParametersType();
		
	}
	/**
	 * Connecting to the server end point.
	 * {@link} - https://graphical.weather.gov:443/xml/SOAP_server/ndfdXMLserver.php
	 */
	public static void connect() throws IOException{
		
		url = new URL("https://graphical.weather.gov:443/xml/SOAP_server/ndfdXMLserver.php") ;
		connection = (HttpURLConnection) url.openConnection() ;

	}
	/**
	 * 
	 * @return latitude - returns the latitude from the input box
	 */
	public static String getlatitutde(){ return latitudefield.getText() ; }
	/**
	 * 
	 * @return longitude - returns the longitude from the input box
	 */
	public static String getlongitude(){ return longitudefield.getText() ; }
	
	
	/**
	 * The DOM parser is implemented in java and parsed.
	 * @param s - is the string where the response xml is stored and sent to the below function.
	 * outputs the retreiived data after parsing and appends them to GUI
	 * @throws ParserConfigurationException - exception
	 * @throws SAXException - exception
	 * @throws IOException - exception
	 * 
	 */
	public static void parseXml(String s) throws ParserConfigurationException, SAXException, IOException{// to parse the response xml file
		DocumentBuilder builder ;
		InputSource source ;
		Document document ;
		try{
	    	builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();  //default method for DOM parsing

	    	source = new InputSource();  //specifying input source for parsing
	    	source.setCharacterStream(new StringReader(s)); //setting all the character streams for parsing from XML response

	    	document= builder.parse(source);  //building a document after parsing
	    	/**
	    	 * Getting wind-speed, wind-direction, cloud-amount and probability of precipitation.
	    	 */
	    	String windspeed = document.getElementsByTagName("wind-speed").item(0).getTextContent();  
	    	String winddirection = document.getElementsByTagName("direction").item(0).getTextContent();	
	    	String cloud = document.getElementsByTagName("cloud-amount").item(0).getTextContent();
	    	String prob = document.getElementsByTagName("probability-of-precipitation").item(0).getTextContent();
	    	
	    	/**
	    	 * in temperature tag many child elements are present.
	    	 * By using a loop we are retrieving the minimum or maximum or dew point temperature
	    	 */
	    	NodeList nodes = document.getElementsByTagName("temperature") ;
	    	for(int i=0; i<nodes.getLength(); i++){
	    		
	    		String nodeValue = nodes.item(i).getAttributes().getNamedItem("type").getNodeValue() ;
	    		if (nodeValue.equals( "dew point") || nodeValue.equals( "maximum") || nodeValue.equals( "minimum") )
	    			Area.append(nodes.item(i).getTextContent()+"Fahrenheit\n");
	    		
	    	}
	    	
	    	Area.append(windspeed+"knots\n"+winddirection+"degrees\n"+cloud+"percent\n"+prob+"percent\n");
	    }
	    catch(NullPointerException e){
	    	e.printStackTrace();
	    }
	}
	
	/***
	 * This function is to send request to the web service to retrieve data
	 * takes input given in GUI and retrieves the data from the web service.
	 */
	public static void trigger(){
		try {
			connect() ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Area.setText("");
		NdfdXMLBindingStub bindingstub;
		try {
			bindingstub = new NdfdXMLBindingStub(url, null);//binding the stub with url
			String getresponse;
			try {
				//the request to web server as attributes specified.
				getresponse = bindingstub.NDFDgen(new BigDecimal(Double.parseDouble(getlatitutde())), 
					new BigDecimal(Double.parseDouble(getlongitude())), productType, startTime, 
					endTime, unitType, weatherParameterType);
				
				System.out.println(getresponse);  
				parseXml(getresponse);
				
			} catch (ParserConfigurationException | SAXException | IOException e1) {
				e1.printStackTrace();
			}
		} catch (AxisFault e1) {
			e1.printStackTrace();
		}finally{
				//close of connection after every fetch
			connection.disconnect();
		}
	}
	/**
	 * specifying all the input attributes for the request function NDFD generator
	 * Getting all attribute values ready for request.
	 */
	public static void main(String[] args) throws RemoteException, IOException {
		new Client() ;
		//action for refresh element
		refresh.addActionListener(l-> trigger());
		//action for request element
		info.addActionListener(l-> trigger());
		
	}
}
