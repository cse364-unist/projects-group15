# Start MongoDB server
mongod --fork --logpath /var/log/mongodb.log
git clone https://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@github.com/cse364-unist/projects-group15.git
cd projects-group15 || exit
git checkout milestone2
cd milestone2 || exit
mvn jacoco:report
mvn package
java -jar ./target/cse364-project-1.0-SNAPSHOT.jar