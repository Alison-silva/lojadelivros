package com.alison.lojadelivros.controller;

import com.alison.lojadelivros.service.GraficoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
public class GraficoController {

    @Autowired
    private GraficoService graficoService;

    @RequestMapping(value = "/graficomedia")
    public ModelAndView inicio() {
        ModelAndView modelAndView = new ModelAndView("cadastro/graficomedia");
        List<Map<String, Object>> dadosGrafico = graficoService.GerarGrafico();
        modelAndView.addObject("dadosGrafico", dadosGrafico);
        return modelAndView;
    }



}
