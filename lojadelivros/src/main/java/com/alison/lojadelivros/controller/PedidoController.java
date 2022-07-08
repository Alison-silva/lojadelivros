package com.alison.lojadelivros.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.alison.lojadelivros.repository.ItensCompraRepository;

@Controller
public class PedidoController {

	@Autowired
	private CompraRepository compraRepository;
	
	@Autowired
	private ItensCompraRepository itensCompraRepository;


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
	
	@RequestMapping(value = "/detalhespedido/{idpedido}")
	public ModelAndView teladetalhes(@PathVariable("idpedido") Long idpedido) {
		ModelAndView modelAndView = new ModelAndView("cadastro/detalhespedido");
		
		Compra compra = compraRepository.findById(idpedido).get();
	
		
		modelAndView.addObject("itens", itensCompraRepository.getItens(idpedido));
		modelAndView.addObject("compra", compra);
		
		return modelAndView;
	}
	
	@GetMapping("**/imprimirnota/{id}")
	public ModelAndView imprimirnota(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) {
		
		ModelAndView modelAndView = new ModelAndView("cadastro/listadepedidos");
		
		Compra compra = compraRepository.findById(id).get();
		
		modelAndView.addObject("itens", itensCompraRepository.getItens(id));
		modelAndView.addObject("compra", compra);
		
		Map<Object, Object> params = new HashMap<>();
		
		params.put("itens", itensCompraRepository.getItens(id));
		params.put("compra", compra);
		
	
		
		
		modelAndView.addObject("pedidos", compraRepository.findAll(PageRequest.of(0, 8)));
		return modelAndView;
	}
	


}





















