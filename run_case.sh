#!/bin/bash
curl -vs "http://localhost:8080/api/listScreeningsBetween?screeningTime1=2023-01-11_17:00&screeningTime2=2023-01-12_17:00" \
2>&1 | sed '/^* /d; /bytes data]$/d; s/> //; s/< //'

echo "=================================================="

curl -vs "http://localhost:8080/api/getScreeningInfo?chosenScreeningId=2" 2>&1 | sed '/^* /d; /bytes data]$/d; s/> //; s/< //'

echo "=================================================="

curl -vs -X POST 'http://localhost:8080/api/registerReservation' \
	-H 'Content-Type: application/json' \
	-d '
{
  "chosenSeatsArray": [
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
}' 2>&1 | sed '/^* /d; /bytes data]$/d; s/> //; s/< //'

read -n 1 -s -r -p "Press any key to continue..."
