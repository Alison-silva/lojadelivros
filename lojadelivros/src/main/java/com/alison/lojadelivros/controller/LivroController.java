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

import com.alison.lojadelivros.model.Livro;
import com.alison.lojadelivros.repository.FornecedorRepository;
import com.alison.lojadelivros.repository.LivroRepository;

@Controller
public class LivroController {

	@Autowired
	private LivroRepository livroRepository;

	@Autowired
	private FornecedorRepository fornecedorRepository;

	@RequestMapping(method = RequestMethod.GET, value = "/listlivro")
	public ModelAndView inicio() {
		ModelAndView modelAndView = new ModelAndView("cadastro/listlivro");
		modelAndView.addObject("livroobj", new Livro());
		modelAndView.addObject("livros", livroRepository.findAll(PageRequest.of(0, 5, Sort.by("titulo"))));
		return modelAndView;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/cadastrolivro")
	public ModelAndView cadastro() {
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastrolivro");
		modelAndView.addObject("fornecedores", fornecedorRepository.findAll());
		modelAndView.addObject("livroobj", new Livro());
		return modelAndView;
	}

	@RequestMapping(method = RequestMethod.POST, value = "**/salvarlivro", consumes = { "multipart/form-data" })
	public ModelAndView salvar(@Valid Livro livro, BindingResult bindingResult, final MultipartFile file)
			throws IOException {

		if (bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView("cadastro/cadastrolivro");
			modelAndView.addObject("livroobj", livro);
			modelAndView.addObject("fornecedores", fornecedorRepository.findAll());

			List<String> msg = new ArrayList<String>();
			for (ObjectError objectError : bindingResult.getAllErrors()) {
				msg.add(objectError.getDefaultMessage());
			}

			modelAndView.addObject("msg", msg);
			modelAndView.addObject("fornecedores", fornecedorRepository.findAll());
			return modelAndView;
		}

		if (file.getSize() > 0) {
			livro.setImage(file.getBytes());
		} else {
			if (livro.getId() != null && livro.getId() > 0) {
				byte[] imageTemp = livroRepository.findById(livro.getId()).get().getImage();
				livro.setImage(imageTemp);
			}
		}

		livroRepository.save(livro);
		ModelAndView andView = new ModelAndView("cadastro/listlivro");
		andView.addObject("livros", livroRepository.findAll(PageRequest.of(0, 5, Sort.by("titulo"))));
		andView.addObject("livroobj", new Livro());
		return andView;
	}

	@GetMapping("/livrospag")
	public ModelAndView carregaLivroPorPaginacao(@PageableDefault(size = 5) Pageable pageable, ModelAndView model,
			@RequestParam("titulopesquisa") String titulopesquisa) {

		Page<Livro> pageLivro = livroRepository.findLivroByNamePage(titulopesquisa, pageable);
		model.addObject("livros", pageLivro);
		model.addObject("livroobj", new Livro());
		model.addObject("titulopesquisa", titulopesquisa);
		model.setViewName("cadastro/listlivro");

		return model;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/listalivros")
	public ModelAndView livros() {

		ModelAndView andView = new ModelAndView("cadastro/listlivros");
		andView.addObject("livros", livroRepository.findAll(PageRequest.of(0, 5, Sort.by("titulo"))));
		andView.addObject("livroobj", new Livro());
		return andView;
	}

	@GetMapping("/image/{idlivro}")
	public void exibirImageLivro(@PathVariable("idlivro") Long id, HttpServletResponse response) throws IOException {
		response.setContentType("image/jpeg");

		Livro livro = livroRepository.findById(id).get();

		InputStream is = new ByteArrayInputStream(livro.getImage());
		IOUtils.copy(is, response.getOutputStream());
	}

	@GetMapping("/editarlivro/{idlivro}")
	public ModelAndView editar(@PathVariable("idlivro") Long idlivro) {

		Livro livro = livroRepository.findById(idlivro).get();

		ModelAndView modelAndView = new ModelAndView("cadastro/cadastrolivro");
		modelAndView.addObject("livroobj", livro);
		modelAndView.addObject("fornecedores", fornecedorRepository.findAll());
		return modelAndView;
	}

	@GetMapping("/removerlivro/{idlivro}")
	public ModelAndView excluir(@PathVariable("idlivro") Long idlivro) {

		livroRepository.deleteById(idlivro);

		ModelAndView modelAndView = new ModelAndView("cadastro/listlivro");
		modelAndView.addObject("livros", livroRepository.findAll(PageRequest.of(0, 5, Sort.by("titulo"))));
		modelAndView.addObject("livroobj", new Livro());
		return modelAndView;
	}

	@PostMapping("**/pesquisarlivro")
	public ModelAndView pesquisar(@RequestParam("titulopesquisa") String titulopesquisa,
			@PageableDefault(size = 5, sort = { "titulo" }) Pageable pageable) {

		Page<Livro> livros = null;

		livros = livroRepository.findLivroByNamePage(titulopesquisa, pageable);

		ModelAndView modelAndView = new ModelAndView("cadastro/listlivro");
		modelAndView.addObject("livros", livros);
		modelAndView.addObject("livroobj", new Livro());
		modelAndView.addObject("titulopesquisa", titulopesquisa);

		return modelAndView;
	}

}
