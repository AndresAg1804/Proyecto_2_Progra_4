package com.example.proyecto_2.data;


import com.example.proyecto_2.logic.Detalle;
import com.example.proyecto_2.logic.Facturas;
import com.example.proyecto_2.logic.Proveedores;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface facturasRepository extends CrudRepository<Facturas, Integer> {

    ArrayList<Facturas> findFacturasByProveedoresByIdProveedor(Proveedores prov);
    @Transactional
    @Modifying
    @Query("SELECT f FROM Facturas f WHERE f.proveedoresByIdProveedor.idP = ?1")
    List<Facturas> findAllByProveedorId(String idproveedor);

    Facturas getFacturasByNumFact(int numfact);
    @Query("SELECT d FROM Detalle d WHERE d.facturasByNumFact.numFact = ?1")
    Iterable<Detalle> findDetallesByFacturaNumFact(int numFact);

    @Query("SELECT f FROM Facturas f WHERE f.proveedoresByIdProveedor.idP = ?1 AND f.numFact = ?2")
    List<Facturas> findAllByIdProveedorAndNumFact(String idProveedor, int numFact);
}
