package com.alison.lojadelivros.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alison.lojadelivros.model.Cliente;
import com.alison.lojadelivros.repository.ClienteRepository;

@Controller
@RequestMapping(value = "/cliente")
public class IndexController {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@PostMapping(value = "/", produces = "application/json")
	public ResponseEntity<Cliente> cadastrar (@RequestBody Cliente cliente){
		
		Cliente clienteSalvo = clienteRepository.save(cliente);
		
		return new ResponseEntity<Cliente>(clienteSalvo, HttpStatus.OK);
	}
	
	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<List<Cliente>> clientes(){
		
		List<Cliente> list = clienteRepository.findAll();
		
		return new ResponseEntity<List<Cliente>>(list, HttpStatus.OK);
	}

}
