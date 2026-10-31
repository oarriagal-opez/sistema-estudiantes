-- 1. Crear y seleccionar la base de datos
CREATE DATABASE IF NOT EXISTS sistema_estudiantes;
USE sistema_estudiantes;

-- 2. Tabla de Personas (Clientes/Empleados)
CREATE TABLE IF NOT EXISTS persona (
    id_persona INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    nit VARCHAR(20) UNIQUE,
    direccion VARCHAR(200),
    email VARCHAR(100)
);

-- 3. Tabla para administrar MÚLTIPLES teléfonos por Cliente (Relación 1:N)
CREATE TABLE IF NOT EXISTS telefono (
    id_telefono INT AUTO_INCREMENT PRIMARY KEY,
    id_persona INT NOT NULL,
    numero VARCHAR(20) NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    FOREIGN KEY (id_persona) REFERENCES persona(id_persona) ON DELETE CASCADE
);

-- 4. Tabla de Estudiantes
CREATE TABLE IF NOT EXISTS estudiante (
    id_estudiante INT AUTO_INCREMENT PRIMARY KEY,
    carne VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    carrera VARCHAR(100),
    correo VARCHAR(100)
);

-- 5. Tabla de Usuarios (Inicio de Sesión)
CREATE TABLE IF NOT EXISTS usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(50) DEFAULT 'ADMIN'
);

-- 6. Tabla de Productos
CREATE TABLE IF NOT EXISTS producto (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL DEFAULT 0
);

-- 7. Tabla de Facturas
CREATE TABLE IF NOT EXISTS factura (
    id_factura INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT,
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10,2) DEFAULT 0.00,
    tipo_entrega VARCHAR(50) DEFAULT 'venta en tienda',
    estado_despacho VARCHAR(50) DEFAULT 'Completado',
    FOREIGN KEY (id_cliente) REFERENCES persona(id_persona) ON DELETE SET NULL
);

-- 8. Tabla Detalle de Facturas
CREATE TABLE IF NOT EXISTS detalle_factura (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_factura INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_factura) REFERENCES factura(id_factura) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto)
);

-- 9. Tabla de Despacho / Rastreo
CREATE TABLE IF NOT EXISTS despacho (
    id_despacho INT AUTO_INCREMENT PRIMARY KEY,
    id_factura INT NOT NULL UNIQUE,
    tipo_entrega VARCHAR(50) DEFAULT 'A domicilio',
    estado VARCHAR(50) DEFAULT 'Empaquetando',
    FOREIGN KEY (id_factura) REFERENCES factura(id_factura) ON DELETE CASCADE
);

-- 10. Tabla de Módulos (Permisos)
CREATE TABLE IF NOT EXISTS modulo (
    id_modulo INT AUTO_INCREMENT PRIMARY KEY,
    nombre_modulo VARCHAR(50) NOT NULL UNIQUE
);

-- 11. Tabla Intermedia de Permisos por Usuario
CREATE TABLE IF NOT EXISTS usuario_permiso (
    id_usuario INT NOT NULL,
    id_modulo INT NOT NULL,
    PRIMARY KEY (id_usuario, id_modulo),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_modulo) REFERENCES modulo(id_modulo) ON DELETE CASCADE
);

-- ========================================================
-- DATOS INICIALES DE PRUEBA
-- ========================================================

-- Insertar usuario inicial admin
INSERT IGNORE INTO usuario (username, password, rol) 
VALUES ('admin', '1234', 'ADMIN');

-- Insertar cliente inicial
INSERT IGNORE INTO persona (id_persona, nombre, nit, direccion, email) 
VALUES (1, 'Consumidor Final', 'CF', 'Ciudad', 'cf@correo.com');

-- Insertar teléfono inicial asociado al cliente (id_persona = 1)
INSERT IGNORE INTO telefono (id_persona, numero, tipo) 
VALUES (1, '00000000', 'Personal');

-- Insertar producto inicial
INSERT IGNORE INTO producto (nombre, precio, stock) 
VALUES ('Cuaderno Universitario', 15.50, 100);

-- Insertar nombres de módulos
INSERT IGNORE INTO modulo (nombre_modulo) VALUES 
('Clientes'),
('Empleados'),
('Productos'),
('Usuarios'),
('Facturas');

-- Asignar permisos al usuario admin (id_usuario = 1)
INSERT IGNORE INTO usuario_permiso (id_usuario, id_modulo) VALUES 
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5);

-- ========================================================
-- CONSULTAS PARA VERIFICAR
-- ========================================================
SELECT * FROM persona;


SELECT * FROM telefono;
SELECT * FROM estudiante;
SELECT * FROM usuario;
SELECT * FROM modulo;
SELECT * FROM usuario_permiso;
SELECT * FROM producto;
SELECT * FROM factura;
SELECT * FROM detalle_factura;
SELECT * FROM despacho;