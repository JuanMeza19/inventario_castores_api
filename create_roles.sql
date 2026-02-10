-- Insertar roles en la base de datos
-- Conectarse a la base de datos: mysql -u root -p castores_db < create_roles.sql

INSERT INTO roles (nombre_rol, descripcion, activo, fecha_creacion) VALUES 
('ADMINISTRADOR', 'Rol con acceso completo al sistema, puede gestionar usuarios, productos, categorías y ver todos los reportes', true, NOW()),
('ALMACENISTA', 'Rol para gestión de inventario, puede registrar entradas y salidas de productos, consultar stock', true, NOW());

-- Verificar que los roles fueron creados
SELECT * FROM roles WHERE nombre_rol IN ('ADMINISTRADOR', 'ALMACENISTA');