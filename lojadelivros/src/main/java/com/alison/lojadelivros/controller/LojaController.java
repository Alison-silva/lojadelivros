package com.alison.lojadelivros.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.alison.lojadelivros.model.ItensCompra;
import com.alison.lojadelivros.model.Livro;
import com.alison.lojadelivros.repository.LivroRepository;

@Controller
public class LojaController {
	
	private List<ItensCompra> itensCompra = new ArrayList<ItensCompra>();

	@Autowired
	private LivroRepository livroRepository;

	@RequestMapping(method = RequestMethod.GET, value = "/lojadelivro")
	public ModelAndView inicio() {
		ModelAndView modelAndView = new ModelAndView("cadastro/lojadelivro");
		modelAndView.addObject("livroobj", new Livro());
		modelAndView.addObject("livros", livroRepository.findAll(PageRequest.of(0, 8, Sort.by("titulo"))));
		return modelAndView;
	}

	@GetMapping("/livropag")
	public ModelAndView livropag(@PageableDefault(size = 5) Pageable pageable, ModelAndView model) {

		Page<Livro> pageLivro = livroRepository.findAll(pageable);
		model.addObject("livros", pageLivro);
		model.addObject("livroobj", new Livro());
		model.setViewName("cadastro/lojadelivro");

		return model;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/lojalivros")
	public ModelAndView lojalivros() {
		ModelAndView andView = new ModelAndView("cadastro/lojadelivro");
		andView.addObject("livros", livroRepository.findAll(PageRequest.of(0, 8, Sort.by("titulo"))));
		return andView;
	}

	@GetMapping("/imagemloja/{idlivroloja}")
	public void imagemloja(@PathVariable("idlivroloja") Long id, HttpServletResponse response) throws IOException {
		response.setContentType("image/jpeg");

		Livro livro = livroRepository.findById(id).get();

		InputStream is = new ByteArrayInputStream(livro.getImage());
		IOUtils.copy(is, response.getOutputStream());
	}

	@GetMapping("/detalheslivro/{idlivro}")
	public ModelAndView detalheslivro(@PathVariable("idlivro") Long idlivro) {

		Livro livro = livroRepository.findById(idlivro).get();

		ModelAndView modelAndView = new ModelAndView("cadastro/detalheslivro");
		modelAndView.addObject("livroobj", livro);
		return modelAndView;
	}

	@GetMapping("/carrinho")
	public ModelAndView chamarCarrinho() {

		ModelAndView modelAndView = new ModelAndView("cadastro/carrinho");
		modelAndView.addObject("listaitens", itensCompra);
		return modelAndView;

	}
	
	@GetMapping("/alterarQuantidade/{id}/{acao}")
	public ModelAndView alterarQuantidade(@PathVariable Long id, @PathVariable Integer acao) {
		ModelAndView modelAndView = new ModelAndView("cadastro/carrinho");
		
		for(ItensCompra it : itensCompra) {
			if(it.getLivro().getId().equals(id)) {
				if(acao.equals(1)) {
					it.setQuantidade(it.getQuantidade() + 1);
				}else if(acao == 0) {
					it.setQuantidade(it.getQuantidade() - 1);
				}
				break;
			}
		}
		
		
		modelAndView.addObject("listaitens", itensCompra);
		return modelAndView;

	}
	
	@GetMapping("/addcarrinho/{idlivro}")
	public ModelAndView addcarrinho(@PathVariable Long idlivro) {
		ModelAndView modelAndView = new ModelAndView("cadastro/carrinho");
		
		Optional<Livro> li = livroRepository.findById(idlivro);
		Livro livro = li.get();
		
		int controle = 0;
		for(ItensCompra it : itensCompra) {
			if(it.getLivro().getId().equals(livro.getId())) {
				it.setQuantidade(it.getQuantidade() + 1);
				controle = 1;
				break;
			}
		}
		
		if(controle == 0) {
		ItensCompra item = new ItensCompra();
		item.setLivro(livro);
		item.setValorUnitario(livro.getPreco());
		item.setQuantidade(item.getQuantidade() + 1);
		item.setValorTotal(item.getQuantidade() * item.getValorUnitario());
		itensCompra.add(item);
		}
		
		modelAndView.addObject("listaitens", itensCompra);
		
		return modelAndView;

	}

}







