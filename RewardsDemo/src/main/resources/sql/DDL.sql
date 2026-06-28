-- DDL for customer table --
CREATE TABLE `customer` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `phone_number` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `address` varchar(45) DEFAULT NULL,
  `points_balance` int DEFAULT NULL,
  `registration_date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `phone_number_UNIQUE` (`phone_number`)
); 

-- DDL for purchase table --
CREATE TABLE `purchase` (
  `id` int NOT NULL AUTO_INCREMENT,
  `customer_id` int DEFAULT NULL,
  `total_price` decimal(10,0) DEFAULT NULL,
  `purchase_date` date DEFAULT NULL,
  `points` int DEFAULT NULL,
  PRIMARY KEY (`id`)
)

-- DDL for item table --
CREATE TABLE `item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `price` decimal(8,0) DEFAULT NULL,
  PRIMARY KEY (`id`)
) 
