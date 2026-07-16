# FUNDAMENTOS SPRING BOOT

### **Realizado por:** Milton Chuqui

---

### **Práctica 1. Configuración del proyecto**

_1. Captura de verificación de Java._

![Captura de verificación de Java](./assets/01-javaVersion-01.png)

_2. Captura del servidor Srping Boot ejecutandose._

![Captura del servidor Spring Boot ejecutandose](./assets/02-springBootServer-01.png)

_3. Captura del endpoint /api/status_

![Captura del endpoint /api/status](./assets/03-endpointStatus-01.png)

_4. Captura del comando_

![Captura del comando](./assets/04-comando-01.png)

_5. Explicación Breve_

**¿Qué entendió sobre el funcionamiento del endpoint
y sobre la función general de Spring Boot en la
creación del servidor?**

- El endpoint /api/status es un punto de acceso que permite verificar el estado del servidor Spring Boot. Cuando se realiza una solicitud a este endpoint, el servidor responde con información sobre su estado actual, lo que indica que está funcionando correctamente.
- Spring Boot es un framework que facilita la creación de aplicaciones Java, proporcionando una configuración automática y simplificando el proceso de desarrollo. Permite a los desarrolladores crear servidores web y servicios REST de manera rápida y eficiente, manejando gran parte de la configuración y las dependencias necesarias para ejecutar la aplicación.

---

## **Práctica 2. Estructura del Proyecto, Arquitectura Interna y Organización Modular**

_1. Captura del IDE mostrando la estructura modular_

![Captura del IDE mostrando la estructura modular](./assets/05-estructuraModular-02.png)

_2. Captura del archivo Fundamentos01Application.java_

![Captura del archivo Fundamentos01Application.java](./assets/06-Fundamentos01Application-02.png)

_3. Explicación breve_

**¿Por qué es importante tener módulos separados?**

- Tener módulos separados es importante porque permite una mejor organización del código, facilita el mantenimiento y la escalabilidad del proyecto. Cada módulo puede enfocarse en una funcionalidad específica, lo que reduce la complejidad y mejora la legibilidad del código. Además, los módulos separados permiten la reutilización de componentes y facilitan las pruebas unitarias, ya que cada módulo puede ser probado de manera independiente.

---

## **Práctica 3. Construcción de una API REST usando controladores, DTOs, modelos y mappers**

En esta práctica se replico toda la estructura de /api/users y se creo la estructura de /api/products, con sus respectivos controladores, DTOs, modelos y mappers.

Los campos del producto son los siguientes:

```java
private Long id;
private String name;
private Double price;
private Integer stock;
private LocalDateTime createdAt;
```

**1. Implementar los 6 endpoints REST para productos**

Con funcionamiento idéntico al de usuarios.

- Endpoints disponibles

| Método | Ruta                | Descripción                 |
| ------ | ------------------- | --------------------------- |
| GET    | `/api/products`     | Lista productos             |
| GET    | `/api/products/:id` | Obtiene producto            |
| POST   | `/api/products`     | Crea producto               |
| PUT    | `/api/products/:id` | Reemplaza producto completo |
| PATCH  | `/api/products/:id` | Actualiza parcialmente      |
| DELETE | `/api/products/:id` | Elimina producto            |

**2. Evidencias**

_1. Captura de consumo de endpoints de Products desde Bruno._

```http
GET /api/products         / Con 3 preductos creados
```

![Captura de consumo de endpoints de Products desde Bruno](./assets/07-consumoEndpointsProducts-03.png)

```http
GET /api/products/:id          / Con un producto existente
```

![Captura de consumo de endpoints de Products desde Bruno](./assets/08-endpointsProductsExis-03.png)

```http
DELETE /api/products/:id       / Eliminando un producto existente
```

![Captura de consumo de endpoints de Products desde Bruno](./assets/09-eliminarProducNoExis-03.png)

```http
DELETE /api/products/:id       / Eliminando un producto que no existe
```

![Captura de consumo de endpoints de Products desde Bruno](./assets/10-eliminarProducExis-03.png)

## **Práctica 4. Controladores + Servicios + Lógica de Negocio**

En esta práctica se replicó toda la estructura implementada en `users/` para el recurso de `products/`.

### **1. Código completo de `ProductServiceImpl.java`**

