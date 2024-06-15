mongod --fork --logpath /var/log/mongodb.log
cd milestone3
mvn clean package
cp ./target/cse364-project.war /opt/tomcat/webapps/
mkdir /opt/tomcat/bin/data
cp ./data/movies.csv /opt/tomcat/bin/data/movies.csv
cp ./data/ratings.csv /opt/tomcat/bin/data/ratings.csv
cp ./data/users.csv /opt/tomcat/bin/data/users.csv
cd /opt/tomcat/conf
sed -i '/<Host name="localhost"/!b;n;n;a\ \ \ \ <Context path="/" docBase="cse364-project" reloadable="false"\/>' /opt/tomcat/conf/server.xml
cd /opt/tomcat/bin
./catalina.sh run