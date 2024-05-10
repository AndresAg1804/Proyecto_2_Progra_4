package com.example.proyecto_2.data;


import com.example.proyecto_2.logic.Producto;
import com.example.proyecto_2.logic.Proveedores;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductosRepository extends CrudRepository<Producto, String>{

    Producto findByIdPrAndProveedoresByIdProd(String idProducto, Proveedores proveedor);
    @Query("select p from Producto p where p.proveedoresByIdProd.idP=?1")
    List<Producto> get_all_productos_de_IDprovedor(String idProveedor);

    @Transactional
    @Modifying
    @Query("update Producto p set p.nombreP = ?1, p.precio = ?2, p.cant = ?3 where p.idPr = ?4")
    void updateProducto(String nombreP, double precio, int cantidad, String idPr);

    @Query("SELECT p FROM Producto p WHERE p.proveedoresByIdProd.idP = ?1 AND p.idPr = ?2")
    List<Producto> findAllByProveedorIdAndProductoId(String idProveedor, String idProducto);


}
