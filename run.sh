mongod --fork --logpath /var/log/mongodb.log
cd milestone3
mvn clean package
cp ./target/cse364-project.war /opt/tomcat/webapps/
cd /opt/tomcat/bin
./catalina.sh run