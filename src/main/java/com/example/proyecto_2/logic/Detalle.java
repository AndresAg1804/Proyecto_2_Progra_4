package com.example.proyecto_2.logic;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Detalle {

    @Id
    @Column(name = "numD")
    private int numD;
    @Basic
    @Column(name = "cantidad")
    private Integer cantidad;
    @Basic
    @Column(name = "monto")
    private Integer monto;
    @ManyToOne
    @JoinColumn(name = "numfact", referencedColumnName = "numfact")
    private Facturas facturasByNumFact;
    @ManyToOne
    @JoinColumn(name = "idprod", referencedColumnName = "idPr")
    private Producto productoByIdProd;


    public int getNumD() {
        return numD;
    }

    public void setNumD(int numD) {
        this.numD = numD;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getMonto() {
        return monto;
    }

    public void setMonto(Integer monto) {
        this.monto = monto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Detalle detalle = (Detalle) o;
        return numD == detalle.numD && Objects.equals(cantidad, detalle.cantidad) && Objects.equals(monto, detalle.monto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numD, cantidad, monto);
    }

    public Facturas getFacturasByNumFact() {
        return facturasByNumFact;
    }

    public void setFacturasByNumFact(Facturas facturasByNumFact) {
        this.facturasByNumFact = facturasByNumFact;
    }

    public Producto getProductoByIdProd() {
        return productoByIdProd;
    }

    public void setProductoByIdProd(Producto productoByIdProd) {
        this.productoByIdProd = productoByIdProd;
    }

}
