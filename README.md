khsSherpa [![Build Status](https://secure.travis-ci.org/in-the-keyhole/khs-sherpa.png?branch=master)](http://travis-ci.org/in-the-keyhole/khs-sherpa)
==========

Easy creation of JSON/API end points with Java POJOs  

About
-----
khsSherpa allows you to turn Java application servers into remote JSON data access mechanisms for mobile and HTML5/JavaScript applications. 

This lightweight server side framework allows Java classes (POJOs) contained inside a JEE application server
to become JSON end points that can be consumed via HTTP by native mobile devices or HTML/JavaScript clients. 

Many MVC frameworks exist, but khsSherpa is intended to allow access to server side Java objects with HTTP/and JSON. It 
also provides session support for client applications that exist outside of a browser.

Additionally, khsSherpa easily integrates with the Spring framework (see Spring Configuration section).

Example RESTful Endpoints
-------------------------
Example khsSherpa endpoint implementation invoked with the following 
RESTful URLs: 

http://<host>/<webapp>/sherpa/helloworld

http://<host>/<webapp>/sherpa/add/10/20
    	
	@Endpoint(authenticated=false)
	public class RestfulEndpoint {
		
	   @Action(mapping = "/helloworld", method = MethodRequest.GET)
	   public String helloWorld() {
		 return "hello world";
	   }	
	
	   // add two numbers method
	   @Action(mapping = "/add/{x}/{y}", method = MethodRequest.GET)
	   public Double add(@Param("x") double x, @Param("y") double y) {
			return new Double(x + y);
	   }
	
     }	

Features  
--------
 * Annotation-based Configuration
 * RESTful Service URL Support
 * Spring Support
 * Authentication and Role-based Permissions
 * JSONP Cross Domain Support 
 * Session Support 
 * Plug-gable User Activity Logging
 * Type Mapping
 * XSS prevention support
 * Works with any JEE application server
 * Plug-gable JSON Serialization

Getting Started
---------------

Using Maven: add this dependency in your 'pom.xml' (available in Maven central repo)

    <dependency>
   	 <groupId>com.keyholesoftware</groupId>
   	 <artifactId>khs-sherpa</artifactId>
   	<version>1.3.0</version>
    </dependency>
   
Not using Maven: include following jars in lib class path

    khs-sherpa-1.3.0.jar
	gson-2.2.1.jar
	commons-lang3-3.1.jar

To build it, clone then install in local Maven repo:

    $ git clone ...
	$ cd khs-sherpa
	$ mvn install
	
Quick Start 
----------
Configure and create Java server side end point in a WAR project
	
	1) Register Sherpa Servlet in WEB.XML (see configuring WEB.XML below)
	
	2) Create the following Java class in a package named com.khs.example.endpoint
	
	@Endpoint(authenticated = false)
	public class TestEndpoint {
	
	// hello world  method
	@Action(mapping = "/helloworld", method = MethodRequest.GET)
	public Result helloWorld() {
		return new Result("Hello World");
	}
		
		class Result {	
			public Result(Object o) {
			result = o;
			}
			public Object result;		
		}
	}
	 

	3) Create sherpa.properties file in your project resource/classpath folder and add this entry:
	   
	endpoint.package=com.khs.example.endpoints
	   
	4) Start app server and in a browser enter the following URL:

	http://<server>/<webapp>/sherpa/helloworld	

