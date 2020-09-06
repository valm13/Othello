package fr.isen.cir3.Othello.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.isen.cir3.Othello.domain.User;
import fr.isen.cir3.Othello.form.UserForm;
import fr.isen.cir3.Othello.repository.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository users;
	
	@GetMapping({"","/list"})
	public String list(Model model, @PageableDefault(page=0,size=10)Pageable pageable) {
		model.addAttribute("users", users.findAll(pageable));
		model.addAttribute("count", users.count());
		return "user-list";
	}
	
	@GetMapping({"/add", "edit/{id}"})
	public String add(@PathVariable(required=false) Long id, Model model) {
		UserForm form = new UserForm();
		model.addAttribute("user", form);
		
		if(id != null)
		{
			User c = users.findById(id).get();
			
			form.setId(c.getId());
			form.setName(c.getName());
			form.setFirstname(c.getFirstname());
			form.setEmail(c.getEmail());
			form.setPseudo(c.getPseudo());
		}
		return "user-add";
	}

	@PostMapping("/add")
	public String addForm(@Valid @ModelAttribute("user") UserForm form, BindingResult result, Model model) {

		if (result.hasErrors()) {
			model.addAttribute("user", form);
			return "user-add";
		}
		User u = new User();
		
		if(form.getId() != null)
			 u = users.findById(form.getId()).get();
			

		u.setName(form.getName());
		u.setFirstname(form.getFirstname());
		u.setEmail(form.getEmail());
		u.setPseudo(form.getPseudo());

		users.save(u);

		return "redirect:/user/list";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Long id) {
		if (users.existsById(id))
			users.deleteById(id);
		return "redirect:/user/list";
	}

}
