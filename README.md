# Registro de estudios médicos

Aplicación para cargar, consultar y actualizar estudios médicos.

Cada registro guarda la fecha de ingreso al sistema, el nombre y apellido,
el DNI, el tipo de estudio y el médico interviniente. También permite
indicar si el estudio se realizó, en qué fecha y si ya fue informado.

La fecha de ingreso corresponde al día en que se registra a la persona
para realizarle el estudio. La fecha de realización se completa cuando
se marca el estudio como realizado.

Para la demostración se usarán datos ficticios.



\## Despliegue en otra computadora



\### Requisitos



\- Docker con el motor iniciado y Docker Compose disponible.

\- Conexión a Internet para descargar las imágenes y dependencias

&#x20; durante la primera compilación.



\### Configuración y arranque



1\. Obtené una copia del repositorio y abrí PowerShell en la carpeta

&#x20;  donde están `Dockerfile` y `compose.yaml`.

2\. En una copia nueva del proyecto, creá el archivo `.env`:



&#x20;  ```powershell

&#x20;  Copy-Item .env.example .env

&#x20;  ```



3\. Abrí `.env` y reemplazá los valores de `MYSQL\_PASSWORD` y

&#x20;  `MYSQL\_ROOT\_PASSWORD` por dos claves de prueba distintas.

4\. Construí e iniciá los servicios:



&#x20;  ```powershell

&#x20;  docker compose up --build -d

&#x20;  ```



5\. Esperá a que Tomcat y MySQL terminen de iniciar. Abrí

&#x20;  http://127.0.0.1:8080/ para cargar un estudio ficticio y

&#x20;  http://127.0.0.1:8080/estudios para comprobar que aparece en el listado.



\### Detener la aplicación



```powershell

docker compose down

```



Este comando detiene los servicios. Los registros quedan en el volumen

`mysql\_datos`.