Configuring WEB.XML
-------------------
Add the khsSherpa framework jar to your classpath/maven dependency list and add the 
SherpaServlet to the WEB-INF/web.xml as shown below: 

  	<listener>
  		<listener-class>com.khs.sherpa.SherpaContextListener</listener-class>
  	</listener>

    <servlet>	
  		<servlet-name>sherpa</servlet-name>
		<display-name>sherpa</display-name>
		<servlet-class>com.khs.sherpa.servlet.SherpaServlet</servlet-class>	
	</servlet>
	<servlet-mapping>
		<servlet-name>sherpa</servlet-name>
		<url-pattern>/sherpa</url-pattern>
	</servlet-mapping>
	
	<servlet-mapping>
		<servlet-name>sherpa</servlet-name>
		<url-pattern>/sherpa/*</url-pattern>
	</servlet-mapping>
	
Configuring khsSherpa
---------------------
Define a sherpa.properties file in your webapp classpath. The only required entry is 
the endpoint.package entry, which tells khsSherpa where to find Java end points. 

    ##khsSherpa server properties

    #package where endpoints are located
    endpoint.package=com.khs.example.endpoints
    
Spring Configuration
--------------------
Sherpa is built so it can be managed by Spring's IOC managed container. Simply add the following 
dependency to your maven POM.XML:

	<dependency>
		<artifactId>khs-sherpa-spring</artifactId>
		<groupId>com.keyholesoftware</groupId>
		<version>1.2.1</version>
	</dependency>

Sherpa will use the managed bean factory implementation in the dependency and allow you to 
configure beans or @Autowire endpoints in an application context. 

Here's a RESTful customer JSON end point that returns a Customer for a specified ID...     

    @Endpoint(authenticated = false)
	public class CustomerEndpoint {
	
	@Autowired
	CustomerDao dao;
	
	@Action(mapping = "/customer/{id}", method = MethodRequest.GET)
	public Customer customerForId(@Param("id") Long id) {
		return dao.findById(id);
	}
		
	}

Spring authentication is also integrated, check it out here...[https://github.com/in-the-keyhole/khs-sherpa-spring] 


Endpoint and Action Naming Conventions
--------------------------------------
By default, endpoint names are a class' simple name and action names are method names. 
Alternative names can be provided by specifying the value attribute of the @Endpoint annotation 
as shown below: 

	@Endpoint("HelloEndpoint")
	public class HelloWorld { 
	
Action names are specified by applying the @Action annotation to a method implementation, as shown below: 

	@Action(url="/hello",method=GET);  // other method options PUT|REMOVE
    public String sayHello() { 

Non RESTful access

	@Action("hello")
	public String sayHello() {
	
The @Action annotation can also be specified to disable a method from execution with the disabled attribute. 

	@Action(disabled=true)
	public String myMethod() { 	
	
RESTful-based Action URLs
-------------------------
End point action methods can be mapped to a restful URL by applying the mapping attribute in the @Action 
annotation. An example restful mapping for the helloworld action is shown below:

	@Action(mapping = "/helloworld", method = MethodRequest.GET)
	public String helloWorld() {		
		return "hello world";
	}

End point action can then be accessed with the following RESTful URL: 

	http:<server>/<webapp>/sherpa/helloworld

Parameters can also be mapped to a RESTful URL by including a delimited parameter name in the mapping. 
The example below shows how a Vendor lookup id is mapped into the URL:
	
	@Action(mapping = "/findbyid/{id}", method = MethodRequest.GET)
	public Vendor fetchOne(@Param("id") Integer id) {
		return repository.findOne(id);
	}
	
A RESTful URL specifying a Vendor lookup ID for the above action is shown below: 	
	
	http:<server>/<webapp>/sherpa/findbyid/100


Example Implementations
-----------------------
Example Web application - https://github.com/in-the-keyhole/khs-sherpa-example-webapp

Working HTML5 JQuery Mobile - http://sherpa.keyholekc.com

HTML5 JQuery Mobile application on GitHub https://github.com/in-the-keyhole/khs-sherpa-jquery
	
Test Fixture
------------
A testing jsp, test-fixture.jsp has been created that will allow testing of khsSherpa end points. Copy this 
file into your web contents web app directory and access the test-fixture.jsp with a browser. You will then be able to invoke @Endpoint 
methods and view JSON results.  

Steps for testing JSON end points from a web app:

	create a java webapp project
	
	copy test-fixure.jsp into web content folder
	
	define a java class and annotate with @Endpoint, see above example
	
	define end point package name in sherpa.properties file
	
	start your webapp
	
	open text-fixture.jsp with this url 
	
		http://<server>/<webapp>/test-fixture.jsp
		
	Fill in end point simple class name, method name and parameter names and submit	


Authentication
--------------
End points can be configured to require authentication by setting the authentication attribute to true 
on the @Endpoint annotation. Authenticated end points can only be invoked if a valid user ID and token
ID are supplied. Valid token IDs are obtained by invoking the framework authenticate action with valid 
credentials. 

An example authentication request URL is shown below: 

	http://<server>/<webapp>/sherpa?action=authenticate&userid=dpitt@keyholesoftware.com&password=password
         
If valid credentials, the following JSON token object will be returned: 

	{
	    "token": "1336103738643",
	    "timeout": 0,
	    "active": true,
	    "userid": "dpitt@keyholesoftware.com",
	    "lastActive": 1336103738643
	}	

The token ID and user ID values are supplied as parameters to @Endpoint method calls.
Authenticated URL's with token parameters will look like this:

	http://<server>/<webapp>/sherpa?endpoint=TestService&action=helloWord&userid=dpitt@keyholesoftware.com&token=1336103738643

Authentication Interface Implementation
---------------------------------------
The default authentication mechanism denies all credentials. Since various authentication mechanisms exist,
the framework supplies an interface: com.khs.sherpa.json.service.UserService. Concrete UserService implementations
are registered by defining the entry below in the sherpa.properties file:  

	## Authentication implementation
	user.service = com.example.LDAPUserService
	
Authentication implementations validate a user ID and password and return a list of valid roles for the user ID. Roles 
are only required if role-based authentication is applied.

Session Timeout
---------------

An authenticated user token will also define a timeout period in milliseconds. 0 indicates that a session will never timeout.
For timeout values greater than zero, the framework requires a new authentication token to be obtained through the 
authentication mechanism in order to continue to access an authenticated end point. Session timeouts can be set with 
the sherpa.properties file entry below: 

	## Session timeout (ms), default is 0 
	session.timeout=900000 

Role-Based Security
-------------------
JEE javax.security roles can be applied to authenticated end point methods. These annotations are listed below: 
	
	javax.annotation.security.PermitAll
	javax.annotation.security.DenyAll
	javax.annotation.security.RolesAllowed
	
Declared roles for a user are applied to the authenticated users token. An example end point method that is 
restricted to only users with a "admin" role is shown below: 

	@RolesAllowed({"admin"})
	public Department create(@Param("number") int number,@Param("name")String name) {	
		Department dept = new Department();
		dept.number = number;
		dept.name = name;		
		return dept;		
	}	


Token Service
-------------
By default, the framework supplies a default session token service implementation that maintains and in-memory mapping of 
user token's authenticated users. Token IDs are generated by current milliseconds from the JVM. Sessions are active for specified
timeout periods and as long as the web application is started. 

If this default behavior is not sufficient, it can be replaced with an alternative implementation by implementing the framework 
supplied TokenService interface and registering it in the sherpa.properties file as shown below: 

JSONP Cross Domain Support
--------------------------
JSONP-based URLs are supported by adding a callback parameter to a Sherpa URL as shown below: 

	http://<server>/<webapp>/sherpa?endpoint=TestService&action=helloWord&callback=<function name> 

The framework will return a JSONP formatted result.

By default, JSONP is disabled and the callback function will be ignored. To enable, set the sherpa.properties value shown below: 

	## Enable JSONP Support, default is false
	jsonp.support = true  

Data Type Mappings
------------------

khsSherpa maps request parameter types to Java method argument types. The following mappings are applied: 

	HTTP Request				Java
	
	Abc							String
	1.0							Float/Double/float/double
	1							Integer/Long/int/long
	0,1,y,n						Boolean/boolen
	mm/dd/yyyy					Date
	mm/dd/yyyy hh:mm:ss am		Calendar
	JSON String					Java Class Type
	
Date/Time format types can be changed framework wide by configuring the date.pattern or datetime.pattern in 
the sherpa.properties file. An example is shown below: 

	## Date format for date types, default is MM/dd/yyyy
	date.format=MM/dd/yyyy
	date.time.format=MM/dd/yyyy hh:mm:ss a

Date/Time format types on an @Endpoint method level can be changed by specifying the format attribute on on @Param annotation
as shown below: 

	public Result time(@Param(value="cal", format="hh:mm:ss a") Calendar cal) {
		return new Result(cal);
	}

Encoding/XSS protection
-----------------------

End points with String parameter types can be automatically encoded to XML, HTML, or CSV formats. Encoding helps 
prevent XSS attacks from browser based clients. 

Encoding format for all String parameters can be enabled by setting the encode.format property in the 
sherpa.properties file as shown below: 

	encode.format = <possible values: HTML,XML,CSV>
	
Encoding can be applied at an end point action level by specifying the encoding format type in the
@Param annotation. An example is shown below: 

	public Result encode(@Param(value="value",format=Encode.HTML) String value) {
		return new Result(value);	
	}

Activity Logging
----------------

By default, end point execution will be logged via the java.util.logging.Logger. This can be turned off by setting 
the property below in sherpa.properties file. 

	acitivity.logging=false
	
An alternative logging implementation can be supplied and configured by implementing the com.khs.sherpa.ActivityService  
interface and registering in the sherpa.properties file as shown below: 

	activity.service.impl = <<qualified class name that implements com.khs.sherpa.service.ActivityService>>
 
JSON Serialization
------------------

By default Sherpa uses the googles GSON open source JSON serialization framework, however Sherpa is designed to allow 
alternative serialization frameworks to be used. The example below illustrates shows how the Jackson serialization 
framework can provide serialization support, with an entry in sherpa.properties.

	## Jackson JSON Serialization Provider
	json.provider=com.khs.sherpa.json.service.JacksonJsonProvider

Serialization providers other than Jackson or GSON can be created by implementing the following interface. 

	public interface JsonProvider {

	    public String toJson(Object object);
	
	    public <T> T toObject(String json, Class<T> type);
	}

Session Management Commands
---------------------------

Framework specific end point is available that will return active user sessions, and describe end point meta data. 
These actions must be invoked using the userid, passwords, and token for a user authenticated with the framework 
"SHERPA_ADMIN" role.

	Current Sessions
	
	http://<server>/<webapp>/sherpa?action=sessions&adminuserid=dpitt@keyholesoftware.com&adminpassword=password
	
  	Describe end point
  	
  	http://<server>/<webapp>/sherpa?action=describe&adminuserid=dpitt@keyholesoftware.com&adminpassword=password
  
  
  	Deactivate User id
  	
  	http://<server>/<webapp>/sherpa?action=deactivate&deactivate=jdoe@keyholesoftware.com&adminuserid=dpitt@keyholesoftware.com&adminpassword=password
  	

  	
  	
  
  
  

  


[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/in-the-keyhole/khs-sherpa/trend.png)](https://bitdeli.com/free "Bitdeli Badge")

