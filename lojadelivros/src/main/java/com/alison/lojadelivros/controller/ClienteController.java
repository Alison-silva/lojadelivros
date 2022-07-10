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

import com.alison.lojadelivros.model.Cliente;
import com.alison.lojadelivros.model.Telefone;
import com.alison.lojadelivros.repository.ClienteRepository;
import com.alison.lojadelivros.repository.TelefoneRepository;


@Controller
public class ClienteController {

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private TelefoneRepository telefoneRepository;

	@RequestMapping(method = RequestMethod.GET, value = "/listclientes")
	public ModelAndView inicio() {
		ModelAndView modelAndView = new ModelAndView("cadastro/listclientes");
		modelAndView.addObject("clienteobj", new Cliente());
		modelAndView.addObject("clientes", clienteRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
		return modelAndView;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/cadastrocliente")
	public ModelAndView cadastro() {
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastrocliente");
		modelAndView.addObject("clienteobj", new Cliente());
		return modelAndView;
	}
	
	@GetMapping("/clientespag")
	public ModelAndView carregaClientePorPaginacao(@PageableDefault(size=5, sort = {"nome"}) Pageable pageable, 
			ModelAndView model, @RequestParam("nomepesquisa") String nomepesquisa) {
		
		Page<Cliente> pageCliente = clienteRepository.findClienteByNamePage(nomepesquisa, pageable);
		model.addObject("clientes", pageCliente);
		model.addObject("clienteobj", new Cliente());
		model.addObject("nomepesquisa", nomepesquisa);
		model.setViewName("cadastro/listclientes");
		
		return model;
	}
	
	

	@RequestMapping(method = RequestMethod.POST, value = "**/salvarcliente", consumes = {"multipart/form-data"})
	public ModelAndView salvar(@Valid Cliente cliente,  BindingResult bindingResult, final MultipartFile file) throws IOException {
		
		cliente.setTelefones(telefoneRepository.getTelefones(cliente.getId()));
		
		if(bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView("cadastro/cadastrocliente");
			modelAndView.addObject("clienteobj", cliente);
			
			List<String> msg = new ArrayList<String>();
			for(ObjectError objectError : bindingResult.getAllErrors()) {
				msg.add(objectError.getDefaultMessage());
			}
			
			modelAndView.addObject("msg", msg);
			return modelAndView;
		}
		
		
		
		if(file.getSize() > 0) {
			cliente.setImage(file.getBytes());
		}else {
			if(cliente.getId() != null && cliente.getId() > 0) {
				byte[] imageTemp = clienteRepository.
						findById(cliente.getId()).get().getImage();
				cliente.setImage(imageTemp);
			}
		}
		
		clienteRepository.save(cliente);
		
		ModelAndView andView = new ModelAndView("cadastro/listclientes");
		andView.addObject("clientes", clienteRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
		andView.addObject("clienteobj", new Cliente());
		return andView;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/listaclientes")
	public ModelAndView clientes() {
		
		ModelAndView andView = new ModelAndView("cadastro/listclientes");
		andView.addObject("clientes", clienteRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
		andView.addObject("clienteobj", new Cliente());
		return andView;
	}
	
	@GetMapping("/imagem/{idcliente}")
	public void exibirImagem(@PathVariable("idcliente") Long id, HttpServletResponse response) throws IOException {
		response.setContentType("image/jpeg");
		Cliente cliente = clienteRepository.findById(id).get();
		
		InputStream is = new ByteArrayInputStream(cliente.getImage());
		IOUtils.copy(is, response.getOutputStream());
	}
	
	//editarcliente
	@GetMapping("/editarcliente/{idcliente}")
	public ModelAndView editar(@PathVariable("idcliente") Long idcliente) {
		
		Cliente cliente = clienteRepository.findById(idcliente).get();
		
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastrocliente");
		modelAndView.addObject("clienteobj", cliente);
		return modelAndView;
	}
	
	@GetMapping("/removercliente/{idcliente}")
	public ModelAndView excluir(@PathVariable("idcliente") Long idcliente) {
		
		clienteRepository.deleteById(idcliente);
		
		ModelAndView modelAndView = new ModelAndView("cadastro/listclientes");
		modelAndView.addObject("clientes", clienteRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
		modelAndView.addObject("clienteobj", new Cliente());
		return modelAndView;
	}
	
	@PostMapping("**/pesquisarcliente")
	public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomepesquisa, 
			@PageableDefault(size = 5, sort = {"nome"}) Pageable pageable) {
		
		Page<Cliente> clientes = null;
		
		clientes = clienteRepository.findClienteByNamePage(nomepesquisa, pageable);
		
		ModelAndView modelAndView = new ModelAndView("cadastro/listclientes");
		modelAndView.addObject("clientes", clientes);
		modelAndView.addObject("clienteobj", new Cliente());
		modelAndView.addObject("nomepesquisa", nomepesquisa);
		
		return modelAndView;
	}

	@GetMapping("/telefones/{idcliente}")
	public ModelAndView telefones(@PathVariable("idcliente") Long idcliente) {
		
		Cliente cliente = clienteRepository.findById(idcliente).get();
		
		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
		modelAndView.addObject("clienteobj", cliente);
		modelAndView.addObject("telefones", telefoneRepository.getTelefones(idcliente));
		return modelAndView;
	}
	
	@PostMapping("**/addfonecliente/{clienteid}")
	public ModelAndView addFoneCliente(Telefone telefone, 
			@PathVariable("clienteid") Long clienteid) {
		
		Cliente cliente = clienteRepository.findById(clienteid).get();
		
		if(telefone != null && telefone.getNumero().isEmpty() 
				|| telefone.getTipo().isEmpty()) {
			
			ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
			modelAndView.addObject("clienteobj", cliente);
			modelAndView.addObject("telefones", telefoneRepository.getTelefones(clienteid));
			
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

		telefone.setCliente(cliente);
		
		telefoneRepository.save(telefone);
		
		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
		modelAndView.addObject("clienteobj", cliente);
		modelAndView.addObject("telefones", telefoneRepository.getTelefones(clienteid));
		return modelAndView;
	}
	
	@GetMapping("/removertelefone/{idtelefone}")
	public ModelAndView removertelefone(@PathVariable("idtelefone") Long idtelefone) {
		
		Cliente cliente = telefoneRepository.findById(idtelefone).get().getCliente();
		
		telefoneRepository.deleteById(idtelefone);
		
		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
		modelAndView.addObject("clienteobj", cliente);
		modelAndView.addObject("telefones", telefoneRepository.getTelefones(cliente.getId()));
		return modelAndView;
	}
	
}









