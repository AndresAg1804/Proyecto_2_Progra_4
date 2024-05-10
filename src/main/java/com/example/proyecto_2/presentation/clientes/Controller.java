package com.example.proyecto_2.presentation.clientes;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import com.example.proyecto_2.logic.Clientes;
import com.example.proyecto_2.logic.Proveedores;
import com.example.proyecto_2.logic.Service;
import com.example.proyecto_2.logic.Usuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@org.springframework.stereotype.Controller("Clientes")
@SessionAttributes({"clientes", "clienteSearch", "clienteEdit","proveedor, mode"})

public class Controller {
    @Autowired
    private Service service;
    @ModelAttribute("clientes") public Iterable<Clientes> clientes() {
        return new ArrayList<Clientes>();
    }

    @ModelAttribute("clienteSearch") public Clientes clienteSearch() {return new Clientes();}

    @ModelAttribute("clienteEdit") public Clientes clienteEdit() {return new Clientes(); }

    @ModelAttribute("proveedor")    public Proveedores proveedor() {return new Proveedores(); }

    @ModelAttribute("mode") public int mode() {return 0; } // 0: add, 1: edit


    @GetMapping("/presentation/Clientes/show")
    public String show(Model model, HttpSession session) {
        Usuarios usuario = (Usuarios) session.getAttribute("usuario");
        Proveedores proveedor = usuario.getProveedoresByIdprov();
        model.addAttribute("clientes", service.clienteFindByProveedor(proveedor.getIdP()));
        session.setAttribute("mode", 0);
        return "/Presentation/Clientes/view";
    }

    @PostMapping("/presentation/Clientes/search")
    public String search(
            @ModelAttribute("clienteSearch") Clientes clienteSearch,
            HttpSession session,
            Model model) {
        Usuarios u=(Usuarios) session.getAttribute("usuario");
        Proveedores proveedor =u.getProveedoresByIdprov();
        if(clienteSearch.getNombreC().isEmpty()){
            model.addAttribute("clientes", service.clienteFindByProveedor(proveedor.getIdP()));
        }
        else {
            model.addAttribute("clientes", service.buscarClientesPorNombreYProveedor(clienteSearch.getNombreC(), proveedor));
        }
        return "/Presentation/Clientes/view";
    }

    @GetMapping("/presentation/Clientes/edit")
    public String edit(@RequestParam("idC") String idC, Model model, HttpSession session) {
        Usuarios u=(Usuarios) session.getAttribute("usuario");
        Proveedores proveedor =u.getProveedoresByIdprov();
        Clientes cliente = service.clienteFindByIDyProvedor(idC, proveedor);
        model.addAttribute("clienteEdit", cliente);
        session.setAttribute("clienteEdit", cliente);
        session.setAttribute("mode", 1);
        model.addAttribute("mode", 1);
        return "/Presentation/Clientes/view";
    }

    @PostMapping("/presentation/Clientes/save")
    public String save(@ModelAttribute("clienteEdit") @Valid Clientes clienteEdit, BindingResult result, HttpSession session, Model model) {
        int mode = (int) session.getAttribute("mode");
        session.setAttribute("clienteEdit", clienteEdit);
        if (result.hasErrors()) {
            return "/Presentation/Clientes/view";
        }
        Usuarios u=(Usuarios) session.getAttribute("usuario");
        Proveedores proveedor =u.getProveedoresByIdprov();
        if (mode == 0) {
            clienteEdit.setProveedoresByProveedorid(service.get_ProvedorBYID(u.getUsern()));
            if(service.clienteFindByIDyProvedor(clienteEdit.getIdC(), proveedor) == null) {
                service.addCliente(clienteEdit);
                session.setAttribute("clienteEdit", new Clientes());
                model.addAttribute("clienteEdit", new Clientes());
            }
        }
        else {
            service.clienteEdit(clienteEdit, proveedor);
            session.setAttribute("clienteEdit", new Clientes());
            model.addAttribute("clienteEdit", new Clientes());
            session.setAttribute("mode", 0);
            model.addAttribute("mode", 0);
        }
        return "redirect:/presentation/Clientes/show";
    }


}
