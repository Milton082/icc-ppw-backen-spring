# Fundamentos Spring Boot

## Autor

Domenica Uyunkar

## Descripción

Proyecto básico desarrollado con Spring Boot para verificar la instalación y funcionamiento del framework.

## Requisitos

* Java 21
* Gradle

## Ejecución

```bash
.\gradlew.bat bootRun
```

## Endpoint disponible

### Estado del servicio

```http
GET /api/status
```

Ejemplo:

```json
{   "status":"running",
    "service":"Spring Boot API",
    "timestamp":"2026-06-12T10:51:45.882028"
}
```

## URL de prueba

```text
http://localhost:8080/api/status
```
# Fundamentos Spring Boot

## Evidencia 1: Versión de Java

![Java Version](img/java-version.png)

## Evidencia 2: Inicio de Tomcat

![Tomcat](img/tomcat-started.png)

## Evidencia 3: Endpoint funcionando

![API Status](img/api-status.png)

## Evidencia 4: Controlador creado

![Status Controller](img/status-controller.png)