Weather Application
---


### Introduction

This is a simple application that requests its data from [OpenWeather](https://openweathermap.org/) and stores the result in a database.
When the same request is done withing 10 minutes, the report is fetched from the database and returned.

### Initial setup
The Weather App runs on Java 11, which must be installed on your system.

Current application configuration expects PostgreSQL database named `weather_db` to be available at `localhost:5432` with user `weather-app` and password `secret-password`. 
The database configuration can be changed in `src/main/resources/application.yml`.

### Running the Application
The Weather App can be started using following command:
`mvn spring-boot:run -Dspring-boot.run.arguments=--open-weather.api-key=YOUR_API_KEY`

It's possible to generate the API key going to the [OpenWeather Sign up](https://openweathermap.org/appid) page.

### Requests
Requests (GET) can be sent to `http://localhost:8080/reports`

It takes 1 parameter and will return the weather for that location:
`location`: City, Area, or even Country (in English)

### Example
```GET http://localhost:8080/reports?location=Utrecht```

will return something like:
```
{
    "id": 4,
    "location": {
        "id": 2745909,
        "name": "Provincie Utrecht",
        "country": "NL"
    },
    "temperature": 4.61,
    "utcTimestamp": 1615247976
}
```
