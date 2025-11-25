# Sistema de Gestión de Call Center (Desktop)

Sistema de escritorio desarrollado en **Java** para la administración integral de un centro de llamadas. El proyecto está construido bajo los principios de la **Arquitectura Hexagonal, Puertos y Adaptadores**, garantizando un desacoplamiento entre la lógica de negocio, la interfaz gráfica y la persistencia de datos.

##  Características Principales

El sistema permite gestionar los cuatro pilares de la operación:

*  Gestión de Agentes: Registro, actualización y control de empleados, turnos y niveles de experiencia.
*  Gestión de Clientes: Base de datos de clientes con información de contacto detallada.
*  Gestión de Campañas: Administración de estrategias de marketing Ventas, Soporte, Encuestas con control de fechas.
*  Registro de Llamadas: Módulo transaccional que vincula un Agente, un Cliente y una Campaña para registrar el resultado de la interacción y su duración.

## Arquitectura de Software

El proyecto sigue estrictamente la **Arquitectura Hexagonal**:

* Dominio: Entidades puras y Excepciones de negocio. Sin dependencias externas.
* Puertos: `input-ports` y `output-ports` que definen los contratos de comunicación.
* Aplicación: Implementación de casos de uso, DTOs y Mappers.
* Infraestructura:
    * Entrada: Interfaz Gráfica java Swing.
    * Salida: Adaptadores de persistencia MySQL con JDBC.

##  Tecnologías Utilizadas

* Lenguaje: Java JDK 17+.
* Interfaz Gráfica: Java Swing.
* Base de Datos: MySQL 8.0 Heidi.
* Gestión de Dependencias: Apache Maven.
* Persistencia: JDBC mysql-connector-j.
* IDE Recomendado: Apache NetBeans.

---

##  Instalación y Configuración

Sigue estos pasos para ejecutar el proyecto en tu entorno local:

### 1. Requisitos Previos
* Tener instalado **Java JDK 17** o superior.
* Tener un servidor MySQL activo Recomendado: **Laragon** o MySQL Workbench.

### 2. Base de Datos
Debes crear una base de datos llamada `callcenter_db`. Ejecuta el siguiente script SQL en tu cliente de base de datos (HeidiSQL, DBeaver, Workbench) para crear las tablas y relaciones:

<details>
<summary><strong>📄 Clic aquí para ver el Script SQL completo</strong></summary>

```sql
CREATE DATABASE IF NOT EXISTS callcenter_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE callcenter_db;

-- 1. Tabla Agentes
CREATE TABLE IF NOT EXISTS agentes (
    id_agente INT AUTO_INCREMENT PRIMARY KEY,
    nombre_completo VARCHAR(100) NOT NULL,
    numero_empleado VARCHAR(20) NOT NULL UNIQUE,
    telefono_contacto VARCHAR(20),
    email VARCHAR(100) NOT NULL,
    horario_turno VARCHAR(50),
    nivel_experiencia VARCHAR(50)) ENGINE=InnoDB;

-- 2. Tabla Clientes
CREATE TABLE IF NOT EXISTS clientes (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre_completo VARCHAR(100) NOT NULL,
    documento_identidad VARCHAR(20) NOT NULL UNIQUE,
    telefono VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    direccion VARCHAR(150)) ENGINE=InnoDB;

-- 3. Tabla Campañas
CREATE TABLE IF NOT EXISTS campanias (
    id_campania INT AUTO_INCREMENT PRIMARY KEY,
    nombre_campania VARCHAR(100) NOT NULL UNIQUE,
    tipo_campania VARCHAR(50),
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE,
    supervisores_cargo VARCHAR(100),
    descripcion_objetivos TEXT) ENGINE=InnoDB;

-- 4. Tabla Llamadas (Relacional)
CREATE TABLE IF NOT EXISTS llamadas (
    id_llamada INT AUTO_INCREMENT PRIMARY KEY,
    fecha_hora DATETIME DEFAULT CURRENT_TIMESTAMP,
    duracion_segundos INT NOT NULL,
    detalle_resultado TEXT NOT NULL,
    observaciones TEXT,
    id_agente INT NOT NULL,
    id_campania INT NOT NULL,
    id_cliente INT NOT NULL,
    FOREIGN KEY (id_agente) REFERENCES agentes(id_agente),
    FOREIGN KEY (id_campania) REFERENCES campanias(id_campania),
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente)) ENGINE=InnoDB;


---
Autores
**Jose Rivera - Desarrollo de módulos Agentes, Clientes y Arquitectura Base.**
**Carlos Martinez - Desarrollo de módulos de Negocio Campañas, Llamadas e Infraestructura de Datos.**
