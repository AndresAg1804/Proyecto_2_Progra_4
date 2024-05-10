package com.example.proyecto_2.logic;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.Objects;

@Entity
public class Facturas {
    @Id
    @Column(name = "numfact")
    private int numFact;
    @Basic
    @Column(name = "total")
    private Integer total;
    @OneToMany(mappedBy = "facturasByNumFact")
    private Collection<Detalle> detallesByNumFact;
    @ManyToOne
    @JoinColumns(@JoinColumn(name = "idcliente", referencedColumnName = "idC"))//aqui cambio jp
    private Clientes clientesByIdCliente;
    @ManyToOne
    @JoinColumns(@JoinColumn(name = "idproveedor", referencedColumnName = "idP"))//aqui cambio jp
    private Proveedores proveedoresByIdProveedor;
    @Basic
    @Column(name = "fecha")
    private String fecha;

    public int getNumFact() {
        return numFact;
    }

    public void setNumFact(int numFact) {
        this.numFact = numFact;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Facturas facturas = (Facturas) o;
        return numFact == facturas.numFact && Objects.equals(total, facturas.total);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numFact, total);
    }

    public Collection<Detalle> getDetallesByNumFact() {
        return detallesByNumFact;
    }

    public void setDetallesByNumFact(Collection<Detalle> detallesByNumFact) {
        this.detallesByNumFact = detallesByNumFact;
    }

    public Clientes getClientesByIdCliente() {
        return clientesByIdCliente;
    }

    public void setClientesByIdCliente(Clientes clientesByIdCliente) {
        this.clientesByIdCliente = clientesByIdCliente;
    }

    public Proveedores getProveedoresByIdProveedor() {
        return proveedoresByIdProveedor;
    }

    public void setProveedoresByIdProveedor(Proveedores proveedoresByIdProveedor) {
        this.proveedoresByIdProveedor = proveedoresByIdProveedor;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
