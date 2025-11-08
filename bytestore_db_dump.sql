-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: bytestore_db
-- ------------------------------------------------------
-- Server version	8.0.35

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `tb_order_items`
--

DROP TABLE IF EXISTS `tb_order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_order_items` (
  `id` binary(16) NOT NULL,
  `order_id` binary(16) NOT NULL,
  `product_id` binary(16) NOT NULL,
  `quantity` int NOT NULL,
  `unit_price` decimal(10,2) NOT NULL,
  `subtotal` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_order_items_order_id` (`order_id`),
  KEY `fk_order_items_product_id` (`product_id`),
  CONSTRAINT `fk_order_items_order_id` FOREIGN KEY (`order_id`) REFERENCES `tb_orders` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_order_items_product_id` FOREIGN KEY (`product_id`) REFERENCES `tb_products` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_order_items`
--

LOCK TABLES `tb_order_items` WRITE;
/*!40000 ALTER TABLE `tb_order_items` DISABLE KEYS */;
INSERT INTO `tb_order_items` VALUES (_binary '×\ï\ï¼\Öğ›‡\à.G	;',_binary '×?z¼\Öğ›‡\à.G	;',_binary '×Œ\â¼\Öğ›‡\à.G	;',1,3499.99,3499.99),(_binary '×òs¼\Öğ›‡\à.G	;',_binary '×?z¼\Öğ›‡\à.G	;',_binary '×NÊ¼\Öğ›‡\à.G	;',1,549.99,549.99),(_binary '×\Ş}¼\Öğ›‡\à.G	;',_binary '×hŒ¼\Öğ›‡\à.G	;',_binary '×R¼\Öğ›‡\à.G	;',1,599.99,599.99),(_binary '×\áz¼\Öğ›‡\à.G	;',_binary '×hŒ¼\Öğ›‡\à.G	;',_binary '×a¼\Öğ›‡\à.G	;',1,699.99,699.99),(_binary '×’K7¼\Öğ›‡\à.G	;',_binary '×‘°Q¼\Öğ›‡\à.G	;',_binary '×¶–¼\Öğ›‡\à.G	;',1,699.99,699.99),(_binary '×’NC¼\Öğ›‡\à.G	;',_binary '×‘°Q¼\Öğ›‡\à.G	;',_binary '×µÇ¼\Öğ›‡\à.G	;',1,599.99,599.99),(_binary '×’NÉ¼\Öğ›‡\à.G	;',_binary '×‘°Q¼\Öğ›‡\à.G	;',_binary '×R\ä¼\Öğ›‡\à.G	;',1,449.99,449.99),(_binary '×“0¼\Öğ›‡\à.G	;',_binary '×’Àı¼\Öğ›‡\à.G	;',_binary '×o¼\Öğ›‡\à.G	;',1,1199.99,1199.99),(_binary '×“\îÏ¼\Öğ›‡\à.G	;',_binary '×“s¼\Öğ›‡\à.G	;',_binary '×Œå‚¼\Öğ›‡\à.G	;',1,8999.99,8999.99),(_binary '×“ñp¼\Öğ›‡\à.G	;',_binary '×“s¼\Öğ›‡\à.G	;',_binary '×!Ü¼\Öğ›‡\à.G	;',1,3499.99,3499.99),(_binary '×”\×ó¼\Öğ›‡\à.G	;',_binary '×”x“¼\Öğ›‡\à.G	;',_binary '×±ª¼\Öğ›‡\à.G	;',1,4499.99,4499.99),(_binary '×”\Û4¼\Öğ›‡\à.G	;',_binary '×”x“¼\Öğ›‡\à.G	;',_binary '×µ\0¼\Öğ›‡\à.G	;',1,3299.99,3299.99),(_binary '×”Ûº¼\Öğ›‡\à.G	;',_binary '×”x“¼\Öğ›‡\à.G	;',_binary '×¶6¼\Öğ›‡\à.G	;',1,799.99,799.99);
/*!40000 ALTER TABLE `tb_order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_orders`
--

DROP TABLE IF EXISTS `tb_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_orders` (
  `id` binary(16) NOT NULL,
  `user_id` binary(16) NOT NULL,
  `status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDENTE',
  `total_amount` decimal(10,2) NOT NULL DEFAULT '0.00',
  `created_at` datetime NOT NULL,
  `paid_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_orders_user_id` (`user_id`),
  CONSTRAINT `fk_orders_user_id` FOREIGN KEY (`user_id`) REFERENCES `tb_users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_orders`
--

LOCK TABLES `tb_orders` WRITE;
/*!40000 ALTER TABLE `tb_orders` DISABLE KEYS */;
INSERT INTO `tb_orders` VALUES (_binary '×?z¼\Öğ›‡\à.G	;',_binary '×ŒI€¼\Öğ›‡\à.G	;','PAGO',4049.98,'2025-11-03 16:12:17','2025-11-08 19:15:02'),(_binary '×hŒ¼\Öğ›‡\à.G	;',_binary '×ŒI€¼\Öğ›‡\à.G	;','PAGO',1299.98,'2025-11-05 16:12:17','2025-11-05 16:12:17'),(_binary '×‘°Q¼\Öğ›‡\à.G	;',_binary '×ŒJš¼\Öğ›‡\à.G	;','PAGO',1849.97,'2025-11-01 16:12:17','2025-11-02 16:12:17'),(_binary '×’Àı¼\Öğ›‡\à.G	;',_binary '×ŒJš¼\Öğ›‡\à.G	;','PENDENTE',1199.99,'2025-11-06 16:12:17',NULL),(_binary '×“s¼\Öğ›‡\à.G	;',_binary '×ŒK,¼\Öğ›‡\à.G	;','PAGO',12499.98,'2025-10-29 16:12:17','2025-10-30 16:12:17'),(_binary '×”x“¼\Öğ›‡\à.G	;',_binary '×ŒE9¼\Öğ›‡\à.G	;','PAGO',8599.97,'2025-10-24 16:12:17','2025-10-25 16:12:17');
/*!40000 ALTER TABLE `tb_orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_products`
--

DROP TABLE IF EXISTS `tb_products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_products` (
  `id` binary(16) NOT NULL,
  `name` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `price` decimal(10,2) NOT NULL,
  `category` varchar(60) COLLATE utf8mb4_unicode_ci NOT NULL,
  `stock_quantity` int NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_products_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_products`
--

LOCK TABLES `tb_products` WRITE;
/*!40000 ALTER TABLE `tb_products` DISABLE KEYS */;
INSERT INTO `tb_products` VALUES (_binary '×Œ\â¼\Öğ›‡\à.G	;','Notebook Dell Inspiron 15','Notebook Dell Inspiron 15 3000, Intel Core i5, 8GB RAM, 256GB SSD, Windows 11',4099.99,'Computadores',9,'2025-11-08 16:12:17','2025-11-08 19:15:02'),(_binary '×Œä•¼\Öğ›‡\à.G	;','Notebook Gamer ASUS ROG','Notebook Gamer ASUS ROG Strix, AMD Ryzen 7, 16GB RAM, 512GB SSD, RTX 3060',8999.99,'Computadores',8,'2025-11-08 16:12:17','2025-11-08 16:12:17'),(_binary '×Œ\å¼\Öğ›‡\à.G	;','Desktop Gamer Completo','Desktop Gamer Intel Core i7, 16GB RAM, SSD 512GB, RTX 4070, Windows 11',7999.99,'Computadores',12,'2025-11-08 16:12:17','2025-11-08 16:12:17'),(_binary '×Œå‚¼\Öğ›‡\à.G	;','MacBook Air M2','MacBook Air 13\" com chip M2, 8GB RAM, 256GB SSD, tela Retina',8999.99,'Computadores',10,'2025-11-08 16:12:17','2025-11-08 16:12:17'),(_binary '×NÊ¼\Öğ›‡\à.G	;','Mouse Logitech MX Master 3','Mouse sem fio Logitech MX Master 3, ergonÃ´mico, botÃµes programÃ¡veis',549.99,'PerifÃ©ricos',49,'2025-11-08 16:12:17','2025-11-08 19:15:02'),(_binary '×Q|¼\Öğ›‡\à.G	;','Mouse Gamer Razer DeathAdder','Mouse Gamer Razer DeathAdder V3, 30.000 DPI, RGB Chroma',399.99,'PerifÃ©ricos',45,'2025-11-08 16:12:17','2025-11-08 16:12:17'),(_binary '×R¼\Öğ›‡\à.G	;','Teclado MecÃ¢nico RGB','Teclado MecÃ¢nico RGB, switches Gateron, retroiluminaÃ§Ã£o RGB, layout ABNT2',599.99,'PerifÃ©ricos',40,'2025-11-08 16:12:17','2025-11-08 16:12:17'),(_binary '×Rw¼\Öğ›‡\à.G	;','Teclado Logitech MX Keys','Teclado sem fio Logitech MX Keys, iluminaÃ§Ã£o adaptativa, teclas confortÃ¡veis',699.99,'PerifÃ©ricos',35,'2025-11-08 16:12:17','2025-11-08 16:12:17'),(_binary '×R\ä¼\Öğ›‡\à.G	;','Webcam Logitech C920','Webcam Logitech C920 HD Pro, Full HD 1080p, microfone estÃ©reo integrado',449.99,'PerifÃ©ricos',30,'2025-11-08 16:12:17','2025-11-08 16:12:17'),(_binary '×SD¼\Öğ›‡\à.G	;','Webcam Logitech Brio 4K','Webcam Logitech Brio 4K, resoluÃ§Ã£o Ultra HD, HDR, autofoco',1299.99,'PerifÃ©ricos',20,'2025-11-08 16:12:17','2025-11-08 16:12:17'),(_binary '×±ª¼\Öğ›‡\à.G	;','Placa de VÃ­deo NVIDIA RTX 4070','Placa de VÃ­deo NVIDIA GeForce RTX 4070, 12GB GDDR6X, Ray Tracing',4499.99,'Componentes',25,'2025-11-08 16:12:17','2025-11-08 16:12:17'),(_binary '×´u¼\Öğ›‡\à.G	;','Placa de VÃ­deo NVIDIA RTX 4060','Placa de VÃ­deo NVIDIA GeForce RTX 4060, 8GB GDDR6, DLSS 3',2499.99,'Componentes',30,'2025-11-08 16:12:17','2025-11-08 16:12:17'),(_binary '×µ\0¼\Öğ›‡\à.G	;','Processador AMD Ryzen 7 7800X3D','Processador AMD Ryzen 7 7800X3D, 8 nÃºcleos, 16 threads, 5.0GHz',3299.99,'Componentes',20,'2025-11-08 16:12:17','2025-11-08 16:12:17'),(_binary '×µc¼\Öğ›‡\à.G	;','Processador Intel Core i7-13700K','Processador Intel Core i7-13700K, 16 nÃºcleos, 24 threads, 5.4GHz',2899.99,'Componentes',22,'2025-11-08 16:12:17','2025-11-08 16:12:17'),(_binary '×µÇ¼\Öğ›‡\à.G	;','MemÃ³ria RAM Corsair 16GB DDR5','MemÃ³ria RAM Corsair Vengeance 16GB DDR5 5600MHz, 2x8GB, RGB',599.99,'Componentes',60,'2025-11-08 16:12:17','2025-11-08 16:12:17'),(_binary '×¶6¼\Öğ›‡\à.G	;','MemÃ³ria RAM Kingston 32GB DDR4','MemÃ³ria RAM Kingston Fury Beast 32GB DDR4 3200MHz, 2x16GB',799.99,'Componentes',50,'2025-11-08 16:12:17','2025-11-08 16:12:17'),(_binary '×¶–¼\Öğ›‡\à.G	;','SSD Samsung 1TB NVMe','SSD Samsung 980 PRO 1TB NVMe M.2, leitura 7000MB/s, escrita 5000MB/s',699.99,'Componentes',70,'2025-11-08 16:12:17','2025-11-08 16:12:17'),(_binary '×·¼\Öğ›‡\à.G	;','SSD Kingston 500GB SATA','SSD Kingston A400 500GB SATA 2.5\", leitura 500MB/s, escrita 450MB/s',299.99,'Componentes',80,'2025-11-08 16:12:17','2025-11-08 16:12:17'),(_binary '×o¼\Öğ›‡\à.G	;','Monitor LG UltraGear 24\"','Monitor LG UltraGear 24\" Full HD, 144Hz, 1ms, FreeSync, HDMI/DisplayPort',1199.99,'Monitores',25,'2025-11-08 16:12:17','2025-11-08 16:12:17'),(_binary '× \ï¼\Öğ›‡\à.G	;','Monitor Samsung Odyssey 27\"','Monitor Samsung Odyssey G5 27\" QHD, 144Hz, 1ms, Curvo, FreeSync Premium',1799.99,'Monitores',20,'2025-11-08 16:12:17','2025-11-08 16:12:17'),(_binary '×!u¼\Öğ›‡\à.G	;','Monitor ASUS ROG 32\" 4K','Monitor ASUS ROG Swift 32\" 4K UHD, 144Hz, HDR, G-Sync, IPS',5999.99,'Monitores',15,'2025-11-08 16:12:17','2025-11-08 16:12:17'),(_binary '×!Ü¼\Öğ›‡\à.G	;','Monitor Dell UltraSharp 27\"','Monitor Dell UltraSharp 27\" 4K UHD, IPS, USB-C, 99% sRGB, ajuste de altura',3499.99,'Monitores',18,'2025-11-08 16:12:17','2025-11-08 16:12:17'),(_binary '×a¼\Öğ›‡\à.G	;','Headset HyperX Cloud II','Headset HyperX Cloud II, som 7.1 surround, microfone removÃ­vel, USB',699.99,'Ãudio',40,'2025-11-08 16:12:17','2025-11-08 16:12:17'),(_binary '×‡¼\Öğ›‡\à.G	;','Headset SteelSeries Arctis 7','Headset SteelSeries Arctis 7, sem fio 2.4GHz, bateria 24h, DTS Headphone:X',1299.99,'Ãudio',30,'2025-11-08 16:12:17','2025-11-08 16:12:17'),(_binary '×‡¥¼\Öğ›‡\à.G	;','Fone de Ouvido Sony WH-1000XM5','Fone de Ouvido Sony WH-1000XM5, cancelamento de ruÃ­do, Bluetooth, bateria 30h',2499.99,'Ãudio',25,'2025-11-08 16:12:17','2025-11-08 16:12:17'),(_binary '×ˆ¼\Öğ›‡\à.G	;','Headset Logitech G Pro X','Headset Logitech G Pro X, som surround 7.1, microfone Blue VO!CE, drivers 50mm',899.99,'Ãudio',35,'2025-11-08 16:12:17','2025-11-08 16:12:17');
/*!40000 ALTER TABLE `tb_products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_users`
--

DROP TABLE IF EXISTS `tb_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_users` (
  `id` binary(16) NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_users_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_users`
--

LOCK TABLES `tb_users` WRITE;
/*!40000 ALTER TABLE `tb_users` DISABLE KEYS */;
INSERT INTO `tb_users` VALUES (_binary '×ŒE9¼\Öğ›‡\à.G	;','Roronoa Zoro','zoro@bytestore.com','$2a$10$6wQ7S1HeUD13Dxjntt1C8../k3KgQpDsqQWhu3TsmXKc/zf82Q9QW','ADMIN','2025-11-08 16:12:17'),(_binary '×ŒI€¼\Öğ›‡\à.G	;','Monkey D Luffy','luffy@bytestore.com','$2a$10$1CCw3r3B1RkX2gP7y.x1vuD4QIerzYq11m1amlz6hscN/JfSBNdBq','USER','2025-11-08 16:12:17'),(_binary '×ŒJš¼\Öğ›‡\à.G	;','Tony Tony Chopper','chopper@bytestore.com','$2a$10$Mm8.4HmpPH1wh9aEh9B1Le.KPvxuvbvpdn9xpFC9EwWPip/h3uzSS','USER','2025-11-08 16:12:17'),(_binary '×ŒK,¼\Öğ›‡\à.G	;','Nico Robin','robin@bytestore.com','$2a$10$KaWR5BuJKIrzUxZTqmgEg.CpW.d0/GXCeUb9P4YzTh/E73BxoSHhC','ADMIN','2025-11-08 16:12:17');
/*!40000 ALTER TABLE `tb_users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-08 18:44:28
