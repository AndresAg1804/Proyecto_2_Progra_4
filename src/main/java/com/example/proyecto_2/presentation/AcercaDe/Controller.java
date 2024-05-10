package com.example.proyecto_2.presentation.AcercaDe;
import com.example.proyecto_2.logic.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller("acercaDe")
public class Controller {
    @Autowired
    private Service service;

    @GetMapping("/presentation/AcercaDe/show")
    public String show() {
        return "Presentation/AcercaDe/view";
    }


}
