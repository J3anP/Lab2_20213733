package com.example.lab2gtics.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
    @RequestMapping(value = "/buscaminas", method = RequestMethod.GET)
    public String paginaPrincipal(){
        return "configuracion";
    }

    @GetMapping( value={"/jugar" ,"/minar"})
    public String juego(){
        return "juego";
    }

}
