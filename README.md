# Lion Warrior API

API REST del proyecto **Lion Warrior Company** (barbería y distribuidora). Expone 
los servicios web que necesita el software: autenticación, usuarios, catálogo de servicios, 
catálogo de productos (DistriLion), barberos y citas.

- **Evidencia: GA7-220501096-AA5-EV03** · Diseño y desarrollo de servicios web - 
proyecto

- **Aprendiz:** Damian Andres Mejia Novoa · Ficha 3235899
- **Programa:** Tecnólogo en Análisis y Desarrollo de Software · SENA
- **Repositorio:** https://github.com/dmejianovoa/lion-warrior-api

---

## Tabla de contenido

1. [Tecnologías y requisitos](#1-tecnologías-y-requisitos)
2. [Configuración y ejecución](#2-configuración-y-ejecución)
3. [Convenciones generales de la API](#3-convenciones-generales-de-la-api)
4. [Resumen de endpoints](#4-resumen-de-endpoints)
5. [Documentación detallada de los servicios](#5-documentación-detallada-de-los-servicios)
6. [Estructura del proyecto](#6-estructura-del-proyecto)
7. [Estándares de codificación](#7-estándares-de-codificación)
8. [Versionamiento](#8-versionamiento)
9. [Seguridad](#9-seguridad)
10. [Alcance y trabajo futuro](#10-alcance-y-trabajo-futuro)

---

## 1. Tecnologías y requisitos

| Tecnología | Uso |
|---|---|
| Java 25 | Lenguaje |
| Spring Boot 4.1.1 | Framework web (Spring Web MVC) |
| Spring Data JPA / Hibernate | Acceso a datos |
| MySQL 8 | Base de datos `lion_warriordb` |
| Bean Validation | Validación de los datos de entrada |
| spring-security-crypto | Cifrado de contraseñas con BCrypt |
| Maven | Gestión de dependencias y construcción |
| Postman | Pruebas de los servicios |
| Git / GitHub | Versionamiento |

**Requisitos para ejecutarlo:**
JDK 25, MySQL 8 y Postman. Maven no hace falta instalarlo 
porque el proyecto incluye el wrapper (`mvnw`).

---

## 2. Configuración y ejecución

```bash
git clone https://github.com/dmejianovoa/lion-warrior-api.git
```

**2. Crear la base de datos.** Ejecutar en MySQL Workbench el script
`database/lion_warriordb`. Crea el schema `lion_warriordb` 
con las 6 tablas que usa la API 
(`users`, `barbers`, `services`, `products`, `appointments`, `appointment_services`).

**3. Configurar la conexión.** En `src/main/resources/`, copiar `application.properties.example`
como `application.properties` y escribir la contraseña de MySQL propia. El archivo
`application.properties` está en el `.gitignore` para no subir credenciales al repositorio.

**4. Ejecutar.** Desde la carpeta donde está el `pom.xml`:

```bash
# Windows
mvnw.cmd spring-boot:run
 
# Linux / macOS
./mvnw spring-boot:run
```
La API queda disponible en **http://localhost:8080**. También se puede ejecutar desde IntelliJ IDEA corriendo la clase `LionwarriorApplication`.

> La base de datos se crea vacía. Para probar, primero se registran usuarios con `POST /api/auth/register` y se crean servicios y productos con sus `POST`.
 
---

## 3. Convenciones generales de la API

- **URL base:** `http://localhost:8080`
- **Formato:** todas las peticiones y respuestas son JSON (`Content-Type: application/json`).
- **Fechas y horas:** fecha `AAAA-MM-DD` (ej. `2026-12-05`), hora `HH:mm` (ej. `10:30`).
- **Dinero:** valores decimales con dos decimales (ej. `25000.00`).
- **Errores:** todas las respuestas de error tienen el mismo formato:

```json
{ "mensaje": "descripción del error" }
```

Los errores de validación (400) empiezan con `Bad Request:` seguido del detalle.

### Códigos de respuesta usados

| Código | Significado | Cuándo se devuelve |
|---|---|---|
| 200 OK | Operación exitosa | Consultas, actualizaciones y login |
| 201 Created | Recurso creado | Registro y creación de recursos |
| 204 No Content | Eliminado | Eliminación exitosa (sin cuerpo) |
| 400 Bad Request | Datos inválidos | Falla una validación o una regla de negocio |
| 401 Unauthorized | Credenciales incorrectas | Login fallido |
| 404 Not Found | No existe | El id consultado no existe |
| 409 Conflict | Conflicto | Duplicados, estado no permitido o registro con dependencias |
 
---

## 4. Resumen de endpoints

| Módulo | Método | Ruta | Descripción | Respuestas |
|---|---|---|---|---|
| Auth | POST | `/api/auth/register` | Registrar usuario (rol `client`) | 201, 400, 409 |
| Auth | POST | `/api/auth/login` | Iniciar sesión con email y contraseña | 200, 400, 401 |
| Users | GET | `/api/users` | Listar usuarios | 200 |
| Users | GET | `/api/users/{id}` | Consultar un usuario | 200, 404 |
| Users | PUT | `/api/users/{id}` | Actualizar un usuario | 200, 400, 404, 409 |
| Users | DELETE | `/api/users/{id}` | Eliminar un usuario | 204, 404, 409 |
| Services | GET | `/api/services` | Listar servicios | 200 |
| Services | GET | `/api/services/{id}` | Consultar un servicio | 200, 404 |
| Services | POST | `/api/services` | Crear un servicio | 201, 400, 409 |
| Services | PUT | `/api/services/{id}` | Actualizar un servicio | 200, 400, 404, 409 |
| Services | DELETE | `/api/services/{id}` | Eliminar un servicio | 204, 404, 409 |
| Products | GET | `/api/products` | Listar productos | 200 |
| Products | GET | `/api/products/{id}` | Consultar un producto | 200, 404 |
| Products | POST | `/api/products` | Crear un producto | 201, 400, 409 |
| Products | PUT | `/api/products/{id}` | Actualizar un producto | 200, 400, 404, 409 |
| Products | DELETE | `/api/products/{id}` | Eliminar un producto | 204, 404, 409 |
| Barbers | GET | `/api/barbers` | Listar barberos (`?active=true/false`) | 200 |
| Barbers | GET | `/api/barbers/{id}` | Consultar un barbero | 200, 404 |
| Barbers | POST | `/api/barbers` | Crear perfil de barbero | 201, 400, 404, 409 |
| Barbers | PUT | `/api/barbers/{id}` | Actualizar o desactivar un barbero | 200, 400, 404 |
| Appointments | GET | `/api/appointments` | Listar citas (`?idUser=` o `?idBarber=`) | 200 |
| Appointments | GET | `/api/appointments/{id}` | Consultar una cita | 200, 404 |
| Appointments | POST | `/api/appointments` | Agendar una cita | 201, 400, 404, 409 |
| Appointments | PATCH | `/api/appointments/{id}/status` | Cambiar el estado de una cita | 200, 400, 404, 409 |
 
---

## 5. Documentación detallada de los servicios

### 5.1 Auth — `/api/auth`

#### POST `/api/auth/register`

Registra un usuario nuevo. El rol siempre queda como `client`, sin importar lo que se envíe. La contraseña se guarda cifrada con BCrypt.

**Body**

| Campo | Tipo | Obligatorio | Validación |
|---|---|---|---|
| `userName` | texto | Sí | No vacío |
| `lastName` | texto | No | — |
| `email` | texto | Sí | Formato de correo válido y único |
| `phoneNumber` | texto | No | Formato `000-000-0000` |
| `password` | texto | Sí | 8 a 72 caracteres, con letras y números |

```json
{
  "userName": "Carlos",
  "lastName": "Ruiz",
  "email": "carlos@correo.com",
  "phoneNumber": "300-123-4567",
  "password": "Clave1234"
}
```

| Código | Cuándo                      | Respuesta                                                    |
|---|-----------------------------|--------------------------------------------------------------|
| 201 | Registro exitoso            | `{"mensaje": "Usuario creado correctamente", "user": {...}}` |
| 400 | Falla una validación        | `{"mensaje": "Bad Request: ..."}`                            |
| 409 | El email ya está registrado | `{"mensaje": "..."}`                                         |

#### POST `/api/auth/login`

Inicia sesión con correo y contraseña. Si el correo no existe o la contraseña es incorrecta,
devuelve la misma respuesta para no revelar qué falló.

**Body**

```json
{ "email": "carlos@correo.com", "password": "Clave1234" }
```
| Código | Cuándo | Respuesta |
|---|---|---|
| 200 | Credenciales correctas | `{"mensaje": "Autenticación satisfactoria", "user": {...}}` |
| 400 | Faltan campos | `{"mensaje": "Bad Request: ..."}` |
| 401 | Credenciales incorrectas | `{"mensaje": "Error en la autenticación"}` |
 
---

### 5.2 Users — `/api/users`

Las respuestas de este módulo **nunca incluyen la contraseña** (`passwordHash`).

| Método | Ruta | Descripción | Respuestas |
|---|---|---|---|
| GET | `/api/users` | Devuelve la lista de usuarios | 200 |
| GET | `/api/users/{id}` | Devuelve un usuario por su id | 200, 404 |
| PUT | `/api/users/{id}` | Actualiza los datos del usuario. 
Si el correo nuevo ya lo usa otro usuario devuelve 409 | 200, 400, 404, 409 |
| DELETE | `/api/users/{id}` | Elimina el usuario. Devuelve 409 si tiene barbero 
o citas asociados | 204, 404, 409 |

**Ejemplo de respuesta (usuario)**

```json
{
  "idUser": 20,
  "userName": "Carlos",
  "lastName": "Ruiz",
  "email": "carlos@correo.com",
  "phoneNumber": "300-123-4567",
  "userRole": "client"
}
```

---

### 5.3 Services — `/api/services`

Catálogo de servicios de la barbería (cortes, barba, etc.).

**Body de POST y PUT**

| Campo | Tipo | Obligatorio | Regla |
|---|---|---|---|
| `serviceName` | texto | Sí | Único, sin distinguir mayúsculas (409 si ya existe) |
| `serviceDescription` | texto | No | Máx. 255 caracteres |
| `servicePrice` | decimal | Sí | Mayor que 0 |
| `durationMin` | entero | No | Duración en minutos |

```json
{
  "serviceName": "Corte vip",
  "serviceDescription": "Corte con tijera y máquina - mascarilla",
  "servicePrice": 45000.00,
  "durationMin": 45
}
```

| Método | Ruta | Respuestas |
|---|---|---|
| GET | `/api/services` | 200 |
| GET | `/api/services/{id}` | 200, 404 |
| POST | `/api/services` | 201, 400, 409 |
| PUT | `/api/services/{id}` | 200, 400, 404, 409 |
| DELETE | `/api/services/{id}` | 204, 404, 409 (el servicio ya está en alguna cita) |
 
---

### 5.4 Products — `/api/products`

Catálogo de productos de DistriLion.

**Body de POST y PUT**

| Campo | Tipo | Obligatorio | Regla |
|---|---|---|---|
| `productName` | texto | Sí | Único (409 si ya existe) |
| `productDescription` | texto | No | Máx. 100 caracteres |
| `priceRetail` | decimal | Sí | Precio al detal |
| `priceWholesale` | decimal | Sí | Precio mayorista. **No puede ser mayor que el precio al detal** (400) |
| `currentStock` | entero | Sí | Existencias actuales (columna `stock_actual`) |
| `minimumStock` | entero | Sí | Stock mínimo (columna `stock_minimo`) |

```json
{
  "productName": "Cera para peinar",
  "productDescription": "Fijación fuerte",
  "priceRetail": 30000.00,
  "priceWholesale": 24000.00,
  "currentStock": 40,
  "minimumStock": 10
}
```

| Método | Ruta | Respuestas |
|---|---|---|
| GET | `/api/products` | 200 |
| GET | `/api/products/{id}` | 200, 404 |
| POST | `/api/products` | 201, 400, 409 |
| PUT | `/api/products/{id}` | 200, 400, 404, 409 |
| DELETE | `/api/products/{id}` | 204, 404, 409 |
 
---

### 5.5 Barbers — `/api/barbers`

Un barbero es un usuario con perfil profesional. Al crear el perfil, el rol del usuario cambia a `barber`. 
**No existe `DELETE`**: un barbero con citas no puede borrarse, se **desactiva** con `"active": false`.

#### POST `/api/barbers`

| Campo | Tipo | Obligatorio | Regla |
|---|---|---|---|
| `idUser` | entero | Sí | El usuario debe existir (404) |
| `specialty` | texto | No | Máx. 100 caracteres |
| `availableSchedule` | texto | No | Máx. 100 caracteres |

```json
{
  "idUser": 5,
  "specialty": "Cortes clásicos y fade",
  "availableSchedule": "Lun-Sáb 9:00-18:00"
}
```

| Código | Cuándo | Mensaje |
|---|---|---|
| 201 | Perfil creado | Datos del barbero |
| 400 | Falta `idUser` | `Bad Request: El id del usuario es obligatorio` |
| 404 | El usuario no existe | `Usuario no encontrado con el id X` |
| 409 | Ya es barbero | `Ese usuario ya tiene perfil de barbero` |
| 409 | El usuario es administrador | `Un administrador no puede registrarse como barbero` |

#### PUT `/api/barbers/{id}`

Todos los campos son opcionales; solo se modifican los que se envían.

```json
{ "specialty": "Barba y perfilado", "availableSchedule": "Lun-Vie 10:00-19:00", "active": false }
```

| Código | Cuándo |
|---|---|
| 200 | Actualizado |
| 400 | Texto demasiado largo |
| 404 | `Barbero no encontrado con el id X` |

#### GET `/api/barbers` y GET `/api/barbers/{id}`

`GET /api/barbers` acepta el filtro opcional `?active=true` o `?active=false`. La respuesta **no incluye correo ni teléfono** del barbero.

```json
{
  "idBarber": 1,
  "idUser": 5,
  "userName": "Carlos",
  "lastName": "Ruiz",
  "active": true,
  "specialty": "Cortes clásicos y fade",
  "availableSchedule": "Lun-Sáb 9:00-18:00"
}
```
 
---

### 5.6 Appointments — `/api/appointments`

Una cita relaciona un cliente, un barbero, una fecha, una hora y uno o más servicios. Cada servicio guarda como `subtotal` el precio que tenía **al momento de agendar**; el `total` es la suma de los subtotales.

#### POST `/api/appointments`

| Campo | Tipo | Obligatorio | Regla |
|---|---|---|---|
| `idUser` | entero | Sí | El usuario debe existir |
| `idBarber` | entero | Sí | El barbero debe existir y estar activo |
| `appointmentDate` | fecha | Sí | `AAAA-MM-DD` |
| `appointmentTime` | hora | Sí | `HH:mm` |
| `serviceIds` | lista de enteros | Sí | Al menos uno; todos deben existir |

```json
{
  "idUser": 1,
  "idBarber": 1,
  "appointmentDate": "2026-12-05",
  "appointmentTime": "10:30",
  "serviceIds": [1, 2]
}
```

La cita se crea con estado `pending`.

| Código | Cuándo | Mensaje |
|---|---|---|
| 201 | Cita creada | Datos de la cita |
| 400 | Falta un campo, lista vacía o JSON inválido | `Bad Request: ...` |
| 400 | La fecha y hora ya pasaron | `Bad Request: La cita no puede tener fecha vencida` |
| 404 | El usuario no existe | `Usuario no encontrado con el id X` |
| 404 | El barbero no existe | `Barbero no encontrado con el id X` |
| 404 | Algún servicio no existe | `Uno o mas servicios no existen` |
| 409 | El barbero está desactivado | `El barbero no esta activo` |
| 409 | El barbero ya tiene cita a esa fecha y hora | `El barbero ya tiene una cita con esa fecha y hora` |

**Ejemplo de respuesta 201**

```json
{
  "idAppointment": 4,
  "idUser": 1,
  "userName": "Ana",
  "idBarber": 1,
  "barberName": "Carlos",
  "appointmentDate": "2026-12-05",
  "appointmentTime": "10:30:00",
  "status": "pending",
  "createdAt": "2026-10-28T20:30:00",
  "services": [
    { "idService": 1, "serviceName": "Corte clásico", "subtotal": 25000.00 },
    { "idService": 2, "serviceName": "Barba", "subtotal": 15000.00 }
  ],
  "total": 40000.00
}
```

Una cita **cancelada** libera la hora: el barbero puede volver a recibir otra cita en ese horario.

#### GET `/api/appointments`

Devuelve todas las citas. Filtros opcionales: `?idUser=1` (citas de un cliente) o `?idBarber=1` (citas de un barbero).

#### GET `/api/appointments/{id}`

200 con la cita, o 404 con `Cita no encontrada con el id X`.

#### PATCH `/api/appointments/{id}/status`

Cambia el estado de la cita. Solo se permiten las transiciones del siguiente diagrama:

```
pending ──► confirmed ──► in_progress ──► completed
   │             │
   └─────────────┴──────► cancelled
```

`completed` y `cancelled` son estados finales.

```json
{ "status": "confirmed" }
```

Valores válidos: `pending`, `confirmed`, `in_progress`, `completed`, `cancelled`.

| Código | Cuándo | Mensaje |
|---|---|---|
| 200 | Estado actualizado | Datos de la cita |
| 400 | Falta `status` o tiene un valor no válido | `Bad Request: ...` |
| 404 | La cita no existe | `Cita no encontrada con el id X` |
| 409 | Transición no permitida | `No se puede pasar una cita de X a Y` |
 
---

## 6. Estructura del proyecto

```
lion-warrior-api/
├── database/
│   └── lion_warriordb  # Script de la base de datos
├── src/main/java/co/edu/sena/lionwarrior/
│   ├── LionwarriorApplication.java        # Clase principal
│   ├── config/                            # BCrypt y CORS
│   ├── controller/                        # Endpoints REST
│   ├── dto/                               # Objetos de entrada y salida (records)
│   ├── exception/                         # Manejo global de errores
│   ├── model/                             # Entidades JPA y enums
│   ├── repository/                        # Acceso a datos (Spring Data)
│   └── service/                           # Lógica de negocio
├── src/main/resources/
│   ├── application.properties.example     # Plantilla de configuración
│   └── application.properties             # Configuración local (no se versiona)
├── pom.xml
└── README.md
```

**Arquitectura en capas:** `controller` recibe la petición HTTP →
`service` aplica las reglas de negocio → `repository` consulta MySQL. 
Los `dto` separan lo que entra y sale de la API de las entidades de la base de datos.
 
---

## 7. Estándares de codificación

| Elemento | Convención | Ejemplo |
|---|---|---|
| Paquetes | minúsculas | `co.edu.sena.lionwarrior.service` |
| Clases e interfaces | PascalCase | `AppointmentService`, `UserRepository` |
| Métodos y variables | camelCase | `findByEmail`, `serviceIds` |
| Constantes y valores de enum | según el ENUM de MySQL | `pending`, `in_progress` |
| Tablas y columnas | snake_case | `appointment_services`, `id_user` |
| DTO | sufijo `Request` / `Response` | `CreateBarberRequest`, `BarberResponse` |

- Cada clase y método importante lleva un comentario que explica su propósito.
- Los DTO son `record` con anotaciones de validación y mensajes claros.
- Las inyecciones de dependencias se hacen por constructor con campos `private final`.
- Los métodos de servicio que modifican datos son transaccionales (`@Transactional`).
---

## 8. Versionamiento

El proyecto se versionó con **Git** y se publicó en **GitHub**. Se hizo un commit por cada módulo, con mensajes siguiendo la convención `tipo: descripción`:

| Prefijo  | Uso                     |
|----------|-------------------------|
| `chore:` | Creación del proyecto   |
| `feat:`  | Funcionalidad nueva     |
| `fix:`   | Corrección de un error  |
| `docs:`  | Documentación o scripts |

Para ver el historial: `git log --oneline`.

El archivo `application.properties` (con la contraseña de la base de datos) está en el `.gitignore`; en el repositorio solo está `application.properties.example`.
 
---

## 9. Seguridad

- Las contraseñas se almacenan cifradas con **BCrypt**; nunca se guardan ni se devuelven en texto plano.
- Ninguna respuesta incluye `passwordHash`.
- El registro siempre asigna el rol `client`.
- En el login, un correo inexistente y una contraseña incorrecta devuelven la misma respuesta (401).
- CORS habilitado únicamente para `http://localhost:4200` (Angular) y `http://localhost:5173` (Vite/React).
---

## 10. Alcance y trabajo futuro

La API implementa **6 de las 13 tablas** del modelo de datos del proyecto, que cubren el flujo principal del negocio: registrarse, consultar el catálogo y agendar citas.

**Pendiente para próximas versiones:**

- Módulos de ventas, pagos, caja, multas, reservas de productos y encuestas.
- Autenticación con **JWT** y autorización por roles (`admin`, `barber`, `client`), para proteger endpoints como la creación de barberos y la gestión de usuarios.
- Ocultar el precio mayorista a los clientes que no sean distribuidores.
- Conectar el frontend Angular (registro, inicio de sesión, DistriLion y página de servicios) a esta API.