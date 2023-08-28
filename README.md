# Parking Slot Reservation Service

### Introduction

Spring Boot service that exposes REST API for parking splot reservation. Current scope is reduced only to make reservations.

### Main functionality of REST interface:

- reservation creation 
- reservation activate
- reservation canceled

### Main functionality on backend side:

Backend has few "workers" which monitoring reservation state 
In `outbox` table are stored events about reservation transition states. Monitored events: 
- reservation is about to start  
- reservation is about to end
- registration that lasts longer than declared 

Reservation is done by calculating car size and contest it against available parkingSlots on 
given parking.

E.g. Car size `COMPACT` fit to each parking size when `MEDIUM` car size only for `MEDIUM` 
and `LARGE` parking size.
Algorithm first tries to find most suitable parking spot, if not found then tries to find bigger 
up to the `LARGE` parking slot size. I case of lack available parking slot in given parking
proper exception is thrown.


### How to build

To build this project run `docker-compose build`. It will build application jar along with docker image.

### How to run

To start this application run `docker-compose up`. Application will start and listen on `localhost:9090`

### How to populate with data
The project contains the file `seed_data.sql` under the path `source/main/resources/db/demo` containing the data
needed to run the system in demo mode

To add those data to db establish connection with postgresql db (all required data are available in `application.yml`)
Database is available during application runtime under url `jdbc:postgresql://localhost:5432/reservation_db`
After successful connection simply paste those data and execute.

# REST API

Below you can find REST API description.

### 1. Get all users

### Request

`POST http://localhost:9090/api/v1alpha1/users`


### Response

    [
    {
        "id": "9a306cb4-0c4f-4ce6-8a49-043fc10bc210",
        "firstName": "John",
        "lastName": "Doe",
        "birthDate": "2000-12-17",
        "email": "john.doe@gmail.com",
        "address": {
            "streetLine1": "Powstańców",
            "streetLine2": null,
            "city": "Kraków",
            "country": "PL",
            "postcode": "21234"
        },
        "createdDate": "2023-08-28T15:08:23.945531",
        "updatedDate": "2023-08-28T15:08:23.945531"
    },
    {
        "id": "9a306cb4-0c4f-4ce6-8a49-043fc10bc211",
        "firstName": "Mark",
        "lastName": "Whisky",
        "birthDate": "1990-12-17",
        "email": "mark.whisky@gmail.com",
        "address": {
            "streetLine1": "Generałów",
            "streetLine2": null,
            "city": "Kraków",
            "country": "PL",
            "postcode": "12345"
        },
        "createdDate": "2023-08-28T15:08:23.945531",
        "updatedDate": "2023-08-28T15:08:23.945531"
    }
    ]

### 2. Get user

### Request

`GET http://localhost:9090/api/v1alpha1/users/:userId`


### Response

    {
    "id": "9a306cb4-0c4f-4ce6-8a49-043fc10bc211",
    "firstName": "Mark",
    "lastName": "Whisky",
    "birthDate": "1990-12-17",
    "email": "mark.whisky@gmail.com",
    "address": {
        "streetLine1": "Generałów",
        "streetLine2": null,
        "city": "Kraków",
        "country": "PL",
        "postcode": "12345"
    },
    "createdDate": "2023-08-28T15:08:23.945531",
    "updatedDate": "2023-08-28T15:08:23.945531"
    }

### 3. Get all user cars

### Request

`GET http://localhost:9090/api/v1alpha1/userCars/-/users/:userId`


### Response

    [
    {
        "id": "9a3b820f-9d46-4d72-bd4a-bf5d234991e3",
        "userId": "9a306cb4-0c4f-4ce6-8a49-043fc10bc211",
        "plate": "KNS5116C",
        "carSize": "MEDIUM",
        "createdDate": "2023-08-28T15:08:23.970999",
        "updatedDate": "2023-08-28T15:08:23.970999"
    }
    ]

### 4. Get user car

### Request

`GET http://localhost:9090/api/v1alpha1/userCars/:userCarId/users/:userId`

### Response

    {
    "id": "9a3b820f-9d46-4d72-bd4a-bf5d234991e3",
    "userId": "9a306cb4-0c4f-4ce6-8a49-043fc10bc211",
    "plate": "KNS5116C",
    "carSize": "MEDIUM",
    "createdDate": "2023-08-28T15:08:23.970999",
    "updatedDate": "2023-08-28T15:08:23.970999"
    }

