# App JSF

Autor: **Alejando Caicedo**

Aplicación de ejemplo que combina Spring Boot 2.7 y JSF 2.3 para gestionar un carrito de compras sencillo.

## Tecnologías principales

- **Spring Boot 2.7.18**: arranque rápido del servicio web y soporte para empaquetado ejecutable.
- **JoinFaces 4.x + JSF 2.3 (Mojarra)**: motor de componentes front-end con Facelets, compatible con el espacio de nombres `javax.*`.
- **CDI 2.0 / Jakarta EE 8**: `@Named`, `@SessionScoped` y `@Inject` para la capa de beans JSF.
- **Lombok**: generación automática de getters, setters y constructores para los modelos.
- **Maven**: gestión de dependencias y ciclo de build.
- **Docker + Docker Compose**: empaquetado y despliegue containerizado.

## Estructura funcional

- Página principal (`index.html`): dos accesos rápidos para añadir productos o gestionar el carrito.
- `add.xhtml`: formulario con campos id/nombre; cada envío agrega o incrementa el producto en sesión.
- `cart.xhtml`: vista con las cantidades actuales, botones `+`, `-` y edición inline del nombre.
- `StoreBean` (`@SessionScoped`): conserva el carrito en memoria durante la sesión y expone las acciones JSF.
- `StoreService`: capa in-memory que persiste productos por id dentro del ciclo de vida de la aplicación.

## Requisitos previos

- JDK 17
- Maven 3.9+
- Docker (para despliegue containerizado)

## Ejecución local (sin Docker)

```powershell
# Compilar
./mvnw.cmd -DskipTests package

# Ejecutar
./mvnw.cmd spring-boot:run
```

La aplicación quedará disponible en `http://localhost:8080/index.html`.

## Despliegue con Docker

1. **Construir imagen**

```powershell
docker compose build
```

2. **Levantar contenedor**

```powershell
docker compose up
```

3. **Probar**

Visita `http://localhost:8080/index.html` para acceder a la portada; desde ahí navega a `add.xhtml` o `cart.xhtml`.

4. **Detener el servicio**

```powershell
docker compose down
```

## Notas

- El almacenamiento es en memoria; reiniciar la app limpia el carrito.
- Las rutas JSF (`.xhtml`) están bajo `META-INF/resources`, por lo que pueden consumirse directamente sin configuraciones adicionales.
