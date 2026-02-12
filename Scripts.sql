-- Insertar roles en la base de datos
-- Conectarse a la base de datos: mysql -u root -p castores_db < create_roles.sql

INSERT INTO roles (nombre_rol, descripcion, activo, fecha_creacion) VALUES 
('ADMINISTRADOR', 'Rol con acceso completo al sistema, puede gestionar usuarios, productos, categorías y ver todos los reportes', true, NOW()),
('ALMACENISTA', 'Rol para gestión de inventario, puede registrar entradas y salidas de productos, consultar stock', true, NOW());

-- Verificar que los roles fueron creados
SELECT * FROM roles WHERE nombre_rol IN ('ADMINISTRADOR', 'ALMACENISTA');

--- Insertar Usuario Administrador y Usuario Almacenista para pruebas
INSERT INTO `castores_db`.`usuarios` (
    `id_usuario`,
    `activo`,
    `email`,
    `fecha_creacion`,
    `fecha_modificacion`,
    `nombre_completo`,
    `password`,
    `ultimo_acceso`,
    `username`,
    `id_rol`
) VALUES 
(1, 1, 'juan@ejemplo.com', '2026-02-10 02:00:03.572887', '2026-02-12 06:10:51.131859', 'Juan Pérez', '$2a$10$oHSL3mlAiT4d06Z6TCS9X.D6S3.4wmGKn9a6fR1MMJ6EJD1vIxDV6', '2026-02-12 06:10:51.131022', 'juanperez', 1),
(2, 1, 'osvaldo@ejemplo.com', '2026-02-12 06:16:27.012811', '2026-02-12 06:16:27.012817', 'Juan Pérez', '$2a$10$65aYXfjJiqvXS/vuOW/AIejcF9xCmqzReUI5UsW9g85CHid7/m5pe', NULL, 'Osvaldo', 2);
