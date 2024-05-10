package com.example.proyecto_2.logic;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.Objects;

@Entity
public class Clientes {
    @Basic
    @Column(name = "nombreC")
    private String nombreC;

    @Id
    @Column(name = "idC")
    private String idC;
    @Basic
    @Column(name = "correo")
    private String correo;
    @Basic
    @Column(name = "telefono")
    private Integer telefono;
    @ManyToOne
    @JoinColumn(name = "proveedorid", referencedColumnName = "idP")
    private Proveedores proveedoresByProveedorid;
    @OneToMany(mappedBy = "clientesByIdCliente")
    private Collection<Facturas> facturasByIdC;


    public String getNombreC() {
        return nombreC;
    }

    public void setNombreC(String nombreC) {
        this.nombreC = nombreC;
    }

    public String getIdC() {
        return idC;
    }

    public void setIdC(String idC) {
        this.idC = idC;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Integer getTelefono() {
        return telefono;
    }

    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clientes clientes = (Clientes) o;
        return Objects.equals(nombreC, clientes.nombreC) && Objects.equals(idC, clientes.idC) && Objects.equals(correo, clientes.correo) && Objects.equals(telefono, clientes.telefono);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombreC, idC, correo, telefono);
    }

    public Proveedores getProveedoresByProveedorid() {
        return proveedoresByProveedorid;
    }

    public void setProveedoresByProveedorid(Proveedores proveedoresByProveedorid) {
        this.proveedoresByProveedorid = proveedoresByProveedorid;
    }

    public Collection<Facturas> getFacturasByIdC() {
        return facturasByIdC;
    }

    public void setFacturasByIdC(Collection<Facturas> facturasByIdC) {
        this.facturasByIdC = facturasByIdC;
    }
}
