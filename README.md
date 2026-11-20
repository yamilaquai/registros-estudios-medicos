# Registro de estudios médicos

Aplicación para cargar, consultar y actualizar estudios médicos.

Cada registro guarda la fecha de ingreso al sistema, el nombre y apellido,
el DNI, el tipo de estudio y el médico interviniente. También permite
indicar si el estudio se realizó, en qué fecha y si ya fue informado.

La fecha de ingreso corresponde al día en que se registra a la persona
para realizarle el estudio. La fecha de realización se completa cuando
se marca el estudio como realizado.

Para la demostración se usan datos ficticios.

## Tecnologías

- Java 17 y Maven para construir la aplicación.
- Tomcat 10.1 para ejecutarla.
- MySQL 8.4 para guardar los estudios.
- Docker Compose para configurar e iniciar ambos servicios.

## Requisitos para desplegar

- Docker iniciado y Docker Compose disponible.
- Git instalado y acceso al repositorio.
- Conexión a Internet durante la primera construcción.
- Puerto 8080 disponible en la computadora.

## Descargar el proyecto

Abrir PowerShell y ejecutar:

```powershell
git clone https://github.com/yamilaquai/registros-estudios-medicos.git
Set-Location registros-estudios-medicos
```

Los siguientes comandos se ejecutan desde la carpeta del proyecto.

## Configurar una copia nueva

Crear el archivo de configuración local a partir de la plantilla:

```powershell
Copy-Item .env.example .env
```

Abrir `.env` y reemplazar los valores de `MYSQL_PASSWORD` y
`MYSQL_ROOT_PASSWORD` por dos contraseñas de prueba distintas.
Mantener el nombre de la base y el usuario definidos en la plantilla.

MySQL utiliza esas credenciales al inicializar una base nueva.
Si se reutiliza un volumen existente, se deben usar las credenciales
con las que se creó esa base.

El archivo `.env` contiene las credenciales locales y queda excluido
de Git. `.env.example` contiene valores ficticios y se comparte.

## Iniciar los servicios

Validar la configuración y construir el proyecto:

```powershell
docker compose config --quiet
docker compose up --build -d
```

La primera ejecución descarga las imágenes y dependencias.

MySQL ejecuta un chequeo de salud. La aplicación espera hasta que
la base responda antes de iniciar.

Consultar el estado:

```powershell
docker compose ps
```

El servicio `db` debe aparecer como `healthy` y `app` como `Up`.

## Usar la aplicación

Desde el navegador de la misma computadora:

- Cargar un estudio en http://127.0.0.1:8080/
- Consultar los registros en http://127.0.0.1:8080/estudios
- Indicar la fecha de realización y marcar el estudio como realizado.
- Marcarlo como informado cuando corresponda.

La aplicación se conecta a MySQL mediante el nombre de servicio `db`
y el puerto interno 3306. MySQL guarda sus datos en el volumen
`mysql_datos`. El puerto 8080 de la aplicación está publicado
en 127.0.0.1 para acceder desde la computadora donde se ejecuta.

## Detener y volver a iniciar

Detener y eliminar los contenedores:

```powershell
docker compose down
```

El volumen conserva los registros.

Volver a iniciar:

```powershell
docker compose up -d
```

## Incorporar cambios en el código

Después de modificar Java o JSP, reconstruir la aplicación:

```powershell
docker compose up --build -d app
```

## Crear un respaldo

Desde la carpeta del proyecto, ejecutar:

```powershell
docker compose exec db mysqldump -h 127.0.0.1 -u app_estudios -p --single-transaction --no-tablespaces --set-gtid-purged=OFF --result-file=/tmp/respaldo-estudios-medicos.sql estudios_medicos
```

Ingresar la contraseña de `MYSQL_PASSWORD` cuando se solicite.

Copiar el respaldo a la carpeta superior a la del proyecto:

```powershell
docker compose cp db:/tmp/respaldo-estudios-medicos.sql ..\respaldo-estudios-medicos.sql
```

Este archivo permite recuperar la tabla y sus registros.

## Probar la restauración en un entorno separado

Crear un segundo proyecto de Compose con su propio volumen
y esperar a que MySQL esté saludable:

```powershell
docker compose -p estudios-restauracion up -d --wait db
```

Comprobar el estado inicial de la tabla:

```powershell
docker compose -p estudios-restauracion exec db mysql -h 127.0.0.1 -u app_estudios -p -D estudios_medicos -e "SELECT COUNT(*) AS total FROM estudios;"
```

En una base nueva, el total esperado es 0.

Copiar e importar el respaldo:

```powershell
docker compose -p estudios-restauracion cp ..\respaldo-estudios-medicos.sql db:/tmp/respaldo-estudios-medicos.sql
docker compose -p estudios-restauracion exec db mysql -h 127.0.0.1 -u app_estudios -p -D estudios_medicos -e "source /tmp/respaldo-estudios-medicos.sql"
```

Consultar los registros recuperados:

```powershell
docker compose -p estudios-restauracion exec db mysql -h 127.0.0.1 -u app_estudios -p -D estudios_medicos -e "SELECT id, tipo_estudio, realizado, fecha_realizacion, informado FROM estudios;"
```

Comparar los resultados con los registros originales y guardar
evidencia de la comparación. MySQL muestra 1 para Sí y 0 para No.