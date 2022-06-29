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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.alison.lojadelivros.model.Cliente;
import com.alison.lojadelivros.model.Compra;
import com.alison.lojadelivros.model.ItensCompra;
import com.alison.lojadelivros.model.Livro;
import com.alison.lojadelivros.repository.ClienteRepository;
import com.alison.lojadelivros.repository.CompraRepository;
import com.alison.lojadelivros.repository.ItensCompraRepository;
import com.alison.lojadelivros.repository.LivroRepository;

@Controller
public class LojaController {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	private List<ItensCompra> itensCompra = new ArrayList<ItensCompra>();
	
	private Compra compra = new Compra();
	

	@Autowired
	private LivroRepository livroRepository;
	
	@Autowired
	private ItensCompraRepository itensCompraRepository;
	
	@Autowired
	private CompraRepository compraRepository;
	
	@Autowired
	private ReportUtil reportUtil;
	
	private void calcularTotal() {
		compra.setValorTotal(0.);
		for(ItensCompra it: itensCompra) {
			compra.setValorTotal(compra.getValorTotal() + it.getValorTotal());
		}
	}
	
	

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
		calcularTotal();
		modelAndView.addObject("compra", compra);
		modelAndView.addObject("listaitens", itensCompra);
		modelAndView.addObject("clientes", clienteRepository.findAll());
		return modelAndView;

	}
	
	@GetMapping("/alterarQuantidade/{id}/{acao}")
	public String alterarQuantidade(@PathVariable Long id, @PathVariable Integer acao) {
	//	ModelAndView modelAndView = new ModelAndView("cadastro/carrinho");
		
		for(ItensCompra it : itensCompra) {
			if(it.getLivro().getId().equals(id)) {
				if(acao.equals(1)) {
					it.setQuantidade(it.getQuantidade() + 1);
					it.setValorTotal(0.);
					it.setValorTotal(it.getValorTotal() + (it.getQuantidade() * it.getValorUnitario()));
				}else if(acao == 0) {
					it.setQuantidade(it.getQuantidade() - 1);
					it.setValorTotal(0.);
					it.setValorTotal(it.getValorTotal() + (it.getQuantidade() * it.getValorUnitario()));
				}
				break;
			}
		}
		
		//modelAndView.addObject("listaitens", itensCompra);
		return "redirect:/carrinho";

	}
	
	@GetMapping("/removerProduto/{id}")
	public String removerProdutoCarrinho(@PathVariable Long id) {
	//	ModelAndView modelAndView = new ModelAndView("cadastro/carrinho");
		
		for(ItensCompra it : itensCompra) {
			if(it.getLivro().getId().equals(id)) {
				itensCompra.remove(it);
				break;
			}
		}
		
		
		//modelAndView.addObject("listaitens", itensCompra);
		return "redirect:/carrinho";

	}
	
	@GetMapping("/addcarrinho/{idlivro}")
	public String addcarrinho(@PathVariable Long idlivro) {
		//ModelAndView modelAndView = new ModelAndView("cadastro/carrinho");
		
		Optional<Livro> li = livroRepository.findById(idlivro);
		Livro livro = li.get();
		
		int controle = 0;
		for(ItensCompra it : itensCompra) {
			if(it.getLivro().getId().equals(livro.getId())) {
				it.setQuantidade(it.getQuantidade() + 1);
				it.setValorTotal(0.);
				it.setValorTotal(it.getValorTotal() + (it.getQuantidade() * it.getValorUnitario()));
				controle = 1;
				break;
			}
		}
		
		if(controle == 0) {
		ItensCompra item = new ItensCompra();
		item.setLivro(livro);
		item.setValorUnitario(livro.getPreco());
		item.setQuantidade(item.getQuantidade() + 1);
		item.setValorTotal(item.getValorTotal() + (item.getQuantidade() * item.getValorUnitario()));
		itensCompra.add(item);
		}
		
		//modelAndView.addObject("listaitens", itensCompra);
		
		return "redirect:/carrinho";

	}
	
	@PostMapping("/finalizarCompra")
	public ModelAndView finalizarCompra(Cliente cliente) {
		ModelAndView modelAndView = new ModelAndView("cadastro/comprarealizada");
		
		compra.setCliente(cliente);
		compraRepository.saveAndFlush(compra);
		
		for(ItensCompra c: itensCompra) {
			c.setCompra(compra);
			modelAndView.addObject("itempedido", c);
			itensCompraRepository.saveAndFlush(c);
		}
		
		itensCompra = new ArrayList<>();
		compra = new Compra();
		return modelAndView;
	}

	
	
	
}







