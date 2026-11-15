<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Estudios registrados</title>
    <style>
        table {
            border-collapse: collapse;
        }

        th, td {
            border: 1px solid #bbb;
            padding: 0.5rem;
            text-align: left;
        }
    </style>
</head>
<body>
    <main>
        <h1>Estudios registrados</h1>
        <p><a href="${pageContext.request.contextPath}/">Cargar un estudio</a></p>

        <c:choose>
            <c:when test="${empty estudios}">
                <p>Todavía no hay estudios registrados.</p>
            </c:when>
            <c:otherwise>
                <table>
                    <thead>
                        <tr>
                            <th scope="col">Fecha de ingreso</th>
                            <th scope="col">Nombre y apellido</th>
                            <th scope="col">DNI</th>
                            <th scope="col">Tipo de estudio</th>
                            <th scope="col">Médico interviniente</th>
                            <th scope="col">Realizado</th>
                            <th scope="col">Fecha de realización</th>
                            <th scope="col">Informado</th>                            
		         	<th scope="col">Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="estudio" items="${estudios}">
                            <tr>
                                <td><c:out value="${estudio.fechaIngreso()}" /></td>
                                <td><c:out value="${estudio.nombreApellido()}" /></td>
                                <td><c:out value="${estudio.dni()}" /></td>
                                <td><c:out value="${estudio.tipoEstudio()}" /></td>
                                <td><c:out value="${estudio.medicoInterviniente()}" /></td>
                                <td><c:out value="${estudio.realizado() ? 'Sí' : 'No'}" /></td>
                                <td><c:out value="${estudio.fechaRealizacion()}" default="—" /></td>
                                <td><c:out value="${estudio.informado() ? 'Sí' : 'No'}" /></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not estudio.realizado()}">
                                            <form method="post" action="${pageContext.request.contextPath}/estudios/estado">
                                                <input type="hidden" name="id" value="${estudio.id()}">
                                                <input type="hidden" name="accion" value="realizar">
                                                <label>
                                                    Fecha de realización
                                                    <input type="date" name="fecha_realizacion" required>
                                                </label>
                                                <button type="submit">Marcar realizado</button>
                                            </form>
                                        </c:when>
                                        <c:when test="${not estudio.informado()}">
                                            <form method="post" action="${pageContext.request.contextPath}/estudios/estado">
                                                <input type="hidden" name="id" value="${estudio.id()}">
                                                <input type="hidden" name="accion" value="informar">
                                                <button type="submit">Marcar informado</button>
                                            </form>
                                        </c:when>
                                        <c:otherwise>
                                            Informado
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </main>
</body>
</html>