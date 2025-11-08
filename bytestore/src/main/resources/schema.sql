-- =====================================================
-- ByteStore Database Schema
-- Database: bytestore_db
-- Version: 1.0.0
-- Date: NOV/2025
-- =====================================================
-- 
-- Este arquivo contém a estrutura completa do banco de dados ByteStore
-- e os dados iniciais (usuários, produtos, pedidos e itens).
-- 
-- ORDEM DE EXECUÇÃO:
-- 1. Criar banco de dados
-- 2. Criar tabelas (users, products, orders, order_items)
-- 3. Inserir dados iniciais (usuários, produtos, pedidos, itens)
-- =====================================================

-- =====================================================
-- CRIAR BANCO DE DADOS
-- =====================================================

DROP DATABASE IF EXISTS bytestore_db;

CREATE DATABASE bytestore_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE bytestore_db;

-- =====================================================
-- CRIAR TABELA tb_users
-- =====================================================

CREATE TABLE IF NOT EXISTS tb_users (
    id BINARY(16) NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at DATETIME NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_users_email (email)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- CRIAR TABELA tb_products
-- =====================================================

CREATE TABLE IF NOT EXISTS tb_products (
    id BINARY(16) NOT NULL,
    name VARCHAR(120) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    category VARCHAR(60) NOT NULL,
    stock_quantity INT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_products_name (name)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- CRIAR TABELA tb_orders
-- =====================================================

CREATE TABLE IF NOT EXISTS tb_orders (
    id BINARY(16) NOT NULL,
    user_id BINARY(16) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',
    total_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    created_at DATETIME NOT NULL,
    paid_at DATETIME NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_orders_user_id 
        FOREIGN KEY (user_id) 
        REFERENCES tb_users(id)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- CRIAR TABELA tb_order_items
-- =====================================================

CREATE TABLE IF NOT EXISTS tb_order_items (
    id BINARY(16) NOT NULL,
    order_id BINARY(16) NOT NULL,
    product_id BINARY(16) NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_order_items_order_id 
        FOREIGN KEY (order_id) 
        REFERENCES tb_orders(id) 
        ON DELETE CASCADE,
    CONSTRAINT fk_order_items_product_id 
        FOREIGN KEY (product_id) 
        REFERENCES tb_products(id)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- INSERIR USUÁRIOS
-- =====================================================

INSERT INTO tb_users (id, name, email, password, role, created_at) VALUES
    (UUID_TO_BIN(UUID()), 'Roronoa Zoro', 'zoro@bytestore.com', '$2a$10$6wQ7S1HeUD13Dxjntt1C8../k3KgQpDsqQWhu3TsmXKc/zf82Q9QW', 'ADMIN', NOW()),
    (UUID_TO_BIN(UUID()), 'Monkey D Luffy', 'luffy@bytestore.com', '$2a$10$1CCw3r3B1RkX2gP7y.x1vuD4QIerzYq11m1amlz6hscN/JfSBNdBq', 'USER', NOW()),
    (UUID_TO_BIN(UUID()), 'Tony Tony Chopper', 'chopper@bytestore.com', '$2a$10$Mm8.4HmpPH1wh9aEh9B1Le.KPvxuvbvpdn9xpFC9EwWPip/h3uzSS', 'USER', NOW()),
    (UUID_TO_BIN(UUID()), 'Nico Robin', 'robin@bytestore.com', '$2a$10$KaWR5BuJKIrzUxZTqmgEg.CpW.d0/GXCeUb9P4YzTh/E73BxoSHhC', 'ADMIN', NOW());

-- =====================================================
-- INSERIR PRODUTOS - COMPUTADORES
-- =====================================================

INSERT INTO tb_products (id, name, description, price, category, stock_quantity, created_at, updated_at) VALUES
    (UUID_TO_BIN(UUID()), 'Notebook Dell Inspiron 15', 'Notebook Dell Inspiron 15 3000, Intel Core i5, 8GB RAM, 256GB SSD, Windows 11', 3499.99, 'Computadores', 15, NOW(), NOW()),
    (UUID_TO_BIN(UUID()), 'Notebook Gamer ASUS ROG', 'Notebook Gamer ASUS ROG Strix, AMD Ryzen 7, 16GB RAM, 512GB SSD, RTX 3060', 8999.99, 'Computadores', 8, NOW(), NOW()),
    (UUID_TO_BIN(UUID()), 'Desktop Gamer Completo', 'Desktop Gamer Intel Core i7, 16GB RAM, SSD 512GB, RTX 4070, Windows 11', 7999.99, 'Computadores', 12, NOW(), NOW()),
    (UUID_TO_BIN(UUID()), 'MacBook Air M2', 'MacBook Air 13" com chip M2, 8GB RAM, 256GB SSD, tela Retina', 8999.99, 'Computadores', 10, NOW(), NOW());

-- =====================================================
-- INSERIR PRODUTOS - PERIFÉRICOS
-- =====================================================

INSERT INTO tb_products (id, name, description, price, category, stock_quantity, created_at, updated_at) VALUES
    (UUID_TO_BIN(UUID()), 'Mouse Logitech MX Master 3', 'Mouse sem fio Logitech MX Master 3, ergonômico, botões programáveis', 549.99, 'Periféricos', 50, NOW(), NOW()),
    (UUID_TO_BIN(UUID()), 'Mouse Gamer Razer DeathAdder', 'Mouse Gamer Razer DeathAdder V3, 30.000 DPI, RGB Chroma', 399.99, 'Periféricos', 45, NOW(), NOW()),
    (UUID_TO_BIN(UUID()), 'Teclado Mecânico RGB', 'Teclado Mecânico RGB, switches Gateron, retroiluminação RGB, layout ABNT2', 599.99, 'Periféricos', 40, NOW(), NOW()),
    (UUID_TO_BIN(UUID()), 'Teclado Logitech MX Keys', 'Teclado sem fio Logitech MX Keys, iluminação adaptativa, teclas confortáveis', 699.99, 'Periféricos', 35, NOW(), NOW()),
    (UUID_TO_BIN(UUID()), 'Webcam Logitech C920', 'Webcam Logitech C920 HD Pro, Full HD 1080p, microfone estéreo integrado', 449.99, 'Periféricos', 30, NOW(), NOW()),
    (UUID_TO_BIN(UUID()), 'Webcam Logitech Brio 4K', 'Webcam Logitech Brio 4K, resolução Ultra HD, HDR, autofoco', 1299.99, 'Periféricos', 20, NOW(), NOW());

-- =====================================================
-- INSERIR PRODUTOS - COMPONENTES
-- =====================================================

INSERT INTO tb_products (id, name, description, price, category, stock_quantity, created_at, updated_at) VALUES
    (UUID_TO_BIN(UUID()), 'Placa de Vídeo NVIDIA RTX 4070', 'Placa de Vídeo NVIDIA GeForce RTX 4070, 12GB GDDR6X, Ray Tracing', 4499.99, 'Componentes', 25, NOW(), NOW()),
    (UUID_TO_BIN(UUID()), 'Placa de Vídeo NVIDIA RTX 4060', 'Placa de Vídeo NVIDIA GeForce RTX 4060, 8GB GDDR6, DLSS 3', 2499.99, 'Componentes', 30, NOW(), NOW()),
    (UUID_TO_BIN(UUID()), 'Processador AMD Ryzen 7 7800X3D', 'Processador AMD Ryzen 7 7800X3D, 8 núcleos, 16 threads, 5.0GHz', 3299.99, 'Componentes', 20, NOW(), NOW()),
    (UUID_TO_BIN(UUID()), 'Processador Intel Core i7-13700K', 'Processador Intel Core i7-13700K, 16 núcleos, 24 threads, 5.4GHz', 2899.99, 'Componentes', 22, NOW(), NOW()),
    (UUID_TO_BIN(UUID()), 'Memória RAM Corsair 16GB DDR5', 'Memória RAM Corsair Vengeance 16GB DDR5 5600MHz, 2x8GB, RGB', 599.99, 'Componentes', 60, NOW(), NOW()),
    (UUID_TO_BIN(UUID()), 'Memória RAM Kingston 32GB DDR4', 'Memória RAM Kingston Fury Beast 32GB DDR4 3200MHz, 2x16GB', 799.99, 'Componentes', 50, NOW(), NOW()),
    (UUID_TO_BIN(UUID()), 'SSD Samsung 1TB NVMe', 'SSD Samsung 980 PRO 1TB NVMe M.2, leitura 7000MB/s, escrita 5000MB/s', 699.99, 'Componentes', 70, NOW(), NOW()),
    (UUID_TO_BIN(UUID()), 'SSD Kingston 500GB SATA', 'SSD Kingston A400 500GB SATA 2.5", leitura 500MB/s, escrita 450MB/s', 299.99, 'Componentes', 80, NOW(), NOW());

-- =====================================================
-- INSERIR PRODUTOS - MONITORES
-- =====================================================

INSERT INTO tb_products (id, name, description, price, category, stock_quantity, created_at, updated_at) VALUES
    (UUID_TO_BIN(UUID()), 'Monitor LG UltraGear 24"', 'Monitor LG UltraGear 24" Full HD, 144Hz, 1ms, FreeSync, HDMI/DisplayPort', 1199.99, 'Monitores', 25, NOW(), NOW()),
    (UUID_TO_BIN(UUID()), 'Monitor Samsung Odyssey 27"', 'Monitor Samsung Odyssey G5 27" QHD, 144Hz, 1ms, Curvo, FreeSync Premium', 1799.99, 'Monitores', 20, NOW(), NOW()),
    (UUID_TO_BIN(UUID()), 'Monitor ASUS ROG 32" 4K', 'Monitor ASUS ROG Swift 32" 4K UHD, 144Hz, HDR, G-Sync, IPS', 5999.99, 'Monitores', 15, NOW(), NOW()),
    (UUID_TO_BIN(UUID()), 'Monitor Dell UltraSharp 27"', 'Monitor Dell UltraSharp 27" 4K UHD, IPS, USB-C, 99% sRGB, ajuste de altura', 3499.99, 'Monitores', 18, NOW(), NOW());

-- =====================================================
-- INSERIR PRODUTOS - ÁUDIO
-- =====================================================

INSERT INTO tb_products (id, name, description, price, category, stock_quantity, created_at, updated_at) VALUES
    (UUID_TO_BIN(UUID()), 'Headset HyperX Cloud II', 'Headset HyperX Cloud II, som 7.1 surround, microfone removível, USB', 699.99, 'Áudio', 40, NOW(), NOW()),
    (UUID_TO_BIN(UUID()), 'Headset SteelSeries Arctis 7', 'Headset SteelSeries Arctis 7, sem fio 2.4GHz, bateria 24h, DTS Headphone:X', 1299.99, 'Áudio', 30, NOW(), NOW()),
    (UUID_TO_BIN(UUID()), 'Fone de Ouvido Sony WH-1000XM5', 'Fone de Ouvido Sony WH-1000XM5, cancelamento de ruído, Bluetooth, bateria 30h', 2499.99, 'Áudio', 25, NOW(), NOW()),
    (UUID_TO_BIN(UUID()), 'Headset Logitech G Pro X', 'Headset Logitech G Pro X, som surround 7.1, microfone Blue VO!CE, drivers 50mm', 899.99, 'Áudio', 35, NOW(), NOW());

-- =====================================================
-- INSERIR PEDIDOS
-- =====================================================

-- Pedido 1: Luffy - PENDENTE
SET @luffy_id = (SELECT id FROM tb_users WHERE email = 'luffy@bytestore.com' LIMIT 1);
SET @notebook_dell_id = (SELECT id FROM tb_products WHERE name = 'Notebook Dell Inspiron 15' LIMIT 1);
SET @mouse_logitech_id = (SELECT id FROM tb_products WHERE name = 'Mouse Logitech MX Master 3' LIMIT 1);

SET @order1_id = UUID_TO_BIN(UUID());
INSERT INTO tb_orders (id, user_id, status, total_amount, created_at, paid_at) VALUES
    (@order1_id, @luffy_id, 'PENDENTE', 4049.98, DATE_SUB(NOW(), INTERVAL 5 DAY), NULL);

INSERT INTO tb_order_items (id, order_id, product_id, quantity, unit_price, subtotal) VALUES
    (UUID_TO_BIN(UUID()), @order1_id, @notebook_dell_id, 1, 3499.99, 3499.99),
    (UUID_TO_BIN(UUID()), @order1_id, @mouse_logitech_id, 1, 549.99, 549.99);

-- Pedido 2: Luffy - PAGO
SET @teclado_mecanico_id = (SELECT id FROM tb_products WHERE name = 'Teclado Mecânico RGB' LIMIT 1);
SET @headset_hyperx_id = (SELECT id FROM tb_products WHERE name = 'Headset HyperX Cloud II' LIMIT 1);

SET @order2_id = UUID_TO_BIN(UUID());
INSERT INTO tb_orders (id, user_id, status, total_amount, created_at, paid_at) VALUES
    (@order2_id, @luffy_id, 'PAGO', 1299.98, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY));

INSERT INTO tb_order_items (id, order_id, product_id, quantity, unit_price, subtotal) VALUES
    (UUID_TO_BIN(UUID()), @order2_id, @teclado_mecanico_id, 1, 599.99, 599.99),
    (UUID_TO_BIN(UUID()), @order2_id, @headset_hyperx_id, 1, 699.99, 699.99);

-- Pedido 3: Chopper - PAGO
SET @chopper_id = (SELECT id FROM tb_users WHERE email = 'chopper@bytestore.com' LIMIT 1);
SET @ssd_samsung_id = (SELECT id FROM tb_products WHERE name = 'SSD Samsung 1TB NVMe' LIMIT 1);
SET @ram_corsair_id = (SELECT id FROM tb_products WHERE name = 'Memória RAM Corsair 16GB DDR5' LIMIT 1);
SET @webcam_c920_id = (SELECT id FROM tb_products WHERE name = 'Webcam Logitech C920' LIMIT 1);

SET @order3_id = UUID_TO_BIN(UUID());
INSERT INTO tb_orders (id, user_id, status, total_amount, created_at, paid_at) VALUES
    (@order3_id, @chopper_id, 'PAGO', 1849.97, DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY));

INSERT INTO tb_order_items (id, order_id, product_id, quantity, unit_price, subtotal) VALUES
    (UUID_TO_BIN(UUID()), @order3_id, @ssd_samsung_id, 1, 699.99, 699.99),
    (UUID_TO_BIN(UUID()), @order3_id, @ram_corsair_id, 1, 599.99, 599.99),
    (UUID_TO_BIN(UUID()), @order3_id, @webcam_c920_id, 1, 449.99, 449.99);

-- Pedido 4: Chopper - PENDENTE
SET @monitor_lg_id = (SELECT id FROM tb_products WHERE name = 'Monitor LG UltraGear 24"' LIMIT 1);

SET @order4_id = UUID_TO_BIN(UUID());
INSERT INTO tb_orders (id, user_id, status, total_amount, created_at, paid_at) VALUES
    (@order4_id, @chopper_id, 'PENDENTE', 1199.99, DATE_SUB(NOW(), INTERVAL 2 DAY), NULL);

INSERT INTO tb_order_items (id, order_id, product_id, quantity, unit_price, subtotal) VALUES
    (UUID_TO_BIN(UUID()), @order4_id, @monitor_lg_id, 1, 1199.99, 1199.99);

-- Pedido 5: Robin - PAGO
SET @robin_id = (SELECT id FROM tb_users WHERE email = 'robin@bytestore.com' LIMIT 1);
SET @macbook_id = (SELECT id FROM tb_products WHERE name = 'MacBook Air M2' LIMIT 1);
SET @monitor_dell_id = (SELECT id FROM tb_products WHERE name = 'Monitor Dell UltraSharp 27"' LIMIT 1);

SET @order5_id = UUID_TO_BIN(UUID());
INSERT INTO tb_orders (id, user_id, status, total_amount, created_at, paid_at) VALUES
    (@order5_id, @robin_id, 'PAGO', 12499.98, DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 9 DAY));

INSERT INTO tb_order_items (id, order_id, product_id, quantity, unit_price, subtotal) VALUES
    (UUID_TO_BIN(UUID()), @order5_id, @macbook_id, 1, 8999.99, 8999.99),
    (UUID_TO_BIN(UUID()), @order5_id, @monitor_dell_id, 1, 3499.99, 3499.99);

-- Pedido 6: Zoro - PAGO
SET @zoro_id = (SELECT id FROM tb_users WHERE email = 'zoro@bytestore.com' LIMIT 1);
SET @rtx_4070_id = (SELECT id FROM tb_products WHERE name = 'Placa de Vídeo NVIDIA RTX 4070' LIMIT 1);
SET @ryzen_id = (SELECT id FROM tb_products WHERE name = 'Processador AMD Ryzen 7 7800X3D' LIMIT 1);
SET @ram_kingston_id = (SELECT id FROM tb_products WHERE name = 'Memória RAM Kingston 32GB DDR4' LIMIT 1);

SET @order6_id = UUID_TO_BIN(UUID());
INSERT INTO tb_orders (id, user_id, status, total_amount, created_at, paid_at) VALUES
    (@order6_id, @zoro_id, 'PAGO', 8599.97, DATE_SUB(NOW(), INTERVAL 15 DAY), DATE_SUB(NOW(), INTERVAL 14 DAY));

INSERT INTO tb_order_items (id, order_id, product_id, quantity, unit_price, subtotal) VALUES
    (UUID_TO_BIN(UUID()), @order6_id, @rtx_4070_id, 1, 4499.99, 4499.99),
    (UUID_TO_BIN(UUID()), @order6_id, @ryzen_id, 1, 3299.99, 3299.99),
    (UUID_TO_BIN(UUID()), @order6_id, @ram_kingston_id, 1, 799.99, 799.99);

-- =====================================================
-- REFERÊNCIAS
-- =====================================================
-- 
-- SENHAS DOS USUÁRIOS (para referência e consulta):
--   - Roronoa Zoro (zoro@bytestore.com): "Santoryu123"
--   - Monkey D Luffy (luffy@bytestore.com): "GomuGomu123"
--   - Tony Tony Chopper (chopper@bytestore.com): "RumbleBall123"
--   - Nico Robin (robin@bytestore.com): "HanaHana123"
-- 
-- CATEGORIAS DE PRODUTOS:
--   - Computadores
--   - Periféricos
--   - Componentes
--   - Monitores
--   - Áudio
-- 
-- STATUS DE PEDIDOS:
--   - PENDENTE: Pedido criado, aguardando pagamento
--   - PAGO: Pedido pago, estoque atualizado
--   - CANCELADO: Pedido cancelado
-- 
-- =====================================================
