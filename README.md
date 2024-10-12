# Actualizador de Precios del Tabulador

Este proyecto es una aplicación de Spring Boot que actualiza los precios del tabulador basado en un archivo de Excel. Permite cargar un archivo Excel con los datos de actualización, y procesa los cambios en la base de datos conectada.

## Requisitos

- Java 17 o superior
- Maven 3.8.1 o superior
- MySQL 8.0 o superior (o cualquier base de datos compatible)
- Archivo Excel en el formato especificado

## Configuración

### 1. Variables de entorno

Es necesario modificar el archivo `application.properties` con los valores correspondientes a tu conexión de base de datos y la ruta del archivo Excel. Las variables a definir son las siguientes:

- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`
- `file.excel`

### 2. Formato del archivo Excel

El archivo de Excel debe seguir el siguiente formato para ser procesado correctamente:

- **Columna A**: idconcepto
- **Columna B**: idConceptoPrecio
- **Columna C**: Área
- **Columna D**: Estudio (nombre)
- **Columna E**: Precio

**Importante**: El archivo Excel no debe incluir las cabeceras de los nombres de las columnas, ya que esto podría causar errores durante el procesamiento.

Asegúrate de que los datos estén organizados correctamente para evitar errores en el procesamiento.

### 3. Instrucciones para correr el proyecto

Para ejecutar el proyecto con Maven, sigue los pasos a continuación:

1. Clona el repositorio:

   git clone https://github.com/alan1606/tabulador.git  
   cd tabulador

2. Asegúrate de que el archivo `application.properties` esté correctamente configurado con tu base de datos y la ruta al archivo Excel.

3. Construye el proyecto:

   mvn clean install

4. Ejecuta la aplicación:

   mvn spring-boot:run

El proyecto se ejecutará en `http://localhost:8080` por defecto.
