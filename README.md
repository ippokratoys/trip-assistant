[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=kostasmantz_trip-assistant&metric=alert_status)](https://sonarcloud.io/dashboard?id=kostasmantz_trip-assistant) [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=kostasmantz_trip-assistant&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=kostasmantz_trip-assistant) [![Bugs](https://sonarcloud.io/api/project_badges/measure?project=kostasmantz_trip-assistant&metric=bugs)](https://sonarcloud.io/dashboard?id=kostasmantz_trip-assistant) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=kostasmantz_trip-assistant&metric=coverage)](https://sonarcloud.io/dashboard?id=kostasmantz_trip-assistant) [![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=kostasmantz_trip-assistant&metric=ncloc)](https://sonarcloud.io/dashboard?id=kostasmantz_trip-assistant)
# Trip-Assistant thesis application
This is an application developed in the scope of my thesis application with subject "Tourist-relevant data extraction from public APIs".
## Getting Started
After reading the instructions below you will have a running tomcat server instance serving an API consumed by the Android client.
### Requirements
You need to have installed Java 8 at least.
### Building
- Clone the repository from github.
- From the root of the project, `cd tripassistant-api`
- Run `./gradlew buid` from UNIX system or `gradlew.bat build` from Windows system.
- Change directory back to the root of the project and run `cd tripassistant-client`
- Run `./gradlew buid` from UNIX system or `gradlew.bat build` from Windows system.
- After successfully building both projects, a runnable jar is produced under *~/tripassistant-api/build/libs/tripassistant-api-{version}-SNAPSHOT.jar* and an apk file under *~/tripassistant-client/app/build/outputs/apk/debug/app-debug.apk*
