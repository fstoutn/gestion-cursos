# Sistema de Gestion de Cursos

API REST construida con Java 17, Spring Boot 3, Spring Data JPA y H2 en memoria.

## Requisitos

- JDK 17 o superior
- Maven 3.8 o superior

## Ejecutar

Desde esta carpeta:

```powershell
mvn spring-boot:run
```

La API queda disponible en `http://localhost:8080`. La consola H2 está en `http://localhost:8080/h2-console`, con JDBC URL `jdbc:h2:mem:cursoapp`, usuario `sa` y contraseña vacía.

## Endpoints

| Metodo | Ruta | Funcion |
| --- | --- | --- |
| GET | `/profesores` | Listar profesores |
| POST | `/profesores` | Crear profesor |
| GET | `/estudiantes` | Listar estudiantes |
| POST | `/estudiantes` | Crear estudiante |
| GET | `/cursos` | Listar cursos con profesor y estudiantes |
| POST | `/cursos` | Crear curso con profesor existente |
| POST | `/cursos/{cursoId}/agregar-estudiante/{estudianteId}` | Asignar estudiante a curso |

Ejemplo de profesor:

```json
{
  "nombre": "Juan Perez",
  "email": "juan@mail.com"
}
```

Ejemplo de estudiante:

```json
{
  "nombre": "Ana",
  "matricula": "A123"
}
```

Ejemplo de curso (el profesor debe existir):

```json
{
  "nombre": "Programacion Java",
  "profesor": { "id": 1 },
  "estudiantes": []
}
```

## Pruebas

```powershell
mvn test
```