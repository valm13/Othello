package fr.isen.cir3.Othello.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

	@GetMapping("/")
	public String welcome(Model model) {
		
		return "redirect:/party/list";
	}
}