package evi.tp;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/estudios/estado")
public class EstadoEstudioServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest solicitud, HttpServletResponse respuesta)
            throws IOException, ServletException {

        int id;
        try {
            id = Integer.parseInt(solicitud.getParameter("id"));
        } catch (NumberFormatException e) {
            respuesta.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "El identificador del estudio no es válido.");
            return;
        }

        String accion = solicitud.getParameter("accion");
        String sql;
        LocalDate fechaRealizacion = null;

        if ("realizar".equals(accion)) {
            String fecha = solicitud.getParameter("fecha_realizacion");

            try {
                fechaRealizacion = LocalDate.parse(fecha == null ? "" : fecha);
            } catch (DateTimeParseException e) {
                respuesta.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Ingresá una fecha de realización válida.");
                return;
            }

            LocalDate hoy = LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires"));
            if (fechaRealizacion.isAfter(hoy)) {
                respuesta.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "La fecha de realización no puede ser posterior a hoy.");
                return;
            }

            sql = "UPDATE estudios SET realizado = TRUE, fecha_realizacion = ? "
                    + "WHERE id = ? AND realizado = FALSE";

        } else if ("informar".equals(accion)) {
            sql = "UPDATE estudios SET informado = TRUE "
                    + "WHERE id = ? AND realizado = TRUE AND informado = FALSE";

        } else {
            respuesta.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "La acción indicada no es válida.");
            return;
        }

        try (Connection conexion = Conexion.abrir();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            if (fechaRealizacion != null) {
                consulta.setObject(1, fechaRealizacion);
                consulta.setInt(2, id);
            } else {
                consulta.setInt(1, id);
            }

            if (consulta.executeUpdate() == 0) {
                respuesta.sendError(HttpServletResponse.SC_CONFLICT,
                        "El estudio no existe o ya cambió de estado.");
                return;
            }

        } catch (SQLException e) {
            throw new ServletException("No se pudo actualizar el estudio.", e);
        }

        respuesta.sendRedirect(solicitud.getContextPath() + "/estudios");
    }
}