package com.inventario.sistema.service;

import com.inventario.sistema.entity.MovimientoInventario;
import com.inventario.sistema.entity.Producto;
import com.inventario.sistema.entity.TipoMovimiento;
import com.inventario.sistema.entity.Usuario;
import com.inventario.sistema.repository.MovimientoInventarioRepository;
import com.inventario.sistema.repository.ProductoRepository;
import com.inventario.sistema.repository.TipoMovimientoRepository;
import com.inventario.sistema.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InventoryService {

    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private MovimientoInventarioRepository movimientoRepository;
    
    @Autowired
    private TipoMovimientoRepository tipoMovimientoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public MovimientoInventario agregarInventario(Long productoId, Integer cantidad, Long usuarioId, String observaciones) {
        if (cantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a cero");
        }

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productoId));

        if (!producto.getActivo()) {
            throw new RuntimeException("No se puede modificar inventario de un producto inactivo");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        TipoMovimiento tipoMovimiento = tipoMovimientoRepository.findByNombreTipo("ENTRADA")
                .orElseThrow(() -> new RuntimeException("Tipo de movimiento ENTRADA no encontrado"));

        Integer cantidadAnterior = producto.getCantidadActual();
        Integer cantidadNueva = cantidadAnterior + cantidad;

        producto.setCantidadActual(cantidadNueva);
        productoRepository.save(producto);

        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setProducto(producto);
        movimiento.setTipoMovimiento(tipoMovimiento);
        movimiento.setCantidad(cantidad);
        movimiento.setCantidadAnterior(cantidadAnterior);
        movimiento.setCantidadNueva(cantidadNueva);
        movimiento.setUsuario(usuario);
        movimiento.setObservaciones(observaciones);
        movimiento.setReferencia("ENTRADA-" + System.currentTimeMillis());

        return movimientoRepository.save(movimiento);
    }

    @Transactional
    public MovimientoInventario restarInventario(Long productoId, Integer cantidad, Long usuarioId, String observaciones) {
        if (cantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a cero");
        }

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productoId));

        if (!producto.getActivo()) {
            throw new RuntimeException("No se puede modificar inventario de un producto inactivo");
        }

        if (producto.getCantidadActual() < cantidad) {
            throw new RuntimeException("No hay suficiente inventario. Actual: " + producto.getCantidadActual() + 
                    ", Solicitado: " + cantidad);
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        TipoMovimiento tipoMovimiento = tipoMovimientoRepository.findByNombreTipo("SALIDA")
                .orElseThrow(() -> new RuntimeException("Tipo de movimiento SALIDA no encontrado"));

        Integer cantidadAnterior = producto.getCantidadActual();
        Integer cantidadNueva = cantidadAnterior - cantidad;

        producto.setCantidadActual(cantidadNueva);
        productoRepository.save(producto);

        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setProducto(producto);
        movimiento.setTipoMovimiento(tipoMovimiento);
        movimiento.setCantidad(cantidad);
        movimiento.setCantidadAnterior(cantidadAnterior);
        movimiento.setCantidadNueva(cantidadNueva);
        movimiento.setUsuario(usuario);
        movimiento.setObservaciones(observaciones);
        movimiento.setReferencia("SALIDA-" + System.currentTimeMillis());

        return movimientoRepository.save(movimiento);
    }

    public List<MovimientoInventario> getMovimientosByProducto(Long productoId) {
        return movimientoRepository.findMovimientosByProducto(productoId);
    }

    public List<MovimientoInventario> getMovimientosByTipo(String tipoMovimiento) {
        TipoMovimiento tipo = tipoMovimientoRepository.findByNombreTipo(tipoMovimiento)
                .orElseThrow(() -> new RuntimeException("Tipo de movimiento no encontrado: " + tipoMovimiento));
        return movimientoRepository.findMovimientosByTipo(tipo.getId());
    }

    public List<MovimientoInventario> getMovimientosPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return movimientoRepository.findMovimientosPorFecha(fechaInicio, fechaFin);
    }

    public List<MovimientoInventario> getAllMovimientos() {
        return movimientoRepository.findAll();
    }

    public List<Producto> getProductosActivos() {
        return productoRepository.findProductosActivos();
    }

    public List<Producto> getProductosInactivos() {
        return productoRepository.findProductosInactivos();
    }

    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }
}