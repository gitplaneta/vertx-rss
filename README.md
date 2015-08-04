To compile:
mvn clean package

To run:
java -jar target/target/vertx-rss-1.0-SNAPSHOT-fat.jar

Available endpoints under http://localhost:8080
GET: 
/
/:id
/?alternate
/?latest

All endpoints produce XML and JSON format, depending on the Accept header (Accept: application/json or Accept: application/xml)