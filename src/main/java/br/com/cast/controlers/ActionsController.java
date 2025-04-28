package br.com.cast.controlers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ActionsController {

	@GetMapping("/creditar")
	public String credit() {
		return "actions/credit";
	}
	
	@GetMapping("/debitar")
	public String debit() {
		return "actions/debit";
	}
	
	@GetMapping("/transferencia")
	public String transfer() {
		return "actions/transfer";
	}
}
