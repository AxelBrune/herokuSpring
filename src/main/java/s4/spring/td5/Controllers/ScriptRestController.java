package s4.spring.td5.Controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import s4.spring.td5.entities.Recherche;
import s4.spring.td5.entities.Script;

@RestController
public class ScriptRestController {
	@Autowired
    private s4.spring.td5.repositories.ScriptRepository scriptRepository; 
	
	@PostMapping(value = "/rest/", consumes = "application/json", produces = "application/json")
	public ArrayList<Script> recupererListes(@RequestBody Recherche rech){
		String code = rech.getCode_recherche();
        String dem = rech.getDemande();
        switch (code) {
            case "titre":
                return scriptRepository.findScriptsByTitleContaining(dem);
            case "desc":
                return scriptRepository.findScriptsByDescriptionContaining(dem);
            case "content":
                return scriptRepository.findScriptsByContentContaining(dem);
            case "cat":
                return scriptRepository.findScriptsByCategoryNameContaining(dem);
            case "lang":
                return scriptRepository.findScriptsByLanguageNameContaining(dem);
            default:
                return null;
        }
	}
	
}