```java
@Service
public class ProductServiceImpl implements ProductService {

    private final List<ProductModel> products = new ArrayList<>();
    private Long currentId = 1L;

    @Override
    public List<ProductResponseDto> findAll() {
        return products.stream()
                .map(ProductMapper::toResponse)
                .toList();
    }

    @Override
    public Object findOne(Long id) {
        return products.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .map(product -> (Object) ProductMapper.toResponse(product))
                .orElseGet(() ->
                        new ErrorResponseDto("Product not found"));
    }

    @Override
    public ProductResponseDto create(CreateProductDto dto) {

        ProductModel product = ProductMapper.toModel(dto);

        product.setId(currentId);
        currentId++;

        products.add(product);

        return ProductMapper.toResponse(product);
    }

    @Override
    public Object update(Long id, UpdateProductDto dto) {

        ProductModel product = products.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (product == null) {
            return new ErrorResponseDto("Product not found");
        }

        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());

        return ProductMapper.toResponse(product);
    }

    @Override
    public Object partialUpdate(
            Long id,
            PartialUpdateProductDto dto) {

        ProductModel product = products.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (product == null) {
            return new ErrorResponseDto("Product not found");
        }

        if (dto.getName() != null) {
            product.setName(dto.getName());
        }

        if (dto.getPrice() != null) {
            product.setPrice(dto.getPrice());
        }

        if (dto.getStock() != null) {
            product.setStock(dto.getStock());
        }

        return ProductMapper.toResponse(product);
    }

    @Override
    public Object delete(Long id) {

        boolean removed = products.removeIf(
                product -> product.getId().equals(id));

        if (!removed) {
            return new ErrorResponseDto("Product not found");
        }

        return new Object() {
            public String message = "Deleted successfully";
        };
    }
}
```

### **Elementos implementados**

- **Uso de `@Service`:** permite que Spring reconozca la clase como un servicio y gestione su instancia.

- **Lista en memoria:** los productos se almacenan temporalmente en un `ArrayList`.

- **Generación de ID:** la variable `currentId` asigna un identificador único a cada producto creado.

- **Uso del mapper:** `ProductMapper` convierte los DTOs en modelos y los modelos en DTOs de respuesta.

- **Métodos CRUD implementados:** se incluyeron los métodos para listar, buscar, crear, actualizar, actualizar parcialmente y eliminar productos.

### **2. Código completo de `ProductsController.java`**

```java
@RestController
@RequestMapping("/products")
public class ProductsController {

    private final ProductService service;

    public ProductsController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProductResponseDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Object findOne(@PathVariable Long id) {
        return service.findOne(id);
    }

    @PostMapping
    public ProductResponseDto create(
            @RequestBody CreateProductDto dto) {

        return service.create(dto);
    }

    @PutMapping("/{id}")
    public Object update(
            @PathVariable Long id,
            @RequestBody UpdateProductDto dto) {

        return service.update(id, dto);
    }

    @PatchMapping("/{id}")
    public Object partialUpdate(
            @PathVariable Long id,
            @RequestBody PartialUpdateProductDto dto) {

        return service.partialUpdate(id, dto);
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable Long id) {
        return service.delete(id);
    }
}
```

### **Elementos implementados**

- **Inyección de `ProductService`:** el servicio se recibe mediante el constructor y se almacena en la variable `service`.

- **Endpoints llamando al servicio:** cada endpoint delega su operación al método correspondiente de `ProductService`.

- **Ausencia de lógica CRUD en el controlador:** el controlador únicamente recibe las solicitudes y llama al servicio. La búsqueda, creación, actualización y eliminación de productos se realizan en `ProductServiceImpl`.

### **3. Explicación breve: inyección del servicio**

El servicio se inyecta mediante el constructor de `ProductsController`.

```java
private final ProductService service;

public ProductsController(ProductService service) {
    this.service = service;
}
```

Spring detecta la implementación de `ProductService` anotada con `@Service`, crea su instancia y la entrega automáticamente al controlador.

## **Práctica 5: Persistencia real con PostgreSQL, Entidades JPA y Repositorios**

- Captura de 5 productos creados en PostgreSQL

![Captura de 5 productos creados en PostgreSQL](./assets/11-productos-05.png)
