package com.alison.lojadelivros;



import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.alison.lojadelivros.model.Cliente;
import com.alison.lojadelivros.repository.ClienteRepository;

@SpringBootTest(classes = LojadelivrosApplication.class)
public class AppSpringTest {
	
	@Autowired
	private ClienteRepository clienteRepository;

	@Test
	public void teste() {

		Cliente cliente = new Cliente();
		cliente.setNome("Teste no teste");
		cliente.setCpf("55555555555");
		
		clienteRepository.save(cliente);
		
	}
	
	

}







