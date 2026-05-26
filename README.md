docker compose down --rmi all

./gradlew clean build

docker compose up --build