### 5. Get all parkings

### Request

`http://localhost:9090/api/v1alpha1/parkings`

### Response

    [
    {
        "id": "3288c23c-5077-40e1-9d54-c11263c813e1",
        "name": "parking outdoor",
        "type": "OUTDOOR",
        "address": {
            "streetLine1": "Paniczna",
            "streetLine2": "21/2",
            "city": "Kraków",
            "country": "PL",
            "postcode": "33112"
        },
        "createdDate": "2023-08-28T15:08:23.993483",
        "updatedDate": "2023-08-28T15:08:23.993483"
    },
    {
        "id": "3288c23c-5077-40e1-9d54-c11263c813e2",
        "name": "parking indoor",
        "type": "INDOOR",
        "address": {
            "streetLine1": "Spawalnicza",
            "streetLine2": "22/4b",
            "city": "Warszawa",
            "country": "PL",
            "postcode": "41231"
        },
        "createdDate": "2023-08-28T15:08:23.993483",
        "updatedDate": "2023-08-28T15:08:23.993483"
    }
    ]

### 6. Get parking

### Request

`GET http://localhost:9090/api/v1alpha1/parkings/:parkingId`

### Response

    {
    "id": "3288c23c-5077-40e1-9d54-c11263c813e2",
    "name": "parking indoor",
    "type": "INDOOR",
    "address": {
        "streetLine1": "Spawalnicza",
        "streetLine2": "22/4b",
        "city": "Warszawa",
        "country": "PL",
        "postcode": "41231"
    },
    "createdDate": "2023-08-28T15:08:23.993483",
    "updatedDate": "2023-08-28T15:08:23.993483"
    }

### 7. Get all parking slots on parking

### Request

`GET http://localhost:9090/api/v1alpha1/parkingSlots/-/parkings/:parkingId`


### Response

    [
    {
        "id": "002f7a20-3bd4-4633-9388-ca6b091b0c72",
        "parkingId": "3288c23c-5077-40e1-9d54-c11263c813e2",
        "size": "SMALL",
        "availability": "AVAILABLE",
        "createdDate": "2023-08-28T15:08:24.019807",
        "updatedDate": "2023-08-28T15:08:24.019807"
    },
    {
        "id": "002f7a20-3bd4-4633-9388-ca6b091b0c73",
        "parkingId": "3288c23c-5077-40e1-9d54-c11263c813e2",
        "size": "MEDIUM",
        "availability": "AVAILABLE",
        "createdDate": "2023-08-28T15:08:24.019807",
        "updatedDate": "2023-08-28T15:08:24.019807"
    },
    {
        "id": "002f7a20-3bd4-4633-9388-ca6b091b0c74",
        "parkingId": "3288c23c-5077-40e1-9d54-c11263c813e2",
        "size": "LARGE",
        "availability": "NOT_AVAILABLE",
        "createdDate": "2023-08-28T15:08:24.019807",
        "updatedDate": "2023-08-28T15:08:24.019807"
    }
    ]

### 8. Get parking slot 

### Request

`GET http://localhost:9090/api/v1alpha1/parkingSlots/:parkingSlotId/parkings/:parkingId`


### Response

    {
    "id": "002f7a20-3bd4-4633-9388-ca6b091b0c76",
    "parkingId": "3288c23c-5077-40e1-9d54-c11263c813e2",
    "size": "MEDIUM",
    "availability": "NOT_AVAILABLE",
    "createdDate": "2023-08-28T15:08:24.019807",
    "updatedDate": "2023-08-28T15:08:24.019807"
    }

### 9. Get all reservations by user

### Request

`GET http://localhost:9090/api/v1alpha1/reservations/-/users/:userId`


