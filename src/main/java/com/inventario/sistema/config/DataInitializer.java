package com.inventario.sistema.config;

import com.inventario.sistema.entity.TipoMovimiento;
import com.inventario.sistema.repository.TipoMovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private TipoMovimientoRepository tipoMovimientoRepository;

    @Override
    public void run(String... args) throws Exception {
        if (tipoMovimientoRepository.count() == 0) {
            // Crear tipo de movimiento ENTRADA
            TipoMovimiento entrada = new TipoMovimiento();
            entrada.setNombreTipo("ENTRADA");
            entrada.setDescripcion("Movimiento de entrada de productos al inventario");
            entrada.setAfectaInventario(TipoMovimiento.AfectaInventario.SUMA);
            entrada.setActivo(true);
            tipoMovimientoRepository.save(entrada);

            // Crear tipo de movimiento SALIDA
            TipoMovimiento salida = new TipoMovimiento();
            salida.setNombreTipo("SALIDA");
            salida.setDescripcion("Movimiento de salida de productos del inventario");
            salida.setAfectaInventario(TipoMovimiento.AfectaInventario.RESTA);
            salida.setActivo(true);
            tipoMovimientoRepository.save(salida);

            System.out.println("Tipos de movimiento inicializados correctamente");
        }
    }
}