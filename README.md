# Sistema de Gestión de Call Center (MySQL Edition)

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Architecture](https://img.shields.io/badge/Architecture-Hexagonal-blue?style=for-the-badge)

Aplicación empresarial de escritorio para la gestión integral de un Call Center. Diseñada bajo **Arquitectura Hexagonal**, este sistema implementa una persistencia robusta sobre **MySQL Server** con capacidades de **Auto-Despliegue**.

---

## Innovación: "Auto-Database Deployment"

A diferencia de las aplicaciones tradicionales que requieren ejecutar scripts SQL manualmente antes de empezar, este sistema es inteligente:

1.  **Detección Automática:** Al iniciar, verifica si la base de datos `callcenter_db` existe en tu servidor MySQL.
2.  **Auto-Creación:** Si no existe, la aplicación se conecta al servidor, crea la base de datos y construye todas las tablas y relaciones automáticamente.
3.  **Transparencia:** El usuario final no necesita interactuar con consolas SQL ni Workbench. Solo necesita tener el servicio de MySQL encendido.

---

## Arquitectura Técnica

El proyecto sigue estrictamente el patrón de **Puertos y Adaptadores**:

```text
com.udc.callcenterdesktop
├── dominio (NÚCLEO)
│   ├── modelo       # Entidades (Agente, Cliente, Llamada)
│   └── puertos      # Interfaces (Repositorios y Servicios)
│
├── aplicacion (LÓGICA)
│   ├── servicios    # Casos de uso y Validaciones de Negocio
│   └── mapper       # Conversión DTO <-> Entidad
│
└── infraestructura (ADAPTADORES)
    ├── entrada      # GUI (Java Swing)
    └── salida       # Persistencia (MySQL JDBC Implementation)
Características de Ingeniería
Integridad Referencial: Validaciones cruzadas en capa de servicio y restricciones FOREIGN KEY en base de datos.

Seguridad: Uso de PreparedStatement para prevenir inyección SQL.

Manejo de Errores: Wrapper de excepciones SQL a excepciones de dominio (CallCenterException).

Configuración de Conexión
El sistema carga las credenciales desde un archivo externo para facilitar el despliegue.

Archivo: src/main/resources/config.properties

Properties

# Driver oficial de MySQL 8
db.driver=com.mysql.cj.jdbc.Driver

# Conexión al servidor local
db.url=jdbc:mysql://localhost:3306/callcenter_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC

# Credenciales de tu MySQL Server local
db.user=root
db.password=TU_CONTRASEÑA
Requisitos e Instalación
Prerrequisitos
Java JDK 11+

MySQL Server (8.0+) instalado y ejecutándose (XAMPP, WAMP o Community Server).

Maven para la gestión de dependencias.

Puesta en Marcha
Clonar el repositorio:

Bash

git clone [https://github.com/tu-usuario/CallCenterHexagonal.git](https://github.com/tu-usuario/CallCenterHexagonal.git)
Configurar Credenciales: Edita el archivo config.properties con tu usuario y contraseña de MySQL.

Ejecutar: Corre la clase Main.java.

¡Observa la consola! Verás cómo el sistema crea la base de datos por ti la primera vez.

Modelo de Datos
El sistema gestiona 4 entidades principales con relaciones fuertes:

Agentes: Personal del call center.

Clientes: Base de datos de contactos.

Campañas: Proyectos de marketing activos.

Llamadas: Registro transaccional que vincula Agente + Cliente + Campaña.
---
Autores
Proyecto desarrollado como parte de la asignatura de Arquitectura de Software.

Jose Rivera - Modulo de Agente y Cliente - Lógica de Negocio, Validaciones y Migración a MySQL Server.
Carlos Molano - Infraestructura  -  Modulo de LLamada y Campaña - Configuración SQLite y Adaptadores.

Made with ❤️ and Java Swing.
