package com.alison.lojadelivros.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

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

import com.alison.lojadelivros.model.Livro;
import com.alison.lojadelivros.repository.LivroRepository;

@Controller
public class LojaController {
	
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
	public ModelAndView livropag(@PageableDefault(size=5) Pageable pageable, ModelAndView model) {
		
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
	

	
}





