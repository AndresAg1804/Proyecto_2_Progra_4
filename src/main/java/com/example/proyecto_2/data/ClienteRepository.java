package com.example.proyecto_2.data;

import com.example.proyecto_2.logic.Clientes;
import com.example.proyecto_2.logic.Proveedores;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends CrudRepository<Clientes, String> {

    @Query("select c from Clientes c where c.proveedoresByProveedorid.idP=?1")
    List<Clientes> findClientesByProveedor(String idProveedor);

    Clientes findByIdCAndProveedoresByProveedorid(String idC, Proveedores proveedores);

    List<Clientes> findClientesByNombreCContainsAndProveedoresByProveedorid(String nombreC, Proveedores proveedores);


}
