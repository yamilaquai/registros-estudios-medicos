CREATE TABLE IF NOT EXISTS estudios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha_ingreso DATE NOT NULL,
    nombre_apellido VARCHAR(150) NOT NULL,
    dni VARCHAR(20) NOT NULL,
    tipo_estudio VARCHAR(100) NOT NULL,
    medico_interviniente VARCHAR(150) NOT NULL,
    realizado BOOLEAN NOT NULL DEFAULT FALSE,
    fecha_realizacion DATE,
    informado BOOLEAN NOT NULL DEFAULT FALSE
) DEFAULT CHARACTER SET utf8mb4;