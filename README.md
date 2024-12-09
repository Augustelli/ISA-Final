# Proyecto ISA Final

Este proyecto es una aplicación web desarrollada con Ionic y Angular, diseñada para ser desplegada utilizando Docker y Nginx. La aplicación está estructurada en dos partes principales: el frontend y el backend.

## Estructura del Proyecto

- **backend/**: Contiene todos los archivos relacionados con el backend del proyecto.
  - `.devcontainer/`: Configuración del contenedor de desarrollo.
  - `.jhipster/`: Configuración específica de JHipster.
  - `.mvn/`: Archivos relacionados con Maven.
  - `src/`: Código fuente del backend.
  - `Dockerfile`: Archivo Docker para construir la imagen del backend.
  - `pom.xml`: Archivo de configuración de Maven.
  - Otros archivos de configuración y scripts.

- **frontend/**: Contiene todos los archivos relacionados con el frontend del proyecto.
  - `isaFinal/`: Código fuente del frontend.
    - `src/global.scss`: Archivo de estilos globales para la aplicación.
    - `Dockerfile`: Archivo Docker para construir la imagen del frontend.

- **conf.d/**, **filebeat/**, **jenkins/**, **k6/**, **kibana/**, **logstash/**: Directorios de configuración y scripts adicionales.

- `docker-compose.yml`: Archivo de configuración de Docker Compose para orquestar los contenedores.

## Cómo Construir y Ejecutar el Proyecto

### Requisitos Previos

- Docker
- Docker Compose

### Instrucciones

1. Clona el repositorio:

    ```sh
    git clone <URL_DEL_REPOSITORIO>
    cd <NOMBRE_DEL_REPOSITORIO>
    ```

2. Construye y ejecuta los contenedores utilizando Docker Compose:

    ```sh
    docker-compose up --build
    ```

3. La aplicación estará disponible en `http://localhost`.

## Descripción del Dockerfile del Frontend

El archivo `frontend/isaFinal/Dockerfile` está diseñado para construir y desplegar la aplicación frontend utilizando Node.js y Nginx. A continuación se describe cada paso del Dockerfile:

1. **Construcción de la Aplicación**:
    - Utiliza la imagen base `node:22-alpine`.
    - Establece el directorio de trabajo en `/app`.
    - Copia los archivos `package*.json` al contenedor.
    - Instala las dependencias utilizando `npm install`.
    - Instala el CLI de Ionic globalmente.
    - Copia el resto de los archivos del proyecto al contenedor.
    - Construye la aplicación utilizando `ionic build --prod`.

2. **Despliegue con Nginx**:
    - Utiliza la imagen base `nginx:alpine`.
    - Copia los archivos construidos al directorio de Nginx.
    - Copia el archivo de configuración de Nginx al contenedor.
