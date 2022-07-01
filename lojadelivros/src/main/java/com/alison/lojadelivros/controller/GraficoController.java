package com.alison.lojadelivros.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GraficoController {



	@RequestMapping(method = RequestMethod.GET, value = "/graficotela")
	public ModelAndView inicio() {

		ModelAndView modelAndView = new ModelAndView("cadastro/graficotela");

		return modelAndView;
	}

}
