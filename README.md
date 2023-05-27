<h2>Data collecting tool</h2>

<b>Technology Used</b><br/>
<code>spring-boot:2.6.3</code><br/>
<code>spring-boot-starter-webflux</code><br/>
<code>camel-core:3.0.1</code><br/>
<code>camel-ftp:3.0.1</code><br/>
<code>camel-csv:3.0.1</code><br/>


<b>FTP SERVER Configuration</b><br/>
Set information on application-live.properties file

ftp.host = "IP address or domain"<br/>
ftp.port = 21 "If use 22 port you may change configuration"<br/>
ftp.username = "Hosting username as FTP username"<br/>
ftp.password = "FTP user password"<br/>
ftp.directory = "The directory, where csv file is stored"<br/>
ftp.filename = "CSV file name with .csv, example data.csv"


<b>Rapidapi.com Configuration</b><br/>
Used "hotels4.p.rapidapi.com" Hotel API from rapidapi.com

RapidAPI.Key = "Rapidapi.com will provide Key after subscription"
RapidAPI.Host = hotels4.p.rapidapi.com


<b>api.openweathermap.org Configuration</b><br/>
Used "api.openweathermap.org" API for Weather Information

weather.appid = "openweathermap.org will provide appid after subscription"


<b>Local xml file store path configuration</b><br/>
Generate XML file and to this path, than upload files to FTP Server

local.xml.path = "example C:/data"
