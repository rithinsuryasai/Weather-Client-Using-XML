A Weather-Client-Using-XML
---

Description
---
A client application that will connect to the National Weather Service web site using HTTP and XML and/or SOAP and display current weather conditions. The client process will connect to the server over a socket connection and request weather information for a certain location. The National Weather Service specifies the location using latitude and longitude instead of zip code. The user should be able to enter coordinates into the client program and get a current update for that location. This service is only updated hourly, so you should not request updates at short intervals. We have a manual refresh button that will reconnect and retrieve the information again.  



Running Instructions
---
* The GUI will be displayed and input the latitude and longitudes then hit the "get weather request" to display the weather parameters.
* Can refresh the data by hitting on "request" button.
* The weather attributes wind speed, wind direction, cloud cover amount and 12HR probability will be displayed.
* Session disconnects after every fetch.


Assumptions
---
All the required packages should be imported to run this program.   
1)gov.weather.graphical.dwml.scheme.DWML_xsd  
URL:  https://graphical.weather.gov/xml/DWMLgen/schema/DWML.xsd   
2)gov.weather.graphical.xml.DWMLgen.wsd_wsdl  
3)longitude input must be starting with negative.  

Test Data
---
Texas latitude:31.9686      longitude:-99.9018  
California latitude:36.7783      longitude:-119.4179  

Objective
---
1. Exposure to XML & SOAP
2. Using Sockets on the WWW

References
---
* http://w1.weather.gov/xml/current_obs/  Web page describing the service and
  giving lots of other links. By following the links at this site you should be able to
  get a quick and easy introduction to Web Services and how they are exposed via
  SOAP and accessed via XML. You should also find tool kits for several different
  environments.
