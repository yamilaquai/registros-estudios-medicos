package evi.tp;

import java.time.LocalDate;

public record Estudio(
        int id,
        LocalDate fechaIngreso,
        String nombreApellido,
        String dni,
        String tipoEstudio,
        String medicoInterviniente,
        boolean realizado,
        LocalDate fechaRealizacion,
        boolean informado
) {
}