# eStockMarket-component3

## Backend:
	### Running Ports on localhost:
   		netflix-eureka (discovery-service) : 8761
   		api-gateway : 8765
   		company-service : 8085
   		stock-service : 8091
   		rabbitmq : 5672 | Management : 15672
   		KeyCloak : 8180
 
 	### uri:
   		Eureka : http://localhost:8761
   		Spring Docs : http://localhost:8085/v3/api-docs
   		Actuator-Monitoring : http://localhost:8085/actuator
   		Open API - Swagger doc : http://localhost:8085/swagger-ui.html
   		Elastic Search : http://localhost:9200
   		Kibana : http://localhost:5601
   		zipkin : http://localhost:9411
   
## SonarQube : http://localhost:9000
	sonar security : ################################
	maven goal : sonar:sonar -Dsonar.login = <sonar security>	
     POM.xml -> plugin
       <plugin>
		<groupId>org.sonarsource.scanner.maven</groupId>
		<artifactId>sonar-maven-plugin</artifactId>
		<version>3.8.0.2131</version>
	</plugin>
			
	<plugin>
		<groupId>org.jacoco</groupId>
		<artifactId>jacoco-maven-plugin</artifactId>
		<version>0.8.5</version>
		<executions>
			<execution>
				<id>prepare-agent</id>
				<goals>
					<goal>prepare-agent</goal>
				</goals>
			</execution>
			<execution>
				<id>report</id>
				<goals>
					<goal>report</goal>
				</goals>
			</execution>
		</executions>
      	</plugin>
 
 
## Docker :
	maven goal : spring-boot:build-image -DskipTests
  	POM.xml -> plugin
  	<plugin>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-maven-plugin</artifactId>
		<configuration>
			<image>
				<name>#####/%%%%%-${project.artifactId}:${project.version}</name>
			</image>
			<pullPolicy>IF_NOT_PRESENT</pullPolicy>
		</configuration>
	</plugin>
 
## React libs :
   		npm install bootstrap --save
     			Add in index.js : import 'bootstrap/dist/css/bootstrap.min.css';
 
   		npm install axios
   		npm install react-router-dom
   		npm install --save-dev http-proxy-middleware
   		npm run build (to create the production build)
 
## Keycloak : 
   	navigate to keycloak-16.1.1\bin >> standalone.bat -Djboss.socket.binding.port-offset=100 (to Keycloak run on different port(8180) other than default port 8080)
  
