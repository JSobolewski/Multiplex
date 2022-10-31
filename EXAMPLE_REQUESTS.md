1. Getting info about screenings between given times:

GET http://localhost:8080/api/listScreeningsBetween?screeningTime1=2023-01-11_17:00&screeningTime2=2023-01-12_17:00


2. Getting info about screenining and chosing one to book:

GET http://localhost:8080/api/getScreeningInfo?chosenScreeningId=2


3. Booking reservation for 3 seats for students in last chosen screening:

POST http://localhost:8080/api/registerReservation
Content-Type: application/json

{
  "seatsArrayWithChosenSeats": [
    ["o", "o", "o", "o", "o"],
    ["o", "o", "o", "o", "o"],
    ["o", "o", "x", "x", "x"],
    ["o", "o", "o", "o", "o"],
    ["o", "o", "o", "o", "o"]
  ],
  "multiplexUser": {
    "user_id": 1,
    "name": "Jakub",
    "surname": "Sobolewski",
    "userType": "STUDENT"
  }
}