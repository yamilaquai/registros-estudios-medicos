<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Registro de estudios médicos</title>
</head>
<body>
    <main>
        <h1>Registro de estudios médicos</h1>
        <h2>Nuevo estudio</h2>

        <form method="post" action="estudios">
            <p>
                <label for="nombre_apellido">Nombre y apellido</label><br>
                <input id="nombre_apellido" name="nombre_apellido"
                       type="text" maxlength="150" required>
            </p>
            <p>
                <label for="dni">DNI</label><br>
                <input id="dni" name="dni"
                       type="text" inputmode="numeric" maxlength="20" required>
            </p>
            <p>
                <label for="tipo_estudio">Tipo de estudio</label><br>
                <input id="tipo_estudio" name="tipo_estudio"
                       type="text" maxlength="100" required>
            </p>
            <p>
                <label for="medico_interviniente">Médico interviniente</label><br>
                <input id="medico_interviniente" name="medico_interviniente"
                       type="text" maxlength="150" required>
            </p>

            <button type="submit">Registrar estudio</button>
        </form>
    </main>
</body>
</html>