Used technologies (included as Maven dependencies): 

  *GWT 2.5.0
  
  *GXT GPL 3.0.1
  
  *Hibernate 3.2.6
  
  *Gilead 1.3.2
  
  *PostgreSQL JDBC driver 9.1
  
Application server: JBoss 7.1.1 final

To deploy to the running Jboss instance: 
(Maven 3.0.4)
  * execute mvn package (will create WAR file named Fotiusnet.war)
  * execute mvn jboss-as:deploy (will deploy the created WAR)

Accessing the deployed application: http://{jboss.url}/Fotiusnet/FotiusNet.html

