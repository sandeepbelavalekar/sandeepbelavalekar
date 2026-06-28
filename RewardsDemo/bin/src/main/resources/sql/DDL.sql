CREATE TABLE `customer` (
  `id` int NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `phoneNumber` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `address` varchar(45) DEFAULT NULL,
  `points` int DEFAULT NULL,
  PRIMARY KEY (`id`)
);
