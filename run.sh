# Start MongoDB server
mongod --fork --logpath /var/log/mongodb.log
git clone https://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@github.com/cse364-unist/projects-group15.git
cd projects-group15 || exit
git checkout milestone3 || exit
cd milestone3 || exit
mvn jacoco:report
mvn package
java -jar ./target/cse364-project-1.0-SNAPSHOT.jar