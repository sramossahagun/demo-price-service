# Price Service

## Descripción

Aplicación desarrollada utilizando **Java 25** y **Spring Boot 3.5.16**.

El servicio permite obtener el precio aplicable a un producto para una fecha determinada, teniendo en cuenta la cadena, el producto y la prioridad de las distintas tarifas disponibles.

---

## Stack técnologico

- Java 25
- Spring Boot 3.5.16
- Maven
- Spring Data JPA
- Hibernate
- H2 Database
- OpenAPI 3.1
- OpenAPI Generator
- Springdoc OpenAPI (Swagger UI)
- MapStruct
- Lombok
- JUnit 5
- Mockito
- AssertJ

---

## Consideraciones del diseño de la solución

- No se indican requerimientos de stack tecnologico (JDK, springboot, etc...) por lo que se desarrolla la solucion en el stack estandar mas consolidado (basado en Springboot 3.x)

- Para el desarrollo se aplica arquitectura hexagonal en proyecto maven. La estructura de carpetas, como en todos los modelos, es opinable, en esta implementacion, se refleja el concepto de puertos y adaptadadores in/out por ser una representacion clasica y sencilla de seguir.

- El formato indicado para fechas (2020-06-14-00.00.00) no indica zona horaria por lo que se asume que es hora local. Bajo este escenario la opcion seleccionada (LocalDateTime + timestamp) es la opcion mas simple, ya que no requiere transformaciones especiales y permite el gobierno de las horas sin conocer zona horaria. En caso de gestionar zona horaria, recomendaria el uso de OffsetDatetime + Instant

- El enunciado no es claro en cuanto a si se debe devolver un único resultado o una colección de resultados si existen varias tarifas aplicables. Por ello, se implementa la solucion que considero mas adecuada, devolver un unico resultado produciendo una excepcion en caso de obtener multiples valores.

- Se establece la configuracion de la aplicacion por entornos mediante profiles de spring, por lo que se requiere el uso de profile "local" para arrancar la aplicacion

- Se implementa una gestión centralizada básica de excepciones, centrada principalmente en el tratamiento de las excepciones de negocio. Dado el carácter del ejercicio, no se realiza el diseño de un sistema completo de gestión de errores, aunque sí se establecen las bases para su evolución. Como ejemplo de esta aproximación, se muestra cómo estandarizar el tratamiento de determinadas excepciones de la capa web (parámetros obligatorios y parámetros con un formato no válido).

- No se realiza gestion de logging avanzado, uso de MDC, ni otros mecanismos de trazado de peticiones, ntegracion con sistemas de monitorizacion y metricas, seguridad, y otros aspectos mas avanzados, ya que se considera que el caso practico forma parte de una solucion completa que gestiona de forma transversal estas cuestiones

- De cara a la ejecucion de test y la obtencion de cobertura, no se han tenido en cuenta clases de tipo entidad y similares (Dtos, clases de dominio, entidades jpa, etc). Dado el caracter del ejercicio, se incluyen en este caso tambien las excepciones, ya que se asume que formaran parte de un sistema completo donde se valida estos elementos transversales de forma completa (en este contexto no aportan valor). Del mismo modo queda excluida la clase de arranque de la aplicacion.

---

## Base de datos

La aplicación utiliza una base de datos **H2 en memoria**.

Su inicialización se realiza automáticamente mediante los siguientes scripts (haciendo uso del profile "local"):

```
src/main/resources/db/local/schema.sql
src/main/resources/db/local/data.sql
```

---

## Generar recursos

```
mvn generate-sources
```

---


## Compilación

```bash
mvn clean compile
```

---

## Contruccion

```bash
mvn clean install
```

---


## Ejecución

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

---

## Ejecución de pruebas

```bash
mvn clean verify
```

Informe de cobertura dispobible en la ruta:


```bash
/target/site/jacoco/index.html
```
---


## Swagger UI

Una vez iniciada la aplicación, la documentación del API estará disponible en:


```bash
http://localhost:8080/swagger-ui/index.html
```

