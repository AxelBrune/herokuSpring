package s4.spring.td5.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import io.github.jeemv.springboot.vuejs.VueJS;
import io.github.jeemv.springboot.vuejs.utilities.Http;
import s4.spring.td5.entities.*;
import s4.spring.td5.repositories.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


@Controller
public class ScriptsController
{
    @Autowired
    private ScriptRepository scriptRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private HistoryRepository historyRepository;
    
    @Autowired
    private VueJS vuejs;
    
    @GetMapping("script/index")
    public String getIndex(ModelMap model, HttpSession session) {
    	mettreLanguageSiBaseVide();
    	mettreCategoriesSiBaseVide();
    	User utilisateurRecu = (User) session.getAttribute("utilisateur");
    	if (utilisateurRecu !=null) {
    		utilisateurRecu=this.userRepository.findById(utilisateurRecu.getId());
    		List<Script> listeScripts = this.scriptRepository.findAll();
    		model.put("utilisateur", utilisateurRecu);
    		return "Script/index";
    	}
    	else {
    		return "indexError";
    	}
    }
    
    @PostMapping(value = {"/script/submit", "/script/submit/{id}"})
    public RedirectView newScript(@RequestParam String title, @RequestParam String description, @RequestParam String content,
    		@RequestParam String creationDate,HttpSession session,@RequestParam String language,@RequestParam String category,
    		@PathVariable(required = false) String id) {
    	Script script = id!=null?this.scriptRepository.findById(Integer.parseInt(id)):null;
		if (script == null) {
			script= new Script(title,description, content,creationDate);
		}
		else {
			script.setTitle(title);
			script.setDescription(description);
			script.setContent(content);
		}
		User util = (User) session.getAttribute("utilisateur");		
		User user=this.userRepository.findById(util.getId());
		List<Script> listeScripts = user.getScripts();
		Language lang=this.languageRepository.findByName(language);
		Category cat = this.categoryRepository.findByName(category);
		script.setLanguage(lang);
		script.setCategory(cat);
		script.setUser(user);
		user.setScripts(listeScripts);
		History h = null;
		if(script.getHistory()==null) {
			h=new History(creationDate,content,"");
			List<History> histories = new ArrayList<History>();
			histories.add(h);
			h.setScripts(script);
			script.setHistory(histories);
		}
		else {
			h = new History(creationDate,content,"");
			List<History> histories = script.getHistory();
			histories.add(h);
			script.setHistory(histories);
			h.setScripts(script);
		}
		this.historyRepository.saveAndFlush(h);
		this.userRepository.save(user);
		this.scriptRepository.saveAndFlush(script);
		
		
     	return new RedirectView("http://localhost:8091/script/index");
    }
    
    @GetMapping("script/script/new")
    public String newScript(ModelMap model, HttpSession session) {
    	model.put("language", this.languageRepository.findAll());
    	model.put("category", this.categoryRepository.findAll());
    	return "Script/new";
    }
    
    @GetMapping("script/{id}")
    public String editScript(ModelMap model, @PathVariable String id,HttpSession session) {
    	User utilisateur = (User) session.getAttribute("utilisateur");
    	if (utilisateur != null) {
    		Script s = this.scriptRepository.findById(Integer.parseInt(id));
    		model.put("language", this.languageRepository.findAll());
        	model.put("category", this.categoryRepository.findAll());
        	model.put("script",s);
        	return "Script/edit";
    	}
    	else {
    		return "index";
    	}
    }
    
    @GetMapping("search")
    public String search(ModelMap model) {
    	vuejs.addData("code_recherche");
        vuejs.addData("demande");
        //ArrayList<Script> liste = (ArrayList<Script>) this.scriptRepository.findAll();
        //JSONArray scripts = new JSONArray(liste);
        vuejs.addDataRaw("scripts", "[]");
        vuejs.addDataRaw("headers", "[{text:'Titre', value:'title'}, {text:'Description', value:'description'},{text:'Categorie', value:'category.name'}, {text:'Langage', value: 'language.name'}]");
        vuejs.addDataRaw("choices", "[{text:'Titre', value:'titre'},{text:'Langage', value: 'lang'}, {text:'Categorie', value: 'cat'},{text:'Description', value:'desc'},{text:'Contenu', value:'content'}]");
        vuejs.addDataRaw("selected", "[]");
        vuejs.addWatcher("selected", "this.headers = [];for(let i = 0; i < this.selected.length; i++){let tab=this.selected[i].split(';'); this.headers.push(JSON.parse('{\"text\":\"'+tab[0]+'\", \"value\":\"'+tab[1]+'\"}'))}");
        vuejs.addWatcher("demande","this.rechercher();" );
        vuejs.addWatcher("code_recherche","this.rechercher();" );
        vuejs.addMethod("test", "console.log(this.rechercher())");
        vuejs.addMethod("rechercher", "let self=this;"+Http.post("http://localhost:8091/rest/",(Object)"{'code_recherche': this.code_recherche, 'demande': this.demande}", "self.scripts = response.data;"));
        vuejs.addMethod("rechercheParCategorie", "this.demande=name; this.code_recherche='cat'", "name");
        vuejs.addMethod("rechercherParLanguage", "this.demade=name; this.code_recherche='lang'", "name");   
        model.put("vuejs",vuejs);
    	return "recherche";
    }
    
    public void mettreLanguageSiBaseVide() {
    	Language l1 = new Language("PHP");
    	Language l2 = new Language("Javascript");
    	Language l3 = new Language("Java");
    	
    	if (this.languageRepository.findAll().size()==0) {
    		this.languageRepository.save(l1);
    		this.languageRepository.save(l2);
    		this.languageRepository.save(l3);
    		this.languageRepository.flush();
    	}
    }
    public void mettreCategoriesSiBaseVide() {
    	Category c1 = new Category("Cat1");
    	Category c2 = new Category("Cat2");
    	Category c3 = new Category("Cat3");
    	
    	if(this.categoryRepository.findAll().size()==0) {
    		this.categoryRepository.save(c1);
    		this.categoryRepository.save(c2);
    		this.categoryRepository.save(c3);
    		this.categoryRepository.flush();
    	}
    }
   

    
}
