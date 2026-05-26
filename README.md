# If using docker compose:


docker compose down --rmi all

./gradlew clean build

docker compose up --build


# If using normal docker

./gradlew clean build

docker build -t nitnem-tracker .

docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=dev nitnem-tracker
OR
docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=dev -e NITNEM_BOT_TOKEN=YOUR_TOKEN -e DB_URL='jdbc:postgresql://YOUR_DB_URL' -e DB_USERNAME=YOUR_USERNAME -e DB_PASSWORD=YOUR_PASSWORD nitnem-tracker
