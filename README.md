# E-Commerce Backend API

Minimal e-commerce backend system using Spring Boot and MongoDB.

## Features
- Product management (create, list)
- Cart management (add items, view, clear)
- Order management (create from cart, view)
- Payment integration with mock service + webhook

## Prerequisites
- Java 21
- MongoDB running on localhost:27017

## Run
```bash
./mvnw spring-boot:run
```

## API Endpoints

### Products
- `POST /api/products` - Create product
- `GET /api/products` - List all products

### Cart
- `POST /api/cart/add` - Add item to cart
- `GET /api/cart/{userId}` - Get user's cart
- `DELETE /api/cart/{userId}/clear` - Clear cart

### Orders
- `POST /api/orders` - Create order from cart
- `GET /api/orders/{orderId}` - Get order details
- `GET /api/orders/user/{userId}` - Get user's orders

### Payments
- `POST /api/payments/create` - Create payment
- `GET /api/payments/order/{orderId}` - Get payment by order

### Webhooks
- `POST /api/webhooks/payment` - Payment webhook callback

## Flow
1. Create products
2. Add items to cart
3. Create order from cart
4. Initiate payment
5. Wait for webhook (auto-triggers after 3s)
6. Check order status (should be PAID)
