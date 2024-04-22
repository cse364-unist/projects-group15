mongod --fork --logpath /var/log/mongodb.log
cd milestone2
mvn package
java -jar ./target/cse364-project-1.0-SNAPSHOT.jar