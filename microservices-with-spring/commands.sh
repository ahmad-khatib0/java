#!/bin/bash

# -f is like watch, --tail=0 means don’t want to see any previous log messages, only new ones
docker-compose logs -f --tail=0

# create new project
spring init \
  --boot-version=3.0.4 \
  --type=gradle-project \
  --java-version=17 \
  --packaging=jar \
  --name=product-service \
  --package-name=se.magnus.microservices.core.product \
  --groupId=se.magnus.microservices.core.product \
  --dependencies=actuator,webflux \
  --version=1.0.0-SNAPSHOT \
  product-service

# view fat JAR file content
unzip -l microservices/product-service/build/libs/product-service-1.0.0-SNAPSHOT.jar

# in: ./05-persistent-dataa/, run tests:
./gradlew microservices:product-service:test --tests PersistenceTests

# start the MongoDB CLI tool or mysql
docker-compose exec mongodb mongosh --quiet
docker-compose exec mysql mysql -uuser -p review-db
docker-compose exec mongodb mongosh product-db --quiet --eval "db.products.find()"
docker-compose exec mysql mysql -uuser -p review-db -e "select * from reviews"

# health check endpoint
curl localhost:8080/actuator/health -s | jq .

# self-signed certificate
keytool -genkeypair \
  -alias localhost \
  -keyalg RSA \
  -keysize 2048 \
  -storetype PKCS12 \
  -keystore edge.p12 \
  -validity 3650
