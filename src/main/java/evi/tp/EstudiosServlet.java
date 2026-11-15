package evi.tp;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/estudios")
public class EstudiosServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest solicitud, HttpServletResponse respuesta)
            throws IOException, ServletException {

        List<Estudio> estudios = new ArrayList<>();

        String sql = "SELECT id, fecha_ingreso, nombre_apellido, dni, tipo_estudio, "
                + "medico_interviniente, realizado, fecha_realizacion, informado "
                + "FROM estudios ORDER BY id DESC";

        try (Connection conexion = Conexion.abrir();
             Statement consulta = conexion.createStatement();
             ResultSet resultado = consulta.executeQuery(sql)) {

            while (resultado.next()) {
                Estudio estudio = new Estudio(
                        resultado.getInt("id"),
                        resultado.getObject("fecha_ingreso", LocalDate.class),
                        resultado.getString("nombre_apellido"),
                        resultado.getString("dni"),
                        resultado.getString("tipo_estudio"),
                        resultado.getString("medico_interviniente"),
                        resultado.getBoolean("realizado"),
                        resultado.getObject("fecha_realizacion", LocalDate.class),
                        resultado.getBoolean("informado")
                );
                estudios.add(estudio);
            }

        } catch (SQLException e) {
            throw new ServletException("No se pudo consultar la tabla de estudios.", e);
        }

        solicitud.setAttribute("estudios", estudios);
        solicitud.getRequestDispatcher("/WEB-INF/listado.jsp")
                .forward(solicitud, respuesta);
    }

    @Override
    protected void doPost(HttpServletRequest solicitud, HttpServletResponse respuesta)
            throws IOException, ServletException {

        solicitud.setCharacterEncoding("UTF-8");

        String nombre = leerCampo(solicitud, "nombre_apellido", 150);
        String dni = leerCampo(solicitud, "dni", 20);
        String tipo = leerCampo(solicitud, "tipo_estudio", 100);
        String medico = leerCampo(solicitud, "medico_interviniente", 150);

        if (nombre == null || dni == null || tipo == null || medico == null) {
            respuesta.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Completá todos los campos respetando su longitud máxima.");
            return;
        }

        LocalDate fechaIngreso = LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires"));
        String sql = "INSERT INTO estudios "
                + "(fecha_ingreso, nombre_apellido, dni, tipo_estudio, medico_interviniente) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conexion = Conexion.abrir();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setObject(1, fechaIngreso);
            consulta.setString(2, nombre);
            consulta.setString(3, dni);
            consulta.setString(4, tipo);
            consulta.setString(5, medico);
            consulta.executeUpdate();

        } catch (SQLException e) {
            throw new ServletException("No se pudo guardar el estudio.", e);
        }

        respuesta.sendRedirect(solicitud.getContextPath() + "/estudios");
    }

    private String leerCampo(HttpServletRequest solicitud, String nombre, int maximo) {
        String valor = solicitud.getParameter(nombre);

        if (valor == null) {
            return null;
        }

        valor = valor.trim();
        if (valor.isEmpty() || valor.length() > maximo) {
            return null;
        }

        return valor;
    }
}