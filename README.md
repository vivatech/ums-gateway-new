# ums-gateway-new

# Deployment Server
- IP - 139.84.167.86
- Port - 8060

# Architecture
- Java version - 1.8
- Maven version - apache-maven-3.8.2

# Jar file location 
 - ums-gateway-new/target/Gateway.jar

# Log file path
- /home/core/gateway/Gateway.log

# Properties file path
- /home/core/ums/gateway/application.properties
- /home/core/ums/gateway/database.properties

# Command to build the project
mvn clean install

# Command to run the project
Find the script file to start, stop and check the status of the application.
/home/core/scripts
- start.sh
- stop.sh
- status.sh

The start.sh runs the registry and gateway

The stop.sh stops the registry and gateway

The status.sh checks the status of the registry and gateway application

Please execute this once for both the applications (registry & gateway) to run.


