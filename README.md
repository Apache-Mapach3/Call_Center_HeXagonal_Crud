# Sistema de Gestión de Call Center (Desktop)

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![SQLite](https://img.shields.io/badge/SQLite-07405E?style=for-the-badge&logo=sqlite&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Architecture](https://img.shields.io/badge/Architecture-Hexagonal-blue?style=for-the-badge)

Aplicación de escritorio robusta y escalable para la gestión integral de un Call Center. Desarrollada bajo los estrictos lineamientos de la **Arquitectura Hexagonal - Puertos y Adaptadores** para garantizar el desacoplamiento, la mantenibilidad y la independencia tecnológica.

---

## Características Principales

### Ingeniería de Software
* **Arquitectura Hexagonal Pura:** Separación estricta entre el Núcleo del Dominio, la Capa de Aplicación y la Infraestructura.
* **Inyección de Dependencias (DI):** Gestión manual de dependencias en el `Main` (Composition Root) para facilitar el testing y la modularidad.
* **Base de Datos Embebida (SQLite):** Sistema **"Plug & Play"**. No requiere instalar servidores MySQL ni configurar puertos. La base de datos es un archivo local portátil.
* **Auto-Migración:** El sistema detecta si la base de datos no existe y crea las tablas automáticamente al iniciar.

###  Funcionalidades de Negocio
* **Gestión de Agentes:** Registro, actualización y control de personal con validaciones de unicidad.
* **Gestión de Clientes:** Base de datos de clientes con validación de documentos de identidad.
* **Campañas de Marketing:** Administración de campañas activas con control de fechas y objetivos.
* **Registro Transaccional de Llamadas:**
    * Validación de **Integridad Referencial en Capa de Servicio**: Verifica la existencia real de Agente, Cliente y Campaña antes de guardar.
    * Historial completo con consultas optimizadas (JOINs) para reportes.

---

##  Arquitectura Técnica

El proyecto sigue la estructura de **Puertos y Adaptadores**:

```text
com.udc.callcenterdesktop
├── dominio (NÚCLEO - Java Puro)
│   ├── modelo       # Entidades ricas (Agente, Cliente, Llamada)
│   ├── puertos      # Interfaces (Contratos para Repositorios y Servicios)
│   └── excepciones  # Excepciones de negocio (CallCenterException)
│
├── aplicacion (ORQUESTACIÓN)
│   ├── servicios    # Lógica de negocio y Casos de Uso
│   ├── dto          # Data Transfer Objects (Objetos planos)
│   └── mapper       # Conversores DTO <-> Entidad
│
└── infraestructura (ADAPTADORES - Detalles Técnicos)
    ├── entrada      # GUI (Java Swing - JFrames)
    └── salida       # Persistencia (SQLite JDBC Implementation)

Configuración
El proyecto utiliza configuración externalizada para mayor seguridad.

Archivo: src/main/resources/config.properties

Properties

# Configuración SQLite
db.driver=org.sqlite.JDBC
db.url=jdbc:sqlite:callcenter_database.db
# No requiere usuario/pass por defecto en modo archivo local
db.user=
db.password=
Requisitos e Instalación
Prerrequisitos
Java JDK: Versión 11 o superior (Recomendado JDK 17 o 21).

Maven: Para la gestión de dependencias y construcción.

NetBeans

Ejecución (Plug & Play)
Dado que usa SQLite, no necesitas instalar ningún motor de base de datos.

Clonar el repositorio:

Bash

git clone [https://github.com/tu-usuario/CallCenterHexagonal.git](https://github.com/tu-usuario/CallCenterHexagonal.git)
Construir el proyecto:

Bash

mvn clean install
Ejecutar:

Desde el IDE: Ejecuta la clase com.udc.callcenterdesktop.Main.

El sistema creará automáticamente el archivo callcenter_database.db en la raíz del proyecto.

Seguridad y Buenas Prácticas
Try-with-resources: Gestión automática del cierre de conexiones JDBC para evitar fugas de memoria y bloqueos de archivo.

PreparedStatements: Protección total contra Inyección SQL.

Validaciones Defensivas:

Los Servicios validan DTOs antes de procesarlos.

Los Repositorios validan Entidades antes de persistirlas.

Encapsulamiento: Todos los atributos de DTOs y Entidades son privados con acceso controlado.

Autores
Proyecto desarrollado como parte de la asignatura de Arquitectura de Software.

Jose Rivera - Lógica de Negocio, Validaciones y Migración de Datos.
Carlos Molano - Infraestructura, Configuración SQLite y Adaptadores.

Made with ❤️ and Java Swing.
