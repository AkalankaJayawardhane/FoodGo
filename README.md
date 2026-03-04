# 🍔 FoodGo - Food Ordering Android App

FoodGo is a simple **Android food ordering application** developed using **Java and SQLite**.
The app allows users to browse food items, add them to a cart, and place orders with a delivery address.

This project was developed as part of an **IT undergraduate coursework project**.

---

## 📱 Features

* 🔐 User Registration & Login
* 🍕 Browse food items by category
* 🛒 Add food items to cart
* ➕ Increase / decrease item quantity
* ❌ Remove items from cart
* 💳 Checkout system
* 📍 Enter delivery address
* 📦 Place order
* 🗂 Order saved in SQLite database
* 👤 User profile and logout

---

## 🧱 System Architecture

The app follows a simple **Android MVC-style architecture**.

**Main Components**

* Activities
* Fragments
* RecyclerView Adapters
* SQLite Database
* Models

---

## 🗄 Database Structure

The app uses **SQLite database** with the following tables:

### Users

| Column   | Description        |
| -------- | ------------------ |
| id       | User ID            |
| name     | User name          |
| email    | User email         |
| password | Encrypted password |

### Food Items

| Column         | Description      |
| -------------- | ---------------- |
| id             | Food ID          |
| name           | Food name        |
| description    | Food description |
| price          | Price            |
| category       | Food category    |
| image_resource | Image resource   |

### Cart Items

| Column   | Description  |
| -------- | ------------ |
| id       | Cart item ID |
| user_id  | User ID      |
| food_id  | Food ID      |
| quantity | Quantity     |

### Orders

| Column      | Description               |
| ----------- | ------------------------- |
| id          | Order ID                  |
| user_id     | User who placed the order |
| total_price | Total order price         |
| address     | Delivery address          |
| order_date  | Order date                |

---

## 📂 Project Structure

```
FoodGo
│
├── activity
│   ├── LoginActivity
│   ├── RegisterActivity
│   ├── MainActivity
│   ├── FoodDetailActivity
│   ├── CheckoutActivity
│   └── OrderSuccessActivity
│
├── fragments
│   ├── HomeFragment
│   ├── CartFragment
│   └── ProfileFragment
│
├── adapters
│   ├── FoodAdapter
│   └── CartAdapter
│
├── database
│   └── DatabaseHelper
│
└── models
    ├── FoodItem
    ├── CartItem
    └── User
```

---

## 🛠 Technologies Used

* **Java**
* **Android Studio**
* **SQLite Database**
* **RecyclerView**
* **Material Design Components**

---

## ▶️ How to Run the Project

1. Clone the repository

```bash
git clone https://github.com/AkalankaJayawardhane/FoodGo.git
```

2. Open the project in **Android Studio**

3. Sync Gradle

4. Run the app using an **Android Emulator or Physical Device**

---

## 👨‍💻 Contributors

* **Akalanka Jayawardhane**
* Chathumini Atthanayake
* aathif99

---

## 📌 Future Improvements

* Online payment integration
* Order history screen
* Admin panel for food management
* Push notifications
* API-based backend

---

## 📜 License

This project is created for **educational purposes**.
