package com.example.proyecto_2.data;

import com.example.proyecto_2.logic.Detalle;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleRepository extends CrudRepository<Detalle, Integer> {

    @Query("select c from Detalle c")
    public List<Detalle> findAll();
}