### Response

    [
    {
        "id": "94c57267-05f0-4d6e-8597-e09e82de1064",
        "userId": "9a306cb4-0c4f-4ce6-8a49-043fc10bc212",
        "userCarId": "9a3b820f-9d46-4d72-bd4a-bf5d234991e6",
        "parkingSlotId": "002f7a20-3bd4-4633-9388-ca6b091b0c77",
        "state": "RESERVED",
        "startedDate": "2023-08-29T07:08:24.05098",
        "endedDate": "2023-08-29T12:08:24.05098",
        "createdDate": "2023-08-28T06:08:24.05098"
    },
    {
        "id": "94c57267-05f0-4d6e-8597-e09e82de1062",
        "userId": "9a306cb4-0c4f-4ce6-8a49-043fc10bc212",
        "userCarId": "9a3b820f-9d46-4d72-bd4a-bf5d234991e4",
        "parkingSlotId": "002f7a20-3bd4-4633-9388-ca6b091b0c75",
        "state": "OCCUPIED",
        "startedDate": "2023-08-28T10:08:24.05098",
        "endedDate": "2023-08-28T21:08:24.05098",
        "createdDate": "2023-08-28T06:08:24.05098"
    },
    {
        "id": "94c57267-05f0-4d6e-8597-e09e82de1063",
        "userId": "9a306cb4-0c4f-4ce6-8a49-043fc10bc212",
        "userCarId": "9a3b820f-9d46-4d72-bd4a-bf5d234991e5",
        "parkingSlotId": "002f7a20-3bd4-4633-9388-ca6b091b0c76",
        "state": "IN_USE",
        "startedDate": "2023-08-28T07:08:24.05098",
        "endedDate": "2023-08-28T20:18:24.05098",
        "createdDate": "2023-08-28T06:08:24.05098"
    }
    ]


### 10. Get reservation

### Request

`GET http://localhost:9090/api/v1alpha1/reservations/:reservationId/users/:userId`


### Response

    {
    "id": "94c57267-05f0-4d6e-8597-e09e82de1064",
    "userId": "9a306cb4-0c4f-4ce6-8a49-043fc10bc212",
    "userCarId": "9a3b820f-9d46-4d72-bd4a-bf5d234991e6",
    "parkingSlotId": "002f7a20-3bd4-4633-9388-ca6b091b0c77",
    "state": "OCCUPIED",
    "startedDate": "2023-08-28T07:08:24.05098",
    "endedDate": "2023-08-28T12:08:24.05098",
    "createdDate": "2023-08-28T06:08:24.05098"
    }


### 11. Create reservation

### Request

`POST http://localhost:9090/api/v1alpha1/reservations`

Example:

    {
    "userId": "9a306cb4-0c4f-4ce6-8a49-043fc10bc211",
    "userCarId": "9a3b820f-9d46-4d72-bd4a-bf5d234991e3",
    "parkingId": "3288c23c-5077-40e1-9d54-c11263c813e2",
    "startTime": "2023-08-29T07:08:24.05098Z",
    "endTime": "2023-08-29T15:08:24.05098Z"
    }

### Response

    {
    "id": "5f244841-1521-4ca7-9561-955934c265e8",
    "userId": "9a306cb4-0c4f-4ce6-8a49-043fc10bc211",
    "userCarId": "9a3b820f-9d46-4d72-bd4a-bf5d234991e3",
    "parkingSlotId": "002f7a20-3bd4-4633-9388-ca6b091b0c73",
    "state": "RESERVED",
    "startedDate": "2023-08-29T07:08:24.05098",
    "endedDate": "2023-08-29T15:08:24.05098",
    "createdDate": "2023-08-28T16:17:33.102891435"
    }


### 12. Cancel reservation

### Request

`PATCH http://localhost:9090/api/v1alpha1/reservations/:reservationId/users/:userId:cancel`

### Response

    {
    "id": "94c57267-05f0-4d6e-8597-e09e82de1061",
    "userId": "9a306cb4-0c4f-4ce6-8a49-043fc10bc210",
    "userCarId": "9a3b820f-9d46-4d72-bd4a-bf5d234991e2",
    "parkingSlotId": "002f7a20-3bd4-4633-9388-ca6b091b0c73",
    "state": "CANCELED",
    "startedDate": "2023-08-28T07:08:24.05098",
    "endedDate": "2023-08-28T09:08:24.05098",
    "createdDate": "2023-08-28T06:08:24.05098"
    }


### 13. Activate reservation

### Request

`PATCH http://localhost:9090/api/v1alpha1/reservations/:reservationId/users/:userId:activate`

### Response

    {
    "id": "94c57267-05f0-4d6e-8597-e09e82de1061",
    "userId": "9a306cb4-0c4f-4ce6-8a49-043fc10bc210",
    "userCarId": "9a3b820f-9d46-4d72-bd4a-bf5d234991e2",
    "parkingSlotId": "002f7a20-3bd4-4633-9388-ca6b091b0c73",
    "state": "IN_USE",
    "startedDate": "2023-08-28T07:08:24.05098",
    "endedDate": "2023-08-28T09:08:24.05098",
    "createdDate": "2023-08-28T06:08:24.05098"
    }
