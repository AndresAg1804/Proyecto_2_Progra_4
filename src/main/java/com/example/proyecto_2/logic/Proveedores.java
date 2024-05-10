package com.example.proyecto_2.logic;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.Objects;

@Entity
public class Proveedores {
    @Basic
    @Column(name = "nombreP")
    private String nombreP;

    @Id
    @Column(name = "idP")
    private String idP;
    @Basic
    @Column(name = "aprobado")
    private Byte aprobado;
    @OneToMany(mappedBy = "proveedoresByProveedorid")
    private Collection<Clientes> clientesByIdP;
    @OneToMany(mappedBy = "proveedoresByIdProveedor")
    private Collection<Facturas> facturasByIdP;
    @OneToMany(mappedBy = "proveedoresByIdProd")
    private Collection<Producto> productosByIdP;
    @OneToMany(mappedBy = "proveedoresByIdprov")
    private Collection<Usuarios> usuariosByIdP;

    public String getNombreP() {
        return nombreP;
    }

    public void setNombreP(String nombreP) {
        this.nombreP = nombreP;
    }

    public String getIdP() {
        return idP;
    }

    public void setIdP(String idP) {
        this.idP = idP;
    }

    public Byte getAprobado() {
        return aprobado;
    }

    public void setAprobado(Byte aprobado) {
        this.aprobado = aprobado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Proveedores that = (Proveedores) o;
        return Objects.equals(nombreP, that.nombreP) && Objects.equals(idP, that.idP) && Objects.equals(aprobado, that.aprobado);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombreP, idP, aprobado);
    }

    public Collection<Clientes> getClientesByIdP() {
        return clientesByIdP;
    }

    public void setClientesByIdP(Collection<Clientes> clientesByIdP) {
        this.clientesByIdP = clientesByIdP;
    }

    public Collection<Facturas> getFacturasByIdP() {
        return facturasByIdP;
    }

    public void setFacturasByIdP(Collection<Facturas> facturasByIdP) {
        this.facturasByIdP = facturasByIdP;
    }

    public Collection<Producto> getProductosByIdP() {
        return productosByIdP;
    }

    public void setProductosByIdP(Collection<Producto> productosByIdP) {
        this.productosByIdP = productosByIdP;
    }

    public Collection<Usuarios> getUsuariosByIdP() {
        return usuariosByIdP;
    }

    public void setUsuariosByIdP(Collection<Usuarios> usuariosByIdP) {
        this.usuariosByIdP = usuariosByIdP;
    }
}
