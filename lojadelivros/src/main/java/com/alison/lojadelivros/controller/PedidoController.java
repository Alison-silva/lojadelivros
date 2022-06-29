package com.alison.lojadelivros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alison.lojadelivros.repository.CompraRepository;

@Controller
public class PedidoController {
	
	@Autowired
	private CompraRepository compraRepository;
	
	
	@RequestMapping(value = "/listadepedidos")
	public ModelAndView inicio() {
		ModelAndView modelAndView = new ModelAndView("cadastro/listadepedidos");
		modelAndView.addObject("pedidos", compraRepository.findAll());
		return modelAndView;
	}
	
	
	
	
	
	

}
