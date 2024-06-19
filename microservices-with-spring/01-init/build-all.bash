cp -r ./microservices/product-service/gradle .
cp ./microservices/product-service/gradlew .
cp ./microservices/product-service/gradlew.bat .
cp ./microservices/product-service/.gitignore .


# We no longer need the generated Gradle executable files in each project, so we can remove them 
find microservices -depth -name "gradle" -exec rm -rfv "{}" \;
find microservices -depth -name "gradlew*" -exec rm -fv "{}" \;

./gradlew build


