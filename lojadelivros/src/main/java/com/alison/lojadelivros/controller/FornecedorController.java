package com.alison.lojadelivros.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alison.lojadelivros.model.Fornecedor;
import com.alison.lojadelivros.model.Telefone;
import com.alison.lojadelivros.repository.FornecedorRepository;
import com.alison.lojadelivros.repository.TelefoneRepository;

@Controller
public class FornecedorController {

	@Autowired
	private FornecedorRepository fornecedorRepository;
	
	@Autowired
	private TelefoneRepository telefoneRepository;

	@RequestMapping(method = RequestMethod.GET, value = "/listfornecedor")
	public ModelAndView inicio() {
		ModelAndView modelAndView = new ModelAndView("cadastro/listfornecedor");
		
		modelAndView.addObject("fornecedorobj", new Fornecedor());
		modelAndView.addObject("fornecedores", fornecedorRepository.findAll(PageRequest.of(0, 5, Sort.by("razaoSocial"))));
		return modelAndView;

	}

	@RequestMapping(method = RequestMethod.GET, value = "/cadastrofornecedor")
	public ModelAndView cadastro() {
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastrofornecedor");
		modelAndView.addObject("fornecedorobj", new Fornecedor());
		return modelAndView;
	}
	
	@GetMapping("/fornecedorpag")
	public ModelAndView carregaFornecedorPorPaginacao(@PageableDefault(size=5) Pageable pageable, 
			ModelAndView model, @RequestParam("razaopesquisa") String razaopesquisa) {
		
		Page<Fornecedor> pageFornecedor = fornecedorRepository.findFornecedorByNamePage(razaopesquisa, pageable);
		model.addObject("fornecedores", pageFornecedor);
		model.addObject("fornecedorobj", new Fornecedor());
		model.addObject("razaopesquisa", razaopesquisa);
		model.setViewName("cadastro/listfornecedor");
		
		return model;
	}
	
	

	@RequestMapping(method = RequestMethod.POST, value = "**/salvarfornecedor", consumes = { "multipart/form-data" })
	public ModelAndView salvar(@Valid Fornecedor fornecedor, BindingResult bindingResult, final MultipartFile file) throws IOException {
		
		fornecedor.setTelefones(telefoneRepository.getTelefonesFornecedor(fornecedor.getId()));
		
		if(bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView("cadastro/cadastrofornecedor");
			modelAndView.addObject("fornecedorobj", fornecedor);
			
			List<String> msg = new ArrayList<String>();
			for(ObjectError objectError : bindingResult.getAllErrors()) {
				msg.add(objectError.getDefaultMessage());
			}
			
			modelAndView.addObject("msg", msg);
			return modelAndView;
		}
		

		if (file.getSize() > 0) {
			fornecedor.setImagem(file.getBytes());
		} else {
			if (fornecedor.getId() != null && fornecedor.getId() > 0) {
				byte[] imageTemp = fornecedorRepository.findById(fornecedor.getId()).get().getImagem();
				fornecedor.setImagem(imageTemp);
			}
		}

		fornecedorRepository.save(fornecedor);

		ModelAndView andView = new ModelAndView("cadastro/listfornecedor");
		andView.addObject("fornecedorobj", new Fornecedor());
		andView.addObject("fornecedores", fornecedorRepository.findAll(PageRequest.of(0, 5, Sort.by("razaoSocial"))));
		return andView;
	}
	
	 @RequestMapping(method = RequestMethod.GET, value = "/listafornecedor")
	 public ModelAndView fornecedor() {
		ModelAndView andView = new ModelAndView("cadastro/listfornecedor");
		
		andView.addObject("fornecedorobj", new Fornecedor());
		andView.addObject("fornecedores", fornecedorRepository.findAll(PageRequest.of(0, 5, Sort.by("razaoSocial"))));
		
		return andView;
	} 
	 
	 
	 
