package br.com.caelum.contas.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.caelum.contas.dao.ContaDAO;
import br.com.caelum.contas.modelo.Conta;

@Controller
public class ContaController {

	private ContaDAO dao;

	@Autowired
	public ContaController(ContaDAO dao) {
		this.dao = dao;
	}

	@RequestMapping("/form")
	public String formulario() {
		return "formulario";
	}

	/*
	 * O parametro Conta � populado automaticamente pelo Spring MVC com as
	 * informa��es que est�o na tela. Para que isso ocorra � necess�rio que o
	 * nome dos campos do formul�rios sejam os mesmos dos atributos da classe.
	 */
	@RequestMapping("/adicionarConta")
	public String adiciona(@Valid Conta conta, BindingResult result) {
		// BindingResult � o par�metro em que o Spring MVC guarda o resultado
		// das valida��es.
		if (result.hasErrors()) {
			System.out.println("hasErrors: " + result.hasErrors());
			return "formulario";
		}

		/*
		 * Podemos validar se um dado foi preenchida da seguinte maneira, mas a
		 * partir do java 6 a Oracle criou a especifica��o do Bean Validation
		 * que serve para realizar a valida��o dos dados de nossas classes,
		 * baseada em anota��es. As valida��es s�o colocadas nas entidades da
		 * aplica��o e no controller � colocada a anota��o @Valid para indicar
		 * os objetos que possuem anota��es de valida��o.
		 * 
		 * if (conta.getDescricao() == null || conta.getDescricao().equals(""))
		 * { return "formulario"; }
		 */

		System.out.println("Conta adicionada �: " + conta.getDescricao());
		dao.adiciona(conta);

		return "conta/conta-adicionada";
	}

	@RequestMapping("/pagaConta")
	public void paga(Long id, HttpServletResponse response) {
		dao.paga(id);
		response.setStatus(200);

	}

	@RequestMapping("/removeConta")
	public String removeConta(Conta conta) {
		dao.remove(conta);
		return "redirect:listaContas";
	}

	@RequestMapping("/listaContas")
	public ModelAndView lista() {
		List<Conta> contas = dao.lista();

		ModelAndView mv = new ModelAndView("conta/lista");
		mv.addObject("contas", contas);
		return mv;
	}

	@RequestMapping("/mostraConta")
	public String mostra(Long id, Model model) {
		model.addAttribute("conta", dao.buscaPorId(id));
		return "conta/mostra";
	}

	@RequestMapping("/alteraConta")
	public String altera(Conta conta) {
		dao.altera(conta);
		return "redirect:listaContas";
	}

}
