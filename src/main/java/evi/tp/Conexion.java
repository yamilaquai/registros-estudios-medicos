package evi.tp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Conexion {

    private Conexion() {
    }

    public static Connection abrir() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se pudo cargar el conector de MySQL.", e);
        }

        String host = System.getenv("DB_HOST");
        String base = System.getenv("DB_NAME");
        String usuario = System.getenv("DB_USER");
        String clave = System.getenv("DB_PASSWORD");
        String url = "jdbc:mysql://" + host + ":3306/" + base;

        return DriverManager.getConnection(url, usuario, clave);
    }
}