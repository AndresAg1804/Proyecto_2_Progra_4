package com.example.proyecto_2.presentation.Usuario;

import jakarta.servlet.http.HttpSession;
import com.example.proyecto_2.logic.Proveedores;
import com.example.proyecto_2.logic.Service;
import com.example.proyecto_2.logic.Usuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@org.springframework.stereotype.Controller("usuarios")
@SessionAttributes({"usuarios"})
//solo el path, como decir un objecto espesifico para poder manerjar este servlet
//agara la info
public class Controller {

    @Autowired
    private Service service;
    @ModelAttribute("usuarios") public Usuarios Usuario() { return new Usuarios(); }

    @GetMapping("/newU")
    public String SENDnewU() {//index->newU
        return "/Presentation/Usuario/newU";
    }
    @GetMapping("/regreso")
    public String mostrarindex() {
        return "index";
    }
    @GetMapping("/presentation/Usuarios/show")// like mande a otro lado
    public String show(Model model,HttpSession session){// index/menu->show all usuarios
        Usuarios u = (Usuarios) session.getAttribute("usuario");
        model.addAttribute("usuarios", u);

        return "/Presentation/Usuario/view";
    }
    @PostMapping("/Usuarios/newU")//regi y mande a otro lado
    public String registrarUsuario(@RequestParam("usern") String usern,
                                   @RequestParam("pasw") String pasw,
                                   @RequestParam("tipo") String tipo,
                                   @RequestParam("nombreP") String nombreP,
                                   @RequestParam("idP") String idP,
                                   Model model, HttpSession session) {
        Usuarios u=service.addUsuario(usern,pasw,tipo,nombreP,idP);
        return "index";
    }

    @PostMapping("/login/login")
    public String login(@RequestParam("usern") String usern,
                     @RequestParam("pasw") String pasw, HttpSession session) {

        Usuarios ulog=null;
            try {
                ulog=service.login(usern,pasw);
                if(ulog!=null  ) { //1==true , esta aprovado
                    switch (ulog.getTipo()) {
                        case "PRO": {
                            if(ulog.getProveedoresByIdprov().getAprobado().equals((byte)1)){
                                session.setAttribute("usuario", ulog);
                                return "redirect:/presentation/Facturar/show";
                            }
                            else{
                                return "index";
                            }
                        }
                        case "ADM": {
                            session.setAttribute("usuario", ulog);
                            return "redirect:/presentation/Usuarios/amd";
                        }
                        default: {
                            return "index";
                        }
                    }
                }
            }
            catch (Exception ex){

            }
            return "/index";

    }
    @GetMapping("/presentation/Usuarios/amd")
    public String AMDapprove(Model model,HttpSession session) {Proveedores u = new Proveedores();
        model.addAttribute("usuarios_too_approve", service.usuariosFindAll());
        return "/Presentation/Usuario/viewALLusuariosAMD";
    }
    @PostMapping("/amd/approve")//regi y mande a otro lado
    public String AMDapprove(@RequestParam("username") String username,
                                   Model model, HttpSession session) {
        service.changePRO(username);
        return "redirect:/presentation/Usuarios/amd";
    }
    @GetMapping("/presentation/OUT/OUT")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/regreso";
    }
    //@psot recive un parametro Cliente->ese lo setea spring mientras el url que yo le mande en el <a th=href="@{/aaa/aaa/(numero=${c.getID} )}"> tenga
}

