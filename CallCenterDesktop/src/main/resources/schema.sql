-- =============================================
-- SCRIPT DE INICIALIZACIÓN DE BASE DE DATOS
-- Call Center System - Hexagonal Architecture
-- =============================================

-- Crear base de datos si no existe
CREATE DATABASE IF NOT EXISTS callcenter_db 
    CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci;

USE callcenter_db;

-- =============================================
-- TABLA: AGENTES
-- =============================================
CREATE TABLE IF NOT EXISTS agentes (
    id_agente BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_completo VARCHAR(100) NOT NULL,
    numero_empleado VARCHAR(50) UNIQUE NOT NULL,
    telefono_contacto VARCHAR(20),
    email VARCHAR(100),
    horario_turno VARCHAR(50),
    nivel_experiencia VARCHAR(50),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    INDEX idx_numero_empleado (numero_empleado),
    INDEX idx_nombre (nombre_completo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================
-- TABLA: CLIENTES
-- =============================================
CREATE TABLE IF NOT EXISTS clientes (
    id_cliente BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_completo VARCHAR(100) NOT NULL,
    documento_identidad VARCHAR(50) UNIQUE NOT NULL,
    telefono VARCHAR(20),
    email VARCHAR(100),
    direccion VARCHAR(200),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    INDEX idx_documento (documento_identidad),
    INDEX idx_nombre (nombre_completo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================
-- TABLA: CAMPAÑAS
-- =============================================
CREATE TABLE IF NOT EXISTS campanias (
    id_campania BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_campania VARCHAR(100) NOT NULL,
    tipo_campania VARCHAR(50),
    fecha_inicio DATE,
    fecha_fin DATE,
    supervisores_cargo VARCHAR(200),
    descripcion_objetivos TEXT,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activa BOOLEAN DEFAULT TRUE,
    INDEX idx_nombre (nombre_campania),
    INDEX idx_fechas (fecha_inicio, fecha_fin)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================
-- TABLA: LLAMADAS
-- =============================================
CREATE TABLE IF NOT EXISTS llamadas (
    id_llamada BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha_hora DATETIME NOT NULL,
    duracion INT COMMENT 'Duración en segundos',
    detalle_resultado VARCHAR(100),
    observaciones TEXT,
    id_agente BIGINT NOT NULL,
    id_cliente BIGINT NOT NULL,
    id_campania BIGINT NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Llaves foráneas con integridad referencial
    FOREIGN KEY (id_agente) REFERENCES agentes(id_agente) 
        ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente) 
        ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (id_campania) REFERENCES campanias(id_campania) 
        ON DELETE RESTRICT ON UPDATE CASCADE,
    
    INDEX idx_fecha (fecha_hora),
    INDEX idx_agente (id_agente),
    INDEX idx_cliente (id_cliente),
    INDEX idx_campania (id_campania)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================
-- DATOS DE PRUEBA (OPCIONAL)
-- =============================================

-- Insertar agentes de ejemplo
INSERT IGNORE INTO agentes (nombre_completo, numero_empleado, telefono_contacto, email, horario_turno, nivel_experiencia)
VALUES 
    ('María García López', 'AG001', '3001234567', 'maria.garcia@callcenter.com', 'Mañana', 'Senior'),
    ('Carlos Rodríguez', 'AG002', '3007654321', 'carlos.rodriguez@callcenter.com', 'Tarde', 'Junior');

-- Insertar clientes de ejemplo
INSERT IGNORE INTO clientes (nombre_completo, documento_identidad, telefono, email, direccion)
VALUES 
    ('Juan Pérez', 'CC-1234567', '3101234567', 'juan.perez@email.com', 'Calle 123 #45-67'),
    ('Ana Martínez', 'CC-7654321', '3157654321', 'ana.martinez@email.com', 'Avenida 45 #12-34');

-- Insertar campañas de ejemplo
INSERT IGNORE INTO campanias (nombre_campania, tipo_campania, fecha_inicio, fecha_fin, supervisores_cargo, descripcion_objetivos)
VALUES 
    ('Campaña Navideña 2025', 'Ventas', '2025-12-01', '2025-12-31', 'Supervisor Principal', 'Incrementar ventas en temporada navideña'),
    ('Retención de Clientes', 'Retención', '2025-01-01', '2025-06-30', 'Equipo de Calidad', 'Mejorar satisfacción y retención');

-- =============================================
-- VERIFICACIÓN
-- =============================================
SELECT 'Base de datos inicializada correctamente' AS estado;
