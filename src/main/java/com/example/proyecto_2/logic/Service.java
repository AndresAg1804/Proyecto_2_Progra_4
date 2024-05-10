package com.example.proyecto_2.logic;


import com.example.proyecto_2.data.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@org.springframework.stereotype.Service("service")
public class Service {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ProveedorRepository proveedorRepository;
    @Autowired
    private usuariosRepository usuarioRepository;
    @Autowired
    private facturasRepository facturasRepository;
    @Autowired
    private ProductosRepository productosRepository;
@Autowired
    private DetalleRepository detalleRepository;

    //Metodos para clientes
    public Iterable<Clientes> clienteFindAll(){
    return clienteRepository.findAll();
    }
    public Optional<Clientes> clienteFindByID(String id){
        return clienteRepository.findById(id); 
    }

    public Iterable<Clientes> clienteFindByProveedor(String idProveedor){
        return clienteRepository.findClientesByProveedor(idProveedor);
    }

    public Clientes clienteFindByIDyProvedor(String idc, Proveedores proveedores){
        return clienteRepository.findByIdCAndProveedoresByProveedorid(idc,proveedores);
    }

    public void addCliente(Clientes c){
        clienteRepository.save(c);
    }

    public void clienteEdit(Clientes cliente, Proveedores proveedor){
        Clientes c=clienteRepository.findByIdCAndProveedoresByProveedorid(cliente.getIdC(),proveedor);
        c.setNombreC(cliente.getNombreC());
        c.setCorreo(cliente.getCorreo());
        c.setTelefono(cliente.getTelefono());
        clienteRepository.save(c);
    }

    public List<Clientes> buscarClientesPorNombreYProveedor(String nombre, Proveedores proveedores){
        return clienteRepository.findClientesByNombreCContainsAndProveedoresByProveedorid(nombre,proveedores);
    }

    //Metodos para proveedores
    public Iterable<Proveedores> proveedorFindAll(){return proveedorRepository.findAll();}

    //Metodos para usuarios
    public Iterable<Usuarios> usuariosFindAll() {return usuarioRepository.findAll();}

    public Iterable<Producto> get_all_productos_de_user(){return productosRepository.findAll();}

    public Usuarios addUsuario(String usern, String pasw, String tipo,String nombreP,String idP) {
        Proveedores p=new Proveedores();
        p.setNombreP(nombreP);
        p.setIdP(idP);
        p.setAprobado((byte) 0);//0=fals 1=true
        proveedorRepository.save(p);
    Usuarios u=new Usuarios();
    u.setUsern(usern);
    u.setPasw(pasw);
    u.setTipo(tipo);
    u.setProveedoresByIdprov(p);
    usuarioRepository.save(u);
    return u;
    }

    public boolean existeU(String us){
        return usuarioRepository.existsById(us);
    }

    public Usuarios login(String usern,String pasw){
        if(existeU(usern)==true){
            return usuarioRepository.findByUsernAndPasw(usern,pasw);
        }
        return null;
    }

    public void changePRO(String username){
        Usuarios u=null;
        if(existeU(username)==true) {
            u = usuarioRepository.findByUsern(username);
            if (u.getProveedoresByIdprov().getAprobado().equals((byte) 0)) {
                u.getProveedoresByIdprov().setAprobado((byte) 1);
            } else {
                u.getProveedoresByIdprov().setAprobado((byte) 0);//boole
            }
            usuarioRepository.save(u);
        }
        //          1=true;
        //          0=false;
    };

    //Metodos para facturas
    public Iterable<Facturas> findFacturasByIdProveedor(String prov){
        return facturasRepository.findAllByProveedorId(prov);
    }

    //Metodos para productos
    public Producto findProdByIdAndProveedor(String id, Proveedores prov){
        return productosRepository.findByIdPrAndProveedoresByIdProd(id,prov);
    }
    public Proveedores get_ProvedorBYID(String id){
        return usuarioRepository.findByUsern(id).getProveedoresByIdprov();
    }
    public void addProdcuto(Producto p){
        productosRepository.save(p);
    }

    public  Iterable<Producto> get_all_productos_de_IDprovedor(String Prob){
        return productosRepository.get_all_productos_de_IDprovedor(Prob);
    }


    public ArrayList<Detalle> actualizaLista(ArrayList<Detalle> lista, String idProducto, int cant, Proveedores proveedor, int modo){ //modo 1 es para verificar que
        //no aumente más de lo permitido y modo 2 para que no disminuya más de lo permitido
        Producto prod=findProdByIdAndProveedor(idProducto, proveedor);
        int monto=0;
        if(modo==1){
            if(prod.getCant()>=cant){
                for(Detalle det : lista){
                    if(Objects.equals(det.getProductoByIdProd().getIdPr(), idProducto)){
                        det.setCantidad(cant);
                       monto= (int) (det.getProductoByIdProd().getPrecio() * det.getCantidad());
                       det.setMonto(monto);
                    }
                }
            }
        }
        else {
            if(cant>=1){
                for(Detalle det : lista){
                    if(Objects.equals(det.getProductoByIdProd().getIdPr(), idProducto)){
                        det.setCantidad(cant);
                        monto= (int) (det.getProductoByIdProd().getPrecio() * det.getCantidad());
                        det.setMonto(monto);
                    }
                }
            }
        }

        return lista;
    }

    public boolean alreadyInList(ArrayList<Detalle> lista, String prod){
        for(Detalle det : lista){
            if(Objects.equals(det.getProductoByIdProd().getIdPr(), prod)){
                return true;
            }
        }
        return false;
    }

    public void guardarFactura(Facturas fact, ArrayList<Detalle> lista){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d");
        String formattedDate = now.format(formatter);
        fact.setFecha(formattedDate);
        int tot=0;
        for(Detalle det : lista){
            tot+=det.getMonto();

        }
        fact.setTotal(tot);
        facturasRepository.save(fact);
        List<Facturas> listF = facturasRepository.findAllByProveedorId(fact.getProveedoresByIdProveedor().getIdP());
        Facturas factura = listF.getLast();

        for(Detalle det : lista) {
            int i = detalleRepository.findAll().size() + 1;
            det.setFacturasByNumFact(factura);
            det.setNumD(i);
            detalleRepository.save(det);
            Producto prod = det.getProductoByIdProd();
            prod.setCant(prod.getCant() - det.getCantidad());
            productosRepository.updateProducto(prod.getNombreP(), prod.getPrecio(), prod.getCant(), prod.getIdPr());
            }
        }
    public void updateProducto(String nombrep, double precio, int cantidad, String idpr) {
        productosRepository.updateProducto(nombrep, precio, cantidad, idpr);
    }
    public Facturas get_FacturaXid(int numfact) {
        return facturasRepository.getFacturasByNumFact(numfact);
    }

    public Iterable<Detalle> findDetallesByFacturaNumFact(int  numf){
        return facturasRepository.findDetallesByFacturaNumFact(numf);
    }
    public List<Facturas> findAllByIdProveedorAndNumFact(String idProveedor, int numFact){
        return facturasRepository.findAllByIdProveedorAndNumFact(idProveedor,numFact);
    }
    public List<Producto> findAllByProveedorIdAndProductoId(String idProveedor, String idProducto){
        return productosRepository.findAllByProveedorIdAndProductoId(idProveedor,idProducto);
    }

}
