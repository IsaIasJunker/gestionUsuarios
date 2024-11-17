package com.grupo1.demo.Services;
import com.grupo1.demo.Models.Sistema;
import com.grupo1.demo.Repositories.SistemaRepository;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DataLoaderService {
    
    @Autowired
    SistemaRepository sistemaRepository;

    /**
     * Metodo que permite cargar datos iniciales en la base de datos, pero solo si la tabla está vacia
     * (es decir, la tabla no tiene sistemas previos).
     */
    @PostConstruct
    public void loadData() {

        // Verificamos si la base de datos ya tiene registros en la tabla "Sistema"
        if (sistemaRepository.count() == 0) {

             // Si no hay registros, creamos nuevas instancias de la entidad "Sistema"
            Sistema sistema1 = new Sistema("cuentas");
            Sistema sistema2 = new Sistema("yimeil");
            Sistema sistema3 = new Sistema("draiv");
            Sistema sistema4 = new Sistema("k-lendar");
            sistemaRepository.save(sistema1);
            sistemaRepository.save(sistema2);
            sistemaRepository.save(sistema3);
            sistemaRepository.save(sistema4);
        }
    }
}

