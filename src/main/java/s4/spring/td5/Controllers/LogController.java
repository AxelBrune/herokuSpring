package s4.spring.td5.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import s4.spring.td5.entities.User;
import s4.spring.td5.repositories.UserRepository;

import javax.persistence.EntityResult;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class LogController
{
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/index")
    public String log()
    {
    	initialisation();
       return "index";
    }

	private void initialisation() {
		if(this.userRepository.findAll().size()==0)
        {
            User user = new User("admin","admin","admin@spring.com","Admin");
            this.userRepository.saveAndFlush(user);
        }
	}
	
	@PostMapping("/login")
	public RedirectView login(@RequestParam String log,@RequestParam String mdp, HttpSession session) {
		List<User> allUsers = userRepository.findAll();
		User utilisateur = this.userRepository.findByLogin(log);
		
		if (utilisateur != null) {
			if (utilisateur.getPassword().equals(mdp)) {
				session.setAttribute("utilisateur", utilisateur);
				return new RedirectView("/script/index");
			}
			else {
				return new RedirectView("/index");
			}
		}
		else {
			return new RedirectView("/index");
		}
	}
	
	@GetMapping("/createaccount")
	public String createUser() {
		return "createuser";
	}
	
	@PostMapping("/createaccount")
	public RedirectView newUser(@RequestParam String login,@RequestParam String email,@RequestParam String  password,@RequestParam String identity) {
		if(login !=null && password != null && email != null && identity !=null) {
			User util = new User(login,password,email,identity);
			this.userRepository.saveAndFlush(util);
        }
		return new RedirectView("index");
    }
}
