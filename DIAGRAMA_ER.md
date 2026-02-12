# Diagrama Entidad-Relación - Sistema de Inventario Castores

## Vista General del Sistema

El sistema de inventario consta de 7 entidades principales interconectadas:

```
┌─────────────────┐    ┌──────────────┐    ┌─────────────────┐
│    ROLES      │    │   USUARIOS   │    │    SESIONES    │
├─────────────────┤    ├──────────────┤    ├─────────────────┤
│ id_rol PK     │◄──┤ id_usuario PK ├──►│ id_sesion PK    │
│ nombre_rol UK  │    │ username UK   │    │ id_usuario FK   │
│ descripcion    │    │ password      │    │ fecha_inicio    │
│ activo        │    │ nombre_completo│    │ fecha_fin       │
│ fecha_creacion │    │ email UK     │    │ ip_address     │
└─────────────────┘    │ id_rol FK    │    │ user_agent      │
                       │ activo       │    └─────────────────┘
                       │ fecha_creacion│
                       │ fecha_modif   │
                       │ ultimo_acceso │
                       └──────────────┘

┌─────────────────────────┐    ┌──────────────────────┐
│   CATEGORIAS_PRODUCTO  │    │      PRODUCTOS       │
├─────────────────────────┤    ├──────────────────────┤
│ id_categoria PK        │◄──┤ id_producto PK       │
│ nombre_categoria UK     │    │ codigo_producto UK    │
│ descripcion           │    │ nombre_producto       │
│ activo               │    │ descripcion         │
│ fecha_creacion        │    │ id_categoria FK      │
└─────────────────────────┘    │ cantidad_actual      │
                              │ unidad_medida        │
                              │ precio_unitario      │
                              │ stock_minimo        │
                              │ stock_maximo        │
                              │ activo              │
                              │ fecha_creacion      │
                              │ fecha_modificacion   │
                              │ id_usuario_creacion FK│
                              └──────────────────────┘

┌──────────────────────┐    ┌────────────────────────────┐
│ TIPOS_MOVIMIENTO    │    │ MOVIMIENTOS_INVENTARIO   │
├──────────────────────┤    ├────────────────────────────┤
│ id_tipo_movimiento PK│◄──┤ id_movimiento PK         │
│ nombre_tipo UK       │    │ id_producto FK           │
│ descripcion         │    │ id_tipo_movimiento FK   │
│ afecta_inventario   │    │ cantidad                │
│ activo             │    │ cantidad_anterior        │
│ fecha_creacion      │    │ cantidad_nueva          │
└──────────────────────┘    │ id_usuario FK           │
                           │ fecha_movimiento        │
                           │ observaciones           │
                           │ referencia             │
                           └────────────────────────────┘
```

## Flujo de Negocio Principal

### 1. Gestión de Usuarios
1. **Usuario** → Pertenece a un **Rol** (ADMIN, MANAGER, EMPLOYEE)
2. **Usuario** → Puede tener múltiples **Sesiones** (login/logout)
3. **Rol** → Define permisos del sistema

### 2. Gestión de Productos
1. **Producto** → Clasificado en **Categoría**
2. **Producto** → Creado por **Usuario**
3. **Producto** → Afectado por **Movimientos de Inventario**
4. **Producto** → Estado activo/inactivo

### 3. Movimientos de Inventario
1. **Movimiento** → Realizado por **Usuario**
2. **Movimiento** → Afecta un **Producto**
3. **Movimiento** → Tipificado como **ENTRADA** o **SALIDA**
4. **Movimiento** → Registra cantidades antes/después