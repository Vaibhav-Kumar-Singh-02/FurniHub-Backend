-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: e_commerce
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin_users`
--

DROP TABLE IF EXISTS `admin_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin_users` (
  `admin_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL DEFAULT '',
  `full_name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `mobile` varchar(20) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('ADMIN','SUPER_ADMIN') DEFAULT 'ADMIN',
  `is_active` tinyint(1) DEFAULT '1',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`admin_id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `mobile` (`mobile`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_users`
--

LOCK TABLES `admin_users` WRITE;
/*!40000 ALTER TABLE `admin_users` DISABLE KEYS */;
INSERT INTO `admin_users` VALUES (1,'admin','Admin User','admin@furnihub.com','9999999999','$2a$10$1qkdcsErdmX/4KVui1Fgv.jNGbkNFOOFJWlgiBz3JqzpVlLP6uNSq','ADMIN',1,'2026-08-08 05:08:08','2026-08-08 05:22:05');
/*!40000 ALTER TABLE `admin_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `app_settings`
--

DROP TABLE IF EXISTS `app_settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `app_settings` (
  `id` int NOT NULL AUTO_INCREMENT,
  `currency` varchar(10) DEFAULT NULL,
  `site_description` varchar(255) DEFAULT NULL,
  `site_name` varchar(100) DEFAULT NULL,
  `support_email` varchar(100) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app_settings`
--

LOCK TABLES `app_settings` WRITE;
/*!40000 ALTER TABLE `app_settings` DISABLE KEYS */;
INSERT INTO `app_settings` VALUES (1,'USD',NULL,'FurniHub Pro',NULL,'2026-08-11 09:03:37.762231');
/*!40000 ALTER TABLE `app_settings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart_items`
--

DROP TABLE IF EXISTS `cart_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_items` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  `quantity` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id_idx` (`user_id`),
  KEY `product_id_idx` (`product_id`),
  CONSTRAINT `producst_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`),
  CONSTRAINT `users_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_items`
--

LOCK TABLES `cart_items` WRITE;
/*!40000 ALTER TABLE `cart_items` DISABLE KEYS */;
/*!40000 ALTER TABLE `cart_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `categorie_id` int NOT NULL AUTO_INCREMENT,
  `category_name` varchar(255) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`categorie_id`),
  UNIQUE KEY `category_name_UNIQUE` (`category_name`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'Bed',NULL,NULL),(2,'Bookshelf',NULL,NULL),(3,'Center Table',NULL,NULL),(4,'Dressing Table',NULL,NULL),(5,'Dining Table',NULL,NULL),(6,'Decor',NULL,NULL),(7,'Lockers',NULL,NULL),(8,'Sofa',NULL,NULL),(9,'Office-Chair',NULL,NULL),(10,'Office-Table',NULL,NULL),(11,'Side-Table',NULL,NULL),(12,'Shoe-Rack',NULL,NULL),(13,'TV-unit',NULL,NULL),(14,'Wardrobe',NULL,NULL),(15,'Test Category','2026-08-09 06:25:11.079343',NULL),(17,'Living Room Furniture','2026-08-09 06:27:03.951212','Furniture for living room');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `coupons`
--

DROP TABLE IF EXISTS `coupons`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `coupons` (
  `coupon_id` int NOT NULL,
  `code` varchar(50) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `discount_type` enum('PERCENTAGE','FIXED') NOT NULL,
  `discount_value` decimal(38,2) NOT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `max_discount_amount` decimal(38,2) DEFAULT NULL,
  `min_purchase_amount` decimal(38,2) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `usage_limit` int DEFAULT NULL,
  `used_count` int DEFAULT NULL,
  `valid_from` datetime(6) DEFAULT NULL,
  `valid_until` datetime(6) DEFAULT NULL,
  `applies_to` varchar(20) DEFAULT 'ALL',
  `product_ids` text,
  PRIMARY KEY (`coupon_id`),
  UNIQUE KEY `UK_eplt0kkm9yf2of2lnx6c1oy9b` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coupons`
--

LOCK TABLES `coupons` WRITE;
/*!40000 ALTER TABLE `coupons` DISABLE KEYS */;
INSERT INTO `coupons` VALUES (1,'TEST25','2026-08-08 16:28:14.138601','PERCENTAGE',25.00,_binary '',NULL,NULL,'2026-08-08 16:28:14.138601',100,0,'2026-08-07 18:30:00.000000','2026-12-31 18:29:59.000000','ALL',NULL),(3,'FURNITURE10','2026-08-08 16:39:16.094565','PERCENTAGE',10.00,_binary '',NULL,NULL,'2026-08-08 16:39:16.094565',50,0,'2026-08-07 18:30:00.000000','2026-12-31 18:29:59.000000','ALL',''),(5,'DISCOUNT','2026-08-08 17:00:33.594572','PERCENTAGE',10.00,_binary '',NULL,NULL,'2026-08-08 17:00:33.594572',0,0,'2026-08-08 11:31:00.000000','2026-08-08 11:32:00.000000','ALL',''),(52,'DIS','2026-08-09 06:17:58.608352','PERCENTAGE',5.00,_binary '',NULL,NULL,'2026-08-09 06:17:58.608352',0,0,'2026-08-09 00:47:00.000000','2026-08-10 00:47:00.000000','ALL',''),(102,'TESTSPECIFIC','2026-08-10 05:39:39.095075','PERCENTAGE',15.00,_binary '',NULL,NULL,'2026-08-10 05:39:39.095075',50,0,'2026-08-07 18:30:00.000000','2026-12-31 18:29:59.000000','SPECIFIC','1');
/*!40000 ALTER TABLE `coupons` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `coupons_seq`
--

DROP TABLE IF EXISTS `coupons_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `coupons_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coupons_seq`
--

LOCK TABLES `coupons_seq` WRITE;
/*!40000 ALTER TABLE `coupons_seq` DISABLE KEYS */;
INSERT INTO `coupons_seq` VALUES (201);
/*!40000 ALTER TABLE `coupons_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jwt_tokens`
--

DROP TABLE IF EXISTS `jwt_tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `jwt_tokens` (
  `token_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `token` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `expires_at` timestamp NOT NULL,
  PRIMARY KEY (`token_id`),
  KEY `user_id_idx` (`user_id`),
  CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=89 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jwt_tokens`
--

LOCK TABLES `jwt_tokens` WRITE;
/*!40000 ALTER TABLE `jwt_tokens` DISABLE KEYS */;
INSERT INTO `jwt_tokens` VALUES (1,1,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIiwiaWF0IjoxNzg1MjU3ODc2LCJleHAiOjE3ODUzNDQyNzZ9.p-BSKNedykZA6MYI3-kiXZ7OnQHpoJjcyEfvu10RvzgUErY3Tcz6R9lk3kaJLgzo','2026-07-28 11:27:56','2026-07-29 11:27:56'),(2,1,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIiwiaWF0IjoxNzg1MjU3ODgxLCJleHAiOjE3ODUzNDQyODF9.VNw8OmH48MNNne5iLWlNe7GiL7IBxtJOA0BQLfykIdisYdKReLLfMy5YQpALNGDL','2026-07-28 11:28:01','2026-07-29 11:28:01'),(3,1,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIiwiaWF0IjoxNzg1MjU3ODgxLCJleHAiOjE3ODUzNDQyODF9.VNw8OmH48MNNne5iLWlNe7GiL7IBxtJOA0BQLfykIdisYdKReLLfMy5YQpALNGDL','2026-07-28 11:28:01','2026-07-29 11:28:01'),(4,1,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIiwiaWF0IjoxNzg1MjU4MDY3LCJleHAiOjE3ODUzNDQ0Njd9.LtTb_Nwm2Q4j3WYrcqaDsUPH7thHx_VzcHA_AJUoLp-1EUsBLHSAh8q7s6JaLai-','2026-07-28 11:31:07','2026-07-29 11:31:07'),(5,2,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJndWRkdUBnbWFpbC5jb20iLCJpYXQiOjE3ODUzMDQxNzYsImV4cCI6MTc4Nzg5NjE3Nn0.V3-5flVUJXFRMMGFNRt_cJ6ic7l7tQoDKijXdkgvkZ0ZO5tUOrMJqOtCibgnIHS3','2026-07-29 00:19:37','2026-08-28 00:19:37'),(6,2,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJndWRkdUBnbWFpbC5jb20iLCJpYXQiOjE3ODUzMDQyMDAsImV4cCI6MTc4Nzg5NjIwMH0.Iah5-QLw9AnG8AXQSsQSRf6Prd8IkTEkt4FRCFCuE7BHH0wfSurbXVQhZ871FSX4','2026-07-29 00:20:00','2026-08-28 00:20:00'),(7,1,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIiwiaWF0IjoxNzg1NDA3NDI3LCJleHAiOjE3ODc5OTk0Mjd9.QrhfGD8zbmad3esDq3NEZGCPcFUJ3qXzxIoflkUZCiqCjKH_qqLoDYvwV0CmymGt','2026-07-30 05:00:27','2026-08-29 05:00:27'),(8,1,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIiwiaWF0IjoxNzg1NDA3ODY2LCJleHAiOjE3ODc5OTk4NjZ9.mGm5Sl7tHktEV4gfohwVruuynJe3eBjJwraQeVrn3_mgoLCEGNXR_9vPPpDDFQNr','2026-07-30 05:07:47','2026-08-29 05:07:47'),(9,3,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0MXByaTJ0QGV4YW1wbGUuY29tIiwiaWF0IjoxNzg1NzU1MDQxLCJleHAiOjE3ODgzNDcwNDF9.YAKu5wqaGQ-lArKpZec73Lnyh7yFchv8QnQTI40cCKNSjq_dD1ueNVc7uGri-8Ef','2026-08-03 05:34:02','2026-09-02 05:34:02'),(10,4,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJyb2JpbjEyM0BnbWFpbC5jb20iLCJpYXQiOjE3ODU3NTUxMjgsImV4cCI6MTc4ODM0NzEyOH0.VzcdVPREGvKmUEY1Anay9B6PLhBfx8cspQfIkwt59y4Jo_HBkp39hmnm5u8_y1-s','2026-08-03 05:35:29','2026-09-02 05:35:29'),(87,10,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJuZXd1c2VyQHRlc3QuY29tIiwiaWF0IjoxNzg2NDI4MjYwLCJleHAiOjE3ODkwMjAyNjB9.iwXnDEj9Q4DNnsdG_ujea9wuyVdsIw7wa25FQ3SHmo1gWigaecoKYZD3HzugfbB0','2026-08-11 00:34:20','2026-09-10 00:34:20'),(88,8,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbkBmdXJuaWh1Yi5jb20iLCJpYXQiOjE3ODY0Mzg4ODgsImV4cCI6MTc4OTAzMDg4OH0.vd8paEH-4hkEH9YqMZ_YvxcxpjVLDFg8Z_AzqDFfsvUeQ7OoZpYyFLelkzhz9bME','2026-08-11 03:31:29','2026-09-10 03:31:29');
/*!40000 ALTER TABLE `jwt_tokens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_items` (
  `order_item_id` int NOT NULL AUTO_INCREMENT,
  `price_per_unit` decimal(38,2) NOT NULL,
  `quantity` int NOT NULL,
  `total_price` decimal(38,2) NOT NULL,
  `order_id` varchar(255) NOT NULL,
  `product_id` int NOT NULL,
  PRIMARY KEY (`order_item_id`),
  KEY `FKbioxgbv59vetrxe0ejfubep1w` (`order_id`),
  KEY `FKocimc7dtr037rh4ls4l95nlfi` (`product_id`),
  CONSTRAINT `FKbioxgbv59vetrxe0ejfubep1w` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
  CONSTRAINT `FKocimc7dtr037rh4ls4l95nlfi` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_items`
--

LOCK TABLES `order_items` WRITE;
/*!40000 ALTER TABLE `order_items` DISABLE KEYS */;
INSERT INTO `order_items` VALUES (1,15000.00,1,15000.00,'2a9a77e141054312',1),(2,19999.00,1,19999.00,'30c711099e3e43fc',27),(3,24999.00,1,24999.00,'3b156f969c9e44e2',26);
/*!40000 ALTER TABLE `order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `order_id` varchar(255) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `payment_method` varchar(255) DEFAULT NULL,
  `shipping_address` text,
  `status` enum('PENDING','CONFIRMED','SHIPPED','DELIVERED','CANCELLED','RETURNED','REFUNDED') NOT NULL,
  `total_amount` decimal(38,2) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`order_id`),
  KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`),
  CONSTRAINT `FK32ql8ubntj5uh44ph9659tiih` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES ('2a9a77e141054312','2026-08-05 07:41:13.755511','Cash on Delivery','123 Test Street, Test City','DELIVERED',15000.00,'2026-08-09 06:18:29.078097',8),('30c711099e3e43fc','2026-08-05 09:37:26.242757','Cash on Delivery','Robin, HSR, Bhagalpur, Bihar, 802001','DELIVERED',19999.00,'2026-08-08 07:46:25.348711',6),('3b156f969c9e44e2','2026-08-08 07:03:50.795639','Razorpay','Robin, HSR, Bangalore, Karnataka, 560089','CONFIRMED',24999.00,'2026-08-08 07:03:50.931617',6);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `otp_verification`
--

DROP TABLE IF EXISTS `otp_verification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `otp_verification` (
  `otp_id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `expires_at` datetime(6) NOT NULL,
  `otp` varchar(6) NOT NULL,
  `verified` bit(1) NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`otp_id`),
  KEY `FKmtitrif16hpdkhtr4m4kgvfv8` (`user_id`),
  CONSTRAINT `FKmtitrif16hpdkhtr4m4kgvfv8` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `otp_verification`
--

LOCK TABLES `otp_verification` WRITE;
/*!40000 ALTER TABLE `otp_verification` DISABLE KEYS */;
INSERT INTO `otp_verification` VALUES (4,'2026-08-08 09:23:38.062914','2026-08-08 09:33:38.062914','278479',_binary '',6);
/*!40000 ALTER TABLE `otp_verification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productimages`
--

DROP TABLE IF EXISTS `productimages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `productimages` (
  `image_id` int NOT NULL AUTO_INCREMENT,
  `product_id` int DEFAULT NULL,
  `image_url` text NOT NULL,
  PRIMARY KEY (`image_id`),
  KEY `product_id_idx` (`product_id`),
  CONSTRAINT `product_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productimages`
--

LOCK TABLES `productimages` WRITE;
/*!40000 ALTER TABLE `productimages` DISABLE KEYS */;
INSERT INTO `productimages` VALUES (1,1,'https://ik.imagekit.io/StringStackVaibhav/Furniture/811UqEn-1pL._SX679_.jpg'),(2,2,'https://ik.imagekit.io/StringStackVaibhav/Furniture/61jwXNhUN-L._SX679_.jpg'),(3,3,'https://ik.imagekit.io/StringStackVaibhav/Furniture/61V5fZnh0nL._SX679_.jpg'),(4,4,'https://ik.imagekit.io/StringStackVaibhav/Furniture/71-x9b63ruL._SX679_.jpg'),(5,5,'https://ik.imagekit.io/StringStackVaibhav/Furniture/71HIP2I1zpL._SX679_.jpg'),(6,6,'https://ik.imagekit.io/StringStackVaibhav/Furniture/61fnKh2fOSL._SX679_.jpg'),(7,7,'https://ik.imagekit.io/StringStackVaibhav/Furniture/41wGBMukE+L._SY300_SX300_QL70_FMwebp_.webp'),(8,8,'https://ik.imagekit.io/StringStackVaibhav/Furniture/41+WuJAf8lL._SY300_SX300_QL70_FMwebp_.webp'),(9,9,'https://ik.imagekit.io/StringStackVaibhav/Furniture/41veRSnDw0L._SY300_SX300_QL70_FMwebp_.webp'),(10,10,'https://ik.imagekit.io/StringStackVaibhav/Furniture/41hIMKqYX-L._SY300_SX300_QL70_FMwebp_.webp'),(11,11,'https://ik.imagekit.io/StringStackVaibhav/Furniture/41L0eKYe4qL._SY300_SX300_QL70_FMwebp_.webp'),(12,12,'https://ik.imagekit.io/StringStackVaibhav/Furniture/510eZ-ehM3L._SY300_SX300_QL70_FMwebp_.webp'),(13,13,'https://ik.imagekit.io/StringStackVaibhav/Furniture/618BPhmREsL._AC_UL480_FMwebp_QL65_.webp'),(14,14,'https://ik.imagekit.io/StringStackVaibhav/Furniture/61qEhDtYkRL._AC_UL480_FMwebp_QL65_.webp'),(15,15,'https://ik.imagekit.io/StringStackVaibhav/Furniture/61eBbyqXQyL._AC_UL480_FMwebp_QL65_.webp'),(16,16,'https://ik.imagekit.io/StringStackVaibhav/Furniture/61hqJefnC9L._AC_UL480_FMwebp_QL65_.webp?updatedAt=1785170594524'),(17,17,'https://ik.imagekit.io/StringStackVaibhav/Furniture/81ABwKIZpSL._AC_UL480_FMwebp_QL65_.webp?updatedAt=1785170594420'),(18,18,'https://ik.imagekit.io/StringStackVaibhav/Furniture/51GIrgTO+wL._AC_UL480_FMwebp_QL65_.webp?updatedAt=1785170594397'),(19,19,'https://ik.imagekit.io/StringStackVaibhav/Furniture/61f2bofII2L._AC_UL480_FMwebp_QL65_.webp?updatedAt=1785170594313'),(20,20,'https://ik.imagekit.io/StringStackVaibhav/Furniture/61Z9yq4WD2L._AC_UL480_FMwebp_QL65_.webp?updatedAt=1785170594326'),(21,21,'https://ik.imagekit.io/StringStackVaibhav/Furniture/71e7TWbxiAL._SX679_.jpg?updatedAt=1785170594396'),(22,22,'https://ik.imagekit.io/StringStackVaibhav/Furniture/811q70S9H0L._SX679_.jpg?updatedAt=1785170594441'),(23,23,'https://ik.imagekit.io/StringStackVaibhav/Furniture/817f67FtrdL._SX679_.jpg?updatedAt=1785170594433'),(24,24,'https://ik.imagekit.io/StringStackVaibhav/Furniture/51hZRY5rnwL._SX679_.jpg?updatedAt=1785170594311'),(25,25,'https://ik.imagekit.io/StringStackVaibhav/Furniture/612G2U5+gjL._SX679_.jpg?updatedAt=1785170594235'),(26,26,'https://ik.imagekit.io/StringstackHari/Project_funiture/bed%205.jpg'),(27,27,'https://ik.imagekit.io/StringstackHari/Project_funiture/bed%204.jpg'),(28,28,'https://ik.imagekit.io/StringstackHari/Project_funiture/bed%203.jpg'),(29,29,'https://ik.imagekit.io/StringstackHari/Project_funiture/bed%202.jpg'),(30,30,'https://ik.imagekit.io/StringstackHari/Project_funiture/bed%201.jpg'),(31,31,'https://ik.imagekit.io/StringstackHari/Project_funiture/d_table%201.jpg'),(32,32,'https://ik.imagekit.io/StringstackHari/Project_funiture/d_table%202.jpg'),(33,33,'https://ik.imagekit.io/StringstackHari/Project_funiture/d_table%203.jpg'),(34,34,'https://ik.imagekit.io/StringstackHari/Project_funiture/d_table%204.jpg'),(35,35,'https://ik.imagekit.io/StringstackHari/Project_funiture/d_table%205.jpg'),(36,36,'https://ik.imagekit.io/StringstackHari/Project_funiture/d_table%206.jpg'),(37,37,'https://ik.imagekit.io/StringstackHari/Project_funiture/d_table%207.jpg'),(38,38,'https://ik.imagekit.io/StringstackHari/Project_funiture/s_table%205.jpg'),(39,39,'https://ik.imagekit.io/StringstackHari/Project_funiture/s_table%204.jpg'),(40,40,'https://ik.imagekit.io/StringstackHari/Project_funiture/s_table%203.jpg'),(41,41,'https://ik.imagekit.io/StringstackHari/Project_funiture/s_table%202.jpg'),(42,42,'https://ik.imagekit.io/StringstackHari/Project_funiture/s_table%201.jpg'),(43,43,'https://ik.imagekit.io/StringstackHari/Project_funiture/s_table%206.jpg'),(44,44,'https://ik.imagekit.io/StringstackHari/Project_funiture/n_table%205.jpg'),(45,45,'https://ik.imagekit.io/StringstackMonika/product%20images/71AC-yv3hbL._SL1500_.jpg'),(46,46,'https://ik.imagekit.io/StringstackHari/Project_funiture/n_table%203.jpg'),(47,47,'https://ik.imagekit.io/StringstackHari/Project_funiture/n_table%202.jpg'),(48,48,'https://ik.imagekit.io/StringstackHari/Project_funiture/n_table%201.jpg'),(49,49,'https://ik.imagekit.io/StringstackMonika/product%20images/81inGzxlahL._SL1500_.jpg'),(50,50,'https://ik.imagekit.io/StringstackMonika/product%20images/71AC-yv3hbL._SL1500_.jpg'),(51,51,'https://ik.imagekit.io/StringstackMonika/product%20images/81pZNN2BPEL._SL1500_.jpg'),(52,52,'https://ik.imagekit.io/StringstackMonika/product%20images/51ODqHsXbRL.jpg'),(53,53,'https://ik.imagekit.io/StringstackMonika/product%20images/51-R20UxJVL._SL1080_.jpg'),(54,54,'https://ik.imagekit.io/StringstackMonika/product%20images/81198G+RN7L._SL1500_.jpg'),(55,55,'https://ik.imagekit.io/StringstackMonika/product%20images/71X3pqWwTGL._SL1500_.jpg'),(56,56,'https://ik.imagekit.io/StringstackMonika/product%20images/71gfBX64sTL._SL1500_.jpg'),(57,57,'https://ik.imagekit.io/StringstackMonika/product%20images/61m2kUNcRqL._SL1024_.jpg'),(58,58,'https://ik.imagekit.io/StringstackMonika/product%20images/71ksE5TephL._SL1500_.jpg'),(59,59,'https://ik.imagekit.io/StringstackMonika/product%20images/81NZrT5mYoL._SL1500_.jpg'),(60,60,'https://ik.imagekit.io/StringstackMonika/product%20images/71XvNqxja8L._SL1500_.jpg'),(61,61,'https://ik.imagekit.io/StringstackMonika/product%20images/51-R20UxJVL._SL1080_.jpg'),(62,62,'https://ik.imagekit.io/StringstackMonika/product%20images/51ZlBUCqJ0L.jpg'),(63,63,'https://ik.imagekit.io/StringstackMonika/product%20images/71aUtnbJSoL._SL1155_.jpg'),(64,64,'https://ik.imagekit.io/StringstackMonika/product%20images/813Lwt53fKL._SL1500_.jpg'),(65,65,'https://ik.imagekit.io/StringstackMonika/product%20images/71SB0AEmxhL._SL1222_.jpg'),(66,66,'https://ik.imagekit.io/StringstackMonika/product%20images/71RGUKbF3gL._SL1500_.jpg'),(67,67,'https://ik.imagekit.io/StringstackMonika/product%20images/61D8JTujXAL._SL1024_.jpg'),(68,68,'https://ik.imagekit.io/StringstackMonika/product%20images/81pZNN2BPEL._SL1500_.jpg'),(69,69,'https://ik.imagekit.io/StringstackMonika/product%20images/71gfBX64sTL._SL1500_.jpg');
/*!40000 ALTER TABLE `productimages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `product_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` text,
  `price` decimal(38,2) NOT NULL,
  `stock` int NOT NULL,
  `categorie_id` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `benefits` text,
  `brand` varchar(255) NOT NULL,
  `discount` int NOT NULL,
  `furniture_type` varchar(100) DEFAULT NULL,
  `how_to_use` text,
  `ingredients` text,
  `product_size` varchar(100) DEFAULT NULL,
  `ratings` double NOT NULL,
  `status` enum('ACTIVE','INACTIVE') NOT NULL,
  `subcategory` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`product_id`),
  KEY `categorie_id_idx` (`categorie_id`),
  CONSTRAINT `categorie_id` FOREIGN KEY (`categorie_id`) REFERENCES `categories` (`categorie_id`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'Solid Wood Center Table','Durable, spacious, timeless, sturdy, elegant.',15000.00,19,3,NULL,'2026-08-05 02:11:14',NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(2,'oval wooden center table','Elegant oval table with storage.',23000.00,18,3,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(3,'Modern Nested Center Table','Stylish, space-saving, durable, elegant, functional.',18000.00,19,3,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(4,'Mid-Century Wooden Coffee Table','Compact, stylish, practical, durable, modern.',35000.00,25,3,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(5,'Modern Geometric Wooden Center Table','Elegant, minimalist, sturdy, contemporary, spacious.',28000.00,14,3,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(6,'Modern Wooden TV Unit','Spacious, stylish, durable, functional, elegant.',16000.00,12,13,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(7,'Wall-Mounted Floating TV Unit','Modern, compact, floating, stylish, functional.',9000.00,17,13,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(8,'Modern Wall-Mounted TV Unit Black','Elegant, spacious, illuminated, functional, contemporary.',10000.00,20,13,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(9,'Contemporary Wooden TV Unit','Compact, stylish, spacious, durable, functional.',12000.00,11,13,NULL,'2026-08-09 06:50:23',NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(10,'Open Shelf Wooden TV Unit','Spacious, minimalist, durable, organized, modern.',8500.00,18,13,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(11,'Ergonomic Mesh Office Chair','Comfortable, breathable, adjustable, supportive, durable.',6500.00,22,9,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(12,'Executive Leather Office Chair','Luxurious, ergonomic, cushioned, adjustable, premium.',7000.00,28,9,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(13,'Modern Accent Lounge Chair','Comfortable, stylish, cushioned, elegant, durable.',9000.00,18,9,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(14,'Ergonomic High-Back Mesh Office Chair','Breathable, ergonomic, adjustable, supportive, comfortable.',7500.00,20,9,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(15,'Executive Leather Office Chair','Comfortable, elegant, adjustable, cushioned, durable.',8000.00,16,9,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(16,'Modern White Study Desk','Clean spacious sturdy minimalist workspace furniture',12000.00,34,10,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(17,'Modern Industrial Computer Desk','Stylish spacious durable organized computer workstation',11999.00,12,10,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(18,'Executive Wooden Study Desk','Elegant compact functional wooden study workstation',10999.00,15,10,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(19,'Storage Study Computer Desk','Organized durable spacious multifunctional study workstation',9000.00,19,10,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(20,'Classic Wooden Writing Desk','Compact elegant practical home office furniture',8500.00,22,10,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(21,'Modern Comfort Sofa','Plush seating with elegant design',27000.00,21,8,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(22,'Urban Luxe Sofa','Stylish comfort for everyday living',34000.00,18,8,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(23,'Harmony Living Sofa','Soft cushions with timeless elegance',38000.00,16,8,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(24,'Cozy Haven Sofa','Inviting comfort for family gatherings',44000.00,13,8,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(25,'Elite Lounge Sofa','Premium craftsmanship meets lasting comfort',39000.00,20,8,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(26,'Luxury King Bed','Premium wooden king size bed with modern finish.',24999.00,11,1,NULL,'2026-08-09 06:50:31',NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(27,'Classic Queen Bed','Elegant queen size wooden bed for modern bedrooms.',19999.00,11,1,NULL,'2026-08-05 04:07:26',NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(28,'Storage Bed','Wooden storage bed with spacious storage.',22999.00,11,1,NULL,'2026-08-09 06:50:42',NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(29,'Modern Bed','Minimalist wooden bed with premium quality.',18999.00,15,1,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(30,'Designer Bed','Contemporary designer bed with durable frame.',26999.00,6,1,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(31,'Classic Dressing Table','Wooden dressing table with mirror and drawers.',8999.00,10,4,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(32,'Premium Dressing Table','Luxury dressing table with elegant storage.',10999.00,8,4,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(33,'Modern Dressing Table','Compact dressing table with stylish mirror.',9999.00,12,4,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(34,'Royal Dressing Table','Premium wooden dressing table.',12999.00,5,4,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(35,'Designer Dressing Table','Designer dressing table with spacious storage.',13999.00,7,4,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(36,'Compact Dressing Table','Space-saving dressing table for modern homes.',8499.00,9,4,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(37,'Elegant Dressing Table','Elegant dressing table with premium finish.',11999.00,6,4,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(38,'Wooden Side Table','Compact bedside table with storage.',2499.00,20,11,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(39,'Classic Side Table','Stylish wooden side table.',2999.00,18,11,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(40,'Modern Side Table','Minimal side table for bedroom.',3499.00,15,11,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(41,'Designer Side Table','Luxury side table with elegant finish.',3999.00,12,11,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(42,'Minimal Side Table','Modern compact bedside table.',2799.00,14,11,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(43,'Premium Side Table','Premium wooden side table with drawer.',4499.00,10,11,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(44,'6-Seater Dining Table','Solid wood dining table for six people.',18999.00,8,5,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(45,'Modern Dining Table','Contemporary dining table with premium finish.',20999.00,6,5,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(46,'Family Dining Table','Spacious dining table for family meals.',17999.00,10,5,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(47,'Luxury Dining Table','Elegant dining table with modern design.',24999.00,5,5,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(48,'Wooden Dining Table','Classic wooden dining table for everyday use.',16999.00,12,5,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(49,'Bookshelf','5-tier wooden bookshelf for books and decor.',6499.00,15,2,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(50,'Wall Decor','Decorative wall design for living room.',1299.00,30,6,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(51,'Steel Locker','Single door steel locker with key lock.',5999.00,12,7,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(52,'Bookshelf Cabinet','Wooden bookshelf cabinet with glass doors.',11999.00,8,2,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(53,'Digital Locker','Electronic locker for valuables.',8999.00,10,7,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(54,'Shoe Rack Cabinet','Modern wooden shoe rack.',7499.00,14,12,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(55,'Tree Bookshelf','Creative tree-shaped wooden bookshelf.',5499.00,18,2,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(56,'Plastic Shoe Rack','Multi-layer plastic shoe rack.',2999.00,25,12,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(57,'Wooden Shoe Rack','Premium wooden shoe rack.',8999.00,10,12,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(58,'Mini Locker','Compact digital locker.',4999.00,20,7,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(59,'Kids Bookshelf','Bookshelf for kids room.',3999.00,15,2,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(60,'Wardrobe Cabinet','Storage wardrobe with transparent doors.',15999.00,8,14,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(61,'Home Locker','Digital home security locker.',7999.00,15,7,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(62,'Dressing Wardrobe','Wardrobe with mirror and drawers.',18999.00,9,14,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(63,'Shoe Storage Cabinet','Large wooden shoe storage rack.',9999.00,10,12,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(64,'Decor Corner Shelf','360-degree decorative corner shelf.',4499.00,16,6,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(65,'Sliding Wardrobe','Sliding door wooden wardrobe.',24999.00,6,14,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(66,'Modern Wardrobe','Modern modular wardrobe.',28999.00,5,14,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(67,'3 Door Wardrobe','Premium three-door wardrobe.',21999.00,7,14,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(68,'Fireproof Locker','Fireproof digital security locker.',10999.00,8,7,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL),(69,'Fabric Wardrobe','Portable fabric wardrobe.',5999.00,12,14,NULL,NULL,NULL,'',0,NULL,NULL,NULL,NULL,0,'ACTIVE',NULL);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reviews`
--

DROP TABLE IF EXISTS `reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reviews` (
  `review_id` int NOT NULL,
  `admin_reply` text,
  `comment` text,
  `created_at` datetime(6) DEFAULT NULL,
  `rating` int NOT NULL,
  `status` enum('PENDING','APPROVED','REJECTED') NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `product_id` int NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`review_id`),
  KEY `FKpl51cejpw4gy5swfar8br9ngi` (`product_id`),
  KEY `FKcgy7qjc1r99dp117y9en6lxye` (`user_id`),
  CONSTRAINT `FKcgy7qjc1r99dp117y9en6lxye` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FKpl51cejpw4gy5swfar8br9ngi` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reviews`
--

LOCK TABLES `reviews` WRITE;
/*!40000 ALTER TABLE `reviews` DISABLE KEYS */;
INSERT INTO `reviews` VALUES (11,NULL,'GOOD','2026-08-09 14:33:18.196783',5,'APPROVED','2026-08-09 14:33:35.992919',1,8);
/*!40000 ALTER TABLE `reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reviews_seq`
--

DROP TABLE IF EXISTS `reviews_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reviews_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reviews_seq`
--

LOCK TABLES `reviews_seq` WRITE;
/*!40000 ALTER TABLE `reviews_seq` DISABLE KEYS */;
INSERT INTO `reviews_seq` VALUES (151);
/*!40000 ALTER TABLE `reviews_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('ADMIN','CUSTOMER') NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `full_name` varchar(255) NOT NULL,
  `mobile` varchar(255) NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  UNIQUE KEY `UK_63cf888pmqtt5tipcne79xsbm` (`mobile`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Test User','test@example.com','$2a$10$iPbDOQ6zp5T0jdXj6AVtRec5bvNBfOjcsN.UUwL5prkoy7f5.23eK','CUSTOMER','2026-07-28 11:27:56','2026-07-28 11:31:07','Test User','9876543210',1),(2,'Guddu','guddu@gmail.com','$2a$10$hsCr1tBKvUreDPr4z/MlY.LFkklWR0P5AljntRvc1fNNSgFTDIsDO','CUSTOMER','2026-07-29 00:19:37','2026-07-29 00:19:37','Guddu','9876543212',1),(3,'Test User d0dea','test1pri2t@example.com','$2a$10$9OrTKqIcwHNvVb6y9SfUUuN4xmDHE1Kwki/gx6H90ukNO0KQ1Cw22','CUSTOMER','2026-08-03 05:34:01','2026-08-03 05:34:01','Test User d0dea','6557095618',1),(4,'Robin','robin123@gmail.com','$2a$10$dg1OmAQ9cDmflNebVa3XruebNTXeU/JxwqlKE6JxCLNVWYML1oFn.','CUSTOMER','2026-08-03 05:35:29','2026-08-03 05:35:29','Robin','8976543219',1),(6,'robin@gmail.com','robin@gmail.com','$2a$10$4DmQuXvzIc4M0dHAFsJVZemiip6/UA0J6x1JkinzXM.Pb6EoPXCri','CUSTOMER','2026-08-03 06:28:10','2026-08-03 06:28:10','Robin','9987766553',1),(7,'chikku','chikku12@gmail.com','$2a$10$rk7E0m2EjFyqCUKvCosInObCA0Y4.REIeSS3ShY4emDUNLFOMCZiK','CUSTOMER','2026-08-04 03:49:07','2026-08-04 03:49:07','chikku','8065798431',1),(8,'admin','admin@furnihub.com','$2a$10$1qkdcsErdmX/4KVui1Fgv.jNGbkNFOOFJWlgiBz3JqzpVlLP6uNSq','ADMIN',NULL,NULL,'Admin User','9999999999',1),(10,'newuser@test.com','newuser@test.com','$2a$10$Dwi.K9iqbfsyjx6B1qBqxue6NdnACRtJdmvppGj.kWdwxW6HCADle','CUSTOMER','2026-08-11 00:34:20','2026-08-11 00:34:20','New User','8888888888',1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wishlist`
--

DROP TABLE IF EXISTS `wishlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wishlist` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `product_id` int NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_wishlist` (`user_id`,`product_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `wishlist_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `wishlist_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wishlist`
--

LOCK TABLES `wishlist` WRITE;
/*!40000 ALTER TABLE `wishlist` DISABLE KEYS */;
INSERT INTO `wishlist` VALUES (7,10,3,'2026-08-11 01:01:48');
/*!40000 ALTER TABLE `wishlist` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-08-12 16:22:37
