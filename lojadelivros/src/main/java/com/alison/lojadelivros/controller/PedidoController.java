package com.alison.lojadelivros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alison.lojadelivros.model.Compra;
import com.alison.lojadelivros.repository.CompraRepository;

@Controller
public class PedidoController {
	
	@Autowired
	private CompraRepository compraRepository;
	
	
	@RequestMapping(value = "/listadepedidos")
	public ModelAndView inicio() {
		ModelAndView modelAndView = new ModelAndView("cadastro/listadepedidos");
		modelAndView.addObject("pedidos", compraRepository.findAll(PageRequest.of(0, 8)));
		return modelAndView;
	}
	
	@GetMapping("/pedidopag")
	public ModelAndView pedidopag(@PageableDefault(size = 8) Pageable pageable, ModelAndView model) {

		Page<Compra> pageCompra = compraRepository.findAll(pageable);
		model.addObject("pedidos", pageCompra);
		model.setViewName("cadastro/listadepedidos");
		return model;
	}

	@GetMapping("/removerpedido/{id}")
	public ModelAndView excluir(@PathVariable("id") Long id) {
		
		compraRepository.deleteById(id);
		
		ModelAndView modelAndView = new ModelAndView("cadastro/listadepedidos");
		modelAndView.addObject("pedidos", compraRepository.findAll(PageRequest.of(0, 8)));
		return modelAndView;
	}

	
	
	
	

}
