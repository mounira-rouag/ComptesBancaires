package com.example.demo.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Entity.Compte;
import com.example.demo.Entity.Operation;
import com.example.demo.Service.BanqueService;

@Controller
public class BanqueController {

	@Autowired
	private BanqueService banqueService ;
	
	@RequestMapping("/operations")
	public String index(){
		return "comptes";
	}
	
	@RequestMapping("/consulterCompte")
	public String consulter(Model model, String codeCompte,
			@RequestParam(name="page", defaultValue="0") int page,
			@RequestParam(name="size", defaultValue="5") int size){
		model.addAttribute("codeCompte", codeCompte);
		try{
			Compte cp = banqueService.consulterCompte(codeCompte);
			model.addAttribute("compte", cp);
			Page<Operation> pageOperations= banqueService.listOperation(codeCompte, page, size);
			model.addAttribute("listOperations", pageOperations.getContent());
			int[] pages = new int [pageOperations.getTotalPages()];
			model.addAttribute("pages",pages);
		} catch (Exception e) {
			model.addAttribute("exception",e);
		}
		return "comptes";
	}
	
	@RequestMapping(value="/saveOperation", method=RequestMethod.POST)
	public String saveOperation(Model model, 
			String typeOperation, String codeCompte,
			double montant, String codeCompte2){
		
		try{
			if(typeOperation.equals("VERS")){
				banqueService.verser(codeCompte, montant);
			}
			else if(typeOperation.equals("RETR")){
				banqueService.retirer(codeCompte, montant);
			} 
			if (typeOperation.equals("VIR")){
				banqueService.virement(codeCompte, codeCompte2, montant);
			}
		} catch(Exception e) {
			model.addAttribute("error", e);
			return "redirect:/consulterCompte?codeCompte="+codeCompte+
					"&error="+e.getMessage();
		}
		
		return "redirect:/consulterCompte?codeCompte="+codeCompte;
	}
	
}