	 @GetMapping("/imagemFornecedor/{idfornecedor}")
	 public void exibirImagemF(@PathVariable("idfornecedor") Long id, HttpServletResponse response) throws IOException {
		response.setContentType("image/jpeg");
		Fornecedor fornecedor = fornecedorRepository.findById(id).get();
		InputStream is = new ByteArrayInputStream(fornecedor.getImagem());
		IOUtils.copy(is, response.getOutputStream()); 
	}
	 
	 
	 @GetMapping("/editarfornecedor/{idfornecedor}")
	 public ModelAndView editar(@PathVariable("idfornecedor") Long idfornecedor) {
		
		Fornecedor fornecedor = fornecedorRepository.findById(idfornecedor).get();
		
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastrofornecedor");
		modelAndView.addObject("fornecedorobj", fornecedor);
		return modelAndView;
	}
	 
	 
	 @GetMapping("/removerfornecedor/{idfornecedor}")
	 public ModelAndView remover(@PathVariable("idfornecedor") Long idfornecedor) {
		
		fornecedorRepository.deleteById(idfornecedor);
		
		ModelAndView modelAndView = new ModelAndView("cadastro/listfornecedor");
		modelAndView.addObject("fornecedorobj", new Fornecedor());
		modelAndView.addObject("fornecedores", fornecedorRepository.findAll(PageRequest.of(0, 5, Sort.by("razaoSocial"))));
		return modelAndView;
	}
	
	@PostMapping("**/pesquisafornecedor")
	public ModelAndView pesquisar(@RequestParam("razaopesquisa") String razaopesquisa, 
			@PageableDefault(size = 5, sort = {"razaoSocial"}) Pageable pageable) {
		
		Page<Fornecedor> fornecedores = null;
		
		fornecedores = fornecedorRepository.findFornecedorByNamePage(razaopesquisa, pageable);
		
		ModelAndView modelAndView = new ModelAndView("cadastro/listfornecedor");
		
		modelAndView.addObject("fornecedores", fornecedores);
		modelAndView.addObject("fornecedorobj", new Fornecedor());
		modelAndView.addObject("razaopesquisa", razaopesquisa);
		
		return modelAndView;
	}
	 
	@GetMapping("/telefonesfornecedor/{idfornecedor}")
	public ModelAndView telefones(@PathVariable("idfornecedor") Long idfornecedor) {
		
		Fornecedor fornecedor = fornecedorRepository.findById(idfornecedor).get();
		
		ModelAndView modelAndView = new ModelAndView("cadastro/telefonesfornecedor");
		modelAndView.addObject("fornecedorobj", fornecedor);
		modelAndView.addObject("telefones", telefoneRepository.getTelefonesFornecedor(idfornecedor));
		return modelAndView;
	}
	
	@PostMapping("**/addfonefornecedor/{fornecedorid}")
	public ModelAndView addFoneFornecedor(Telefone telefone, 
			@PathVariable("fornecedorid") Long fornecedorid) {
		
		Fornecedor fornecedor = fornecedorRepository.findById(fornecedorid).get();
		
		if(telefone != null && telefone.getNumero().isEmpty() 
				|| telefone.getTipo().isEmpty()) {
			
			ModelAndView modelAndView = new ModelAndView("cadastro/telefonesfornecedor");
			modelAndView.addObject("fornecedorobj", fornecedor);
			modelAndView.addObject("telefones", telefoneRepository.getTelefonesFornecedor(fornecedorid));
			
			List<String> msg = new ArrayList<String>();
			if(telefone.getNumero().isEmpty()) {
				msg.add("Numero deve ser informado");
			}
			
			if(telefone.getTipo().isEmpty()) {
				msg.add("Tipo deve ser informado");
			}
			
			modelAndView.addObject("msg", msg);
			
			return modelAndView;
			
			
		}
		
		

		telefone.setFornecedor(fornecedor);
		
		telefoneRepository.save(telefone);
		
		ModelAndView modelAndView = new ModelAndView("cadastro/telefonesfornecedor");
		modelAndView.addObject("fornecedorobj", fornecedor);
		modelAndView.addObject("telefones", telefoneRepository.getTelefonesFornecedor(fornecedorid));
		return modelAndView;
	}
	
	@GetMapping("/removertelefonefornecedor/{idtelefone}")
	public ModelAndView removertelefone(@PathVariable("idtelefone") Long idtelefone) {
		
		Fornecedor fornecedor = telefoneRepository.findById(idtelefone).get().getFornecedor();
		
		telefoneRepository.deleteById(idtelefone);
		
		ModelAndView modelAndView = new ModelAndView("cadastro/telefonesfornecedor");
		modelAndView.addObject("fornecedorobj", fornecedor);
		modelAndView.addObject("telefones", telefoneRepository.getTelefonesFornecedor(fornecedor.getId()));
		return modelAndView;
	}

}












