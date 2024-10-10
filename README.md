# ✈️ **Jet API** - Flight Reservation System 🚀

Welcome to the **Jet API** – your one-stop service for managing airlines, airports, users, and their preferences in a seamless flight reservation system. Whether you're building a booking system or integrating airline data, this API has got you covered!

## 🌐 **Base URL**

https://api.jetblue.com/v1/

---

## 🛫 **Airline Endpoints**

### 1. **Create an Airline**
- **Method:** `POST`
- **Endpoint:** `/airline`
- **Description:** Add a new airline to the system.
- **Response:** Airline created successfully.

### 2. **Get Airline Info**
- **Method:** `GET`
- **Endpoint:** `/airline/{airlineId}`
- **Description:** Retrieve details about a specific airline.
- **Response:** Detailed airline information.

### 3. **Update Airline Info**
- **Method:** `PUT`
- **Endpoint:** `/airline/{airlineId}`
- **Description:** Modify details of an existing airline.
- **Response:** Airline updated successfully.

### 4. **Delete Airline**
- **Method:** `DELETE`
- **Endpoint:** `/airline/{airlineId}`
- **Description:** Remove an airline from the system.
- **Response:** Airline deleted.

---

## 🏙 **Airport Endpoints**

### 1. **Create an Airport**
- **Method:** `POST`
- **Endpoint:** `/airport`
- **Description:** Add a new airport to the system.
- **Response:** Airport created successfully.

### 2. **Update Airport Info**
- **Method:** `PATCH`
- **Endpoint:** `/airport/{airportId}`
- **Description:** Modify the details of an existing airport.
- **Response:** Airport information updated.

### 3. **Get Airport Info**
- **Method:** `GET`
- **Endpoint:** `/airport/{airportId}`
- **Description:** Retrieve details about a specific airport.
- **Response:** Detailed airport information.

### 4. **Get All Airports**
- **Method:** `GET`
- **Endpoint:** `/airport`
- **Description:** List all airports in the system.
- **Response:** List of all airports.

### 5. **Delete an Airport**
- **Method:** `DELETE`
- **Endpoint:** `/airport/{airportId}`
- **Description:** Remove an airport from the system.
- **Response:** Airport deleted.

---

## 👥 **User Endpoints**

### 1. **Get User Info**
- **Method:** `GET`
- **Endpoint:** `/user/{userId}`
- **Description:** Retrieve details about a specific user.
- **Response:** User information.

### 2. **Create a User**
- **Method:** `POST`
- **Endpoint:** `/user`
- **Description:** Add a new user to the system.
- **Response:** User created successfully.

### 3. **Modify User Info**
- **Method:** `PATCH`
- **Endpoint:** `/user/{userId}`
- **Description:** Modify the details of an existing user.
- **Response:** User information updated.

---

## 💼 **User Preferences Endpoints**

### 1. **Get User Preferences**
- **Method:** `GET`
- **Endpoint:** `/user/{userId}/preferences`
- **Description:** Retrieve the preferences of a user (e.g., preferred airlines, seating preferences).
- **Response:** User preferences data.

### 2. **Set User Preferences**
- **Method:** `POST`
- **Endpoint:** `/user/{userId}/preferences`
- **Description:** Set the preferences for a user.
- **Response:** Preferences set successfully.

### 3. **Update User Preferences**
- **Method:** `PATCH`
- **Endpoint:** `/user/{userId}/preferences`
- **Description:** Update an existing user's preferences.
- **Response:** Preferences updated successfully.

---

## 🛠 **Usage Guide**

1. **Test the API** using tools like [Postman](https://www.postman.com/) or command-line with `curl`.
2. **Base URL** is the starting point for all API calls:

https://api.jetblue.com/v1/

3. **Example Request:**
```bash
curl -X GET "https://api.jetblue.com/v1/airline/123" -H "accept: application/json"
```

🔒 Authentication

	•	API Key or OAuth 2.0 depending on the setup.
	•	Ensure that all requests contain the correct Authorization headers to access restricted endpoints.

📊 Response Format

All responses will be in JSON format:
```json
{
  "status": "success",
  "data": {
    "message": "Airline created successfully."
  }
}
```

❗️ Error Handling

	•	400 Bad Request: Invalid input parameters or request body.
	•	404 Not Found: The requested resource does not exist.
	•	500 Internal Server Error: A problem occurred on the server.

🛡 Rate Limiting

	•	The API enforces rate limits to ensure fair usage. If you exceed the rate limit, you will receive a 429 Too Many Requests response.

[//]: # (💡 Need Help?)

[//]: # ()
[//]: # (	•	Reach out to the Jet Blue API Support Team for assistance or queries.)

[//]: # (	•	Explore our API Documentation for detailed explanations.)

Enjoy building with Jet Blue API! 🚀
