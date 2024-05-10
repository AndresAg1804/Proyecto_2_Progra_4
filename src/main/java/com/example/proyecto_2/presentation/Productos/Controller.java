package com.example.proyecto_2.presentation.Productos;

import jakarta.servlet.http.HttpSession;
import com.example.proyecto_2.logic.Producto;
import com.example.proyecto_2.logic.Proveedores;
import com.example.proyecto_2.logic.Service;
import com.example.proyecto_2.logic.Usuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.stereotype.Controller("Productos")
public class Controller {

    @Autowired
    private Service service;

    @GetMapping("/presentation/Productos/show")
    public String show(Model model,HttpSession session) {

        if(session.getAttribute("proEDIT")!=null){
            Producto proEDIT = (Producto) session.getAttribute("proEDIT");
            if(proEDIT.getNombreP()!=""){
                model.addAttribute("proEDIT", proEDIT);
            }
            else{
                model.addAttribute("proEDIT", null);
            }
        }
        //existe una mejor manera? si
        Usuarios u=(Usuarios) session.getAttribute("usuario");
        if(session.getAttribute("productos_BUSQUEDA")!=null){
            model.addAttribute("productos", session.getAttribute("productos_BUSQUEDA"));
            session.setAttribute("productos_BUSQUEDA",null);
        }
        else {
            session.setAttribute("productos", service.get_all_productos_de_IDprovedor(u.getProveedoresByIdprov().getIdP()));
            model.addAttribute("productos", session.getAttribute("productos"));
        }
        //model.addAttribute("productos",service.get_all_productos_de_IDprovedor(u.getProveedoresByIdprov().getIdP()));

        return "/Presentation/Productos/view";
    }
    @GetMapping("/set/editpro") //por mas que modifique los <a> solo pueden ser GetMapping
    public String editarPRO(@RequestParam("idPr")String idPr,
                            @RequestParam("nombreP")String nombreP,
                            @RequestParam("precio")Double precio,
                            @RequestParam("cant")Integer cant,
                            Model model, HttpSession session){

        Producto pp=new Producto(idPr,nombreP,precio,cant);
        session.setAttribute("proEDIT", pp);
        model.addAttribute("proEDIT",pp);
        return "redirect:/presentation/Productos/show";
    }
    @PostMapping("/productos/add")
    public String appPRO(@RequestParam("idPr") String idPr,
                         @RequestParam("nombreP") String nombreP,
                         @RequestParam("precio") Double precio,
                         @RequestParam("cant")Integer cant,
                         Model model, HttpSession session){
        Producto producto=new Producto(idPr,nombreP,precio,cant);

        Usuarios u=(Usuarios) session.getAttribute("usuario");
        Proveedores prove =u.getProveedoresByIdprov();
        producto.setProveedoresByIdProd(service.get_ProvedorBYID(u.getUsern()));

        if(model.getAttribute("proEDIT")!=null){
            service.updateProducto( nombreP,  precio,  cant, idPr);
            session.setAttribute("proEDIT", null);
            model.addAttribute("proEDIT", null);
        }
        else {
            //porque el ID DE cada Provedor es el Username
            service.addProdcuto(producto);
            session.setAttribute("proEDIT", null);
            model.addAttribute("proEDIT", null);
        }
        return "redirect:/presentation/Productos/show";
    }
    @PostMapping("/productos/buscar")
    public String buscPro(@RequestParam("idPr") String idPr,Model model, HttpSession session){
        if(idPr==""){

            return "redirect:/presentation/Productos/show";
        }
        Usuarios u=(Usuarios) session.getAttribute("usuario");
        session.setAttribute("productos_BUSQUEDA", service.findAllByProveedorIdAndProductoId(u.getProveedoresByIdprov().getIdP(),idPr));
        model.addAttribute("productos_BUSQUEDA", session.getAttribute("productos_BUSQUEDA"));
        return "redirect:/presentation/Productos/show";
    }


}
