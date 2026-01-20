# 🛒 E-Commerce Backend API

A simple e-commerce backend system built with Spring Boot and MongoDB. This project demonstrates core e-commerce functionality including product management, shopping cart, order processing, and payment integration with webhook support.

## 📋 Table of Contents
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Usage Examples](#usage-examples)
- [Project Structure](#project-structure)
- [Troubleshooting](#troubleshooting)

## ✨ Features
- **Product Management**: Create and list products with inventory tracking
- **Shopping Cart**: Add/remove items, view cart contents, and clear cart
- **Order Management**: Create orders from cart, view order details and history
- **Payment Integration**: Mock payment service with async webhook callbacks
- **Stock Management**: Automatic stock deduction when orders are placed
- **Order Status Tracking**: Track order lifecycle (CREATED → PAID/FAILED)

## 🛠 Tech Stack
- **Framework**: Spring Boot 4.0.1
- **Language**: Java 17+
- **Database**: MongoDB (NoSQL)
- **Build Tool**: Maven
- **Dependencies**:
  - Spring Data MongoDB
  - Spring Web MVC
  - Lombok (for cleaner code)
  - Spring Boot DevTools

## 📦 Prerequisites

Before you begin, ensure you have the following installed:

- **Java Development Kit (JDK) 17** or higher
  - Check version: `java -version`
  - Download: [OpenJDK](https://jdk.java.net/17/) or [Oracle JDK](https://www.oracle.com/java/technologies/downloads/)

- **MongoDB** (Community Edition recommended)
  - Should be running on `localhost:27017` (default port)
  - Check if running: `mongosh` or `mongo`
  - Download: [MongoDB Community Server](https://www.mongodb.com/try/download/community)

- **Maven** (optional - included via Maven Wrapper)
  - Wrapper scripts: `./mvnw` (Linux/Mac) or `mvnw.cmd` (Windows)

## 🚀 Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Raam751/assignmentecom.git
   cd assignmentecom
   ```

2. **Ensure MongoDB is running**
   ```bash
   # Start MongoDB (if not already running)
   # On Linux/Mac:
   sudo systemctl start mongod
   # or
   brew services start mongodb-community
   
   # On Windows:
   net start MongoDB
   ```

3. **Configure application (optional)**
   
   Edit `src/main/resources/application.properties` if you need custom MongoDB settings:
   ```properties
   spring.data.mongodb.host=localhost
   spring.data.mongodb.port=27017
   spring.data.mongodb.database=ecom
   ```

## ▶️ Running the Application

### Option 1: Using Maven Wrapper (Recommended)
```bash
./mvnw spring-boot:run
```

### Option 2: Using installed Maven
```bash
mvn spring-boot:run
```

### Option 3: Build and run JAR
```bash
./mvnw clean package
java -jar target/ecom-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

## 📚 API Documentation

### Products

#### Create Product
```http
POST /api/products
Content-Type: application/json

{
  "name": "Laptop",
  "description": "High performance laptop",
  "price": 999.99,
  "stock": 10
}
```

#### Get All Products
```http
GET /api/products
```

#### Get Product by ID
```http
GET /api/products/{productId}
```

### Cart

#### Add Item to Cart
```http
POST /api/cart/add
Content-Type: application/json

{
  "userId": "user123",
  "productId": "prod456",
  "quantity": 2
}
```

#### Get User's Cart
```http
GET /api/cart/{userId}
```

#### Clear Cart
```http
DELETE /api/cart/{userId}/clear
```

### Orders

#### Create Order from Cart
```http
POST /api/orders
Content-Type: application/json

{
  "userId": "user123"
}
```

#### Get Order Details
```http
GET /api/orders/{orderId}
```

#### Get User's Orders
```http
GET /api/orders/user/{userId}
```

### Payments

#### Create Payment
```http
POST /api/payments/create
Content-Type: application/json

{
  "orderId": "order789",
  "amount": 1999.98
}
```

#### Get Payment by Order
```http
GET /api/payments/order/{orderId}
```

### Webhooks

#### Payment Webhook (Internal - Auto-triggered)
```http
POST /api/webhooks/payment
Content-Type: application/json

{
  "orderId": "order789",
  "status": "SUCCESS",
  "paymentId": "pay_xyz123"
}
```

## 💡 Usage Examples

### Complete Purchase Flow

1. **Create a Product**
   ```bash
   curl -X POST http://localhost:8080/api/products \
     -H "Content-Type: application/json" \
     -d '{
       "name": "Wireless Mouse",
       "description": "Ergonomic wireless mouse",
       "price": 29.99,
       "stock": 50
     }'
   ```
   Response: `{"id":"abc123", "name":"Wireless Mouse", ...}`

2. **Add Product to Cart**
   ```bash
   curl -X POST http://localhost:8080/api/cart/add \
     -H "Content-Type: application/json" \
     -d '{
       "userId": "user001",
       "productId": "abc123",
       "quantity": 2
     }'
   ```

3. **View Cart**
   ```bash
   curl http://localhost:8080/api/cart/user001
   ```

4. **Create Order**
   ```bash
   curl -X POST http://localhost:8080/api/orders \
     -H "Content-Type: application/json" \
     -d '{"userId": "user001"}'
   ```
   Response: `{"id":"ord456", "status":"CREATED", "totalAmount":59.98, ...}`

5. **Initiate Payment**
   ```bash
   curl -X POST http://localhost:8080/api/payments/create \
     -H "Content-Type: application/json" \
     -d '{
       "orderId": "ord456",
       "amount": 59.98
     }'
   ```
   
   The payment service will simulate a 3-second processing delay and automatically trigger a webhook.

6. **Check Order Status** (after ~3 seconds)
   ```bash
   curl http://localhost:8080/api/orders/ord456
   ```
   Response: `{"id":"ord456", "status":"PAID", ...}`

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/assignment/ecom/
│   │   ├── client/           # External service clients
│   │   │   └── MockPaymentClient.java
│   │   ├── config/           # Configuration classes
│   │   │   └── RestTemplateConfig.java
│   │   ├── controller/       # REST API endpoints
│   │   │   ├── CartController.java
│   │   │   ├── OrderController.java
│   │   │   ├── PaymentController.java
│   │   │   └── ProductController.java
│   │   ├── dto/              # Data Transfer Objects
│   │   │   ├── AddToCartRequest.java
│   │   │   ├── CartItemResponse.java
│   │   │   ├── CreateOrderRequest.java
│   │   │   ├── OrderResponse.java
│   │   │   ├── PaymentRequest.java
│   │   │   └── PaymentWebhookRequest.java
│   │   ├── model/            # Entity models
│   │   │   ├── CartItem.java
│   │   │   ├── Order.java
│   │   │   ├── OrderItem.java
│   │   │   ├── Payment.java
│   │   │   ├── Product.java
│   │   │   └── User.java
│   │   ├── repository/       # MongoDB repositories
│   │   │   ├── CartRepository.java
│   │   │   ├── OrderRepository.java
│   │   │   ├── PaymentRepository.java
│   │   │   └── ProductRepository.java
│   │   ├── service/          # Business logic
│   │   │   ├── CartService.java
│   │   │   ├── OrderService.java
│   │   │   ├── PaymentService.java
│   │   │   └── ProductService.java
│   │   ├── webhook/          # Webhook handlers
│   │   │   └── PaymentWebhookController.java
│   │   └── EcomApplication.java  # Main application
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/assignment/ecom/
        └── EcomApplicationTests.java
```

## 🔧 Troubleshooting

### MongoDB Connection Issues

**Problem**: Application fails to start with MongoDB connection error

**Solution**:
1. Verify MongoDB is running:
   ```bash
   mongosh  # or mongo for older versions
   ```
2. Check MongoDB is on correct port:
   ```bash
   sudo lsof -i :27017  # Linux/Mac
   netstat -ano | findstr :27017  # Windows
   ```
3. Update `application.properties` if using non-default settings

### Port Already in Use

**Problem**: `Port 8080 is already in use`

**Solution**:
1. Find and kill the process using port 8080:
   ```bash
   # Linux/Mac
   lsof -ti:8080 | xargs kill -9
   
   # Windows
   netstat -ano | findstr :8080
   taskkill /PID <PID> /F
   ```
2. Or change the port in `application.properties`:
   ```properties
   server.port=8081
   ```

### Build Fails with Java Version Error

**Problem**: `java: invalid source release: 17`

**Solution**: Ensure you're using Java 17+
```bash
java -version
# Update JAVA_HOME environment variable if needed
```

### Webhook Not Triggering

**Problem**: Payment webhook doesn't update order status

**Solution**:
1. Webhook triggers after 3 seconds - wait a bit longer
2. Check application logs for errors
3. Ensure application is running when payment is created
4. Webhook calls `http://localhost:8080` - won't work if app is on different host/port

### Cart is Empty Error

**Problem**: "Cart is empty" when creating order

**Solution**: Add items to cart first before creating an order
```bash
curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -d '{"userId": "user123", "productId": "prod456", "quantity": 1}'
```

## 📝 Notes

- This is a learning/demo project with simplified authentication (user IDs are plain strings)
- The payment service is mocked and automatically approves all transactions after 3 seconds
- Stock is tracked and automatically reduced when orders are placed
- Failed payments will mark orders as "FAILED"
- MongoDB data persists between restarts unless you drop the database

## 🤝 Contributing

Feel free to submit issues and pull requests!

## 📄 License

This project is open source and available for educational purposes.
