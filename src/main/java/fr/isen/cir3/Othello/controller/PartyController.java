package fr.isen.cir3.Othello.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

import fr.isen.cir3.Othello.Case;
import fr.isen.cir3.Othello.Point;
import fr.isen.cir3.Othello.Score;
import fr.isen.cir3.Othello.domain.Coup;
import fr.isen.cir3.Othello.domain.Party;
import fr.isen.cir3.Othello.domain.User;
import fr.isen.cir3.Othello.form.PartyForm;
import fr.isen.cir3.Othello.repository.PartyRepository;
import fr.isen.cir3.Othello.repository.UserRepository;

@Controller
@RequestMapping("/party")
public class PartyController {

	@Autowired
	private PartyRepository partys;

	@Autowired
	private UserRepository users;

	@GetMapping("list")
	public String list(Model model, @PageableDefault(page = 0, size = 10) Pageable pageable) {
		model.addAttribute("partys", partys.findAll(pageable));
		model.addAttribute("count", partys.count());
		return "party-list";
	}

	@GetMapping({ "/add", "" })
	public String add(Model model) {
		PartyForm form = new PartyForm();
		model.addAttribute("party", form);
		model.addAttribute("users", users.findAll());

		return "party-add";
	}

	@PostMapping("/add")
	public String addForm(@Valid @ModelAttribute("party") PartyForm form, BindingResult result, Model model) {
		Party p = new Party();
		
		if (result.hasErrors()) {
			model.addAttribute("party", form);
			model.addAttribute("users", users.findAll());
			return "party-add";
		}
		
		// Mon erreur : Impossible de créer une partie avec le même joueur
		if (form.getJoueur_blanc() == form.getJoueur_noir()) {
			model.addAttribute(form);
			model.addAttribute("users", users.findAll());
			model.addAttribute("erreur", "Il faut 2 joueurs différents pour créer une partie");
			return "party-add";
		}
		
		// Vérifie que les users existent
		if (form.getJoueur_blanc() != null && form.getJoueur_noir() != null) {
			Optional<User> user = users.findById(form.getJoueur_blanc());
			if (!user.isPresent()) {
				throw new RuntimeException("User white not found");
			}
			p.setJoueur_blanc(user.get());
			
			user = users.findById(form.getJoueur_noir());
			if (!user.isPresent()) {
				throw new RuntimeException("User black not found");
			}
			p.setJoueur_noir(user.get());	
		}
		
		int taille = form.getTaille().intValue();
		
		// Création de la grille
		ArrayList<ArrayList<Case>> grille;
		grille = new ArrayList<>(taille); // création des lignes
		for (int i = 0; i < taille; ++i) {
			grille.add(new ArrayList<>(taille));
		}
		
		// Initialise à 0
		for (int i = 0; i < taille; ++i) {
			for(int j = 0; j < taille; j++)
			{
				grille.get(i).add(j, new Case(i,j,0));
			}
		}
		
		// Pions centraux initiaux
		
		// Noirs
		grille.get(taille / 2 - 1).get(taille / 2 - 1).setValue(1);
		grille.get(taille / 2 ).get(taille / 2).setValue(1);
		// Blancs
		grille.get(taille / 2).get(taille / 2 - 1).setValue(2);;
		grille.get(taille / 2 - 1).get(taille / 2).setValue(2);;
		
		
		p.setGrille(grille);
		p.setName(form.getName());
		p.setEtat("Tour noir");
		p.setTaille(form.getTaille());
		partys.save(p);

		return "redirect:/party/list";
	}

	@GetMapping("/{id}/delete")
	public String delete(@PathVariable Long id) {
		if (partys.existsById(id))
			partys.deleteById(id);
		return "redirect:/party/list";
	}

	@GetMapping("/{id}/play")
	public String display(@PathVariable(required = true) Long id, Model model) {
		if (partys.existsById(id)) {
			Party p = partys.findById(id).get();
			Score s = getScore(p);
			model.addAttribute("score",s);
			model.addAttribute("party", p);
			return "party-play";
		} else
			return "redirect:/party/list";

	}
	
	@GetMapping("/{id}/play/{row}/{col}")
	public String play(@PathVariable(required = true) Long id, @PathVariable(required = true) int row, @PathVariable(required = true) int col, Model model) {
		
		if (partys.existsById(id))
		{
			Party p = partys.findById(id).get();
			if(p.getEtat().equals(new String("Tour noir")) || p.getEtat().equals(new String("Tour blanc")))
			{			
				int couleur_adverse = (p.getEtat().equals(new String("Tour noir"))) ? 2 : 1;
				int ma_couleur = (p.getEtat().equals(new String("Tour noir"))) ? 1 : 2;

				ArrayList<Case> casesAvecPions = getAvecPions(p);			
				ArrayList<Case> casesVidesAutourPions = getCasesVides(casesAvecPions, p);
				ArrayList<Case> legalMoves = getLegalMove(casesVidesAutourPions, p, couleur_adverse);

				
				if(legalMoves.contains(new Case(row,col,0)))
				{
					// On positionne le pion et on capture les cases
					p.getGrille().get(row).get(col).setValue(ma_couleur);
					capture(row,col,p,couleur_adverse);
					List<Coup> coups = p.getCoups();
					Coup coup = new Coup();
					coup.setCol((long) col);
					coup.setRow((long) row);
					if(p.getEtat().equals(new String("Tour blanc")))
						coup.setJoueur(p.getJoueur_blanc());
					else
						coup.setJoueur(p.getJoueur_noir());
					coups.add(coup);
					
					// Changement de joueur
					if(p.getEtat().equals(new String("Tour noir")))
						p.setEtat("Tour blanc");
					else if(p.getEtat().equals(new String("Tour blanc")))
						p.setEtat("Tour noir");
					
					// On refais l'opération avec l'autre joueur pour savoir ses coups possible
					casesAvecPions = getAvecPions(p);			
					casesVidesAutourPions = getCasesVides(casesAvecPions, p);
					legalMoves = getLegalMove(casesVidesAutourPions, p, ma_couleur);
					
					// Si il ne peut pas jouer, on regarde si le premier peut
					if(legalMoves.isEmpty())
					{
						// On rechange donc les joueurs
						if(p.getEtat().equals(new String("Tour noir")))
							p.setEtat("Tour blanc");
						else if(p.getEtat().equals(new String("Tour blanc")))
							p.setEtat("Tour noir");
						
						// Mais on verifie quand meme si le premier joueur peut encore jouer
						casesAvecPions = getAvecPions(p);			
						casesVidesAutourPions = getCasesVides(casesAvecPions, p);
						legalMoves = getLegalMove(casesVidesAutourPions, p, couleur_adverse);
						
						// Personne ne peut --> Fin de partie
						if(legalMoves.isEmpty())
						{
							Score s = getScore(p);
							if(s.getNoirs() == s.getBlancs())
								p.setEtat("Match nul");
							else if(s.getNoirs() > s.getBlancs())
								p.setEtat("Victoire noir");
							else
								p.setEtat("Victoire blanc");
							partys.save(p);
							model.addAttribute("score",s);
							model.addAttribute("party", p);
							return "redirect:/party/"+id+"/play";				
						}
					}
					
					partys.save(p);
					Score s = getScore(p);
					model.addAttribute("score",s);
					model.addAttribute("party", p);
					
					return "redirect:/party/"+id+"/play";
				}
					
				else
				{
					Score s = getScore(p);
					model.addAttribute("score",s);
					model.addAttribute("erreur","Le coup est impossible");
					model.addAttribute("party", p);
					return "party-play";
				}
			}
			// La partie est finie on affiche le score
			else
				return "redirect:/party/\"+id+\"/play";			
		} else
			return "redirect:/party/list";

	}

	// Working
	public ArrayList<Case> getAvecPions(Party p)
	{
		ArrayList<Case> casesPions = new ArrayList<>();
		for(ArrayList<Case> listeCases : p.getGrille())
		{
			for(Case Case : listeCases)
			{
				if(Case.getValue() == 1 || Case.getValue() == 2)
					casesPions.add(Case);
			}
		}
		return casesPions;
	}
	// Working
	public ArrayList<Case> getCasesVides(ArrayList<Case> casesPions, Party p)
	{
		Point[] ALL_DIRECTIONS = new Point[]{
	            new Point(1, 0),
	            new Point(1, 1),
	            new Point(0, 1),
	            new Point(-1, 1),
	            new Point(-1, 0),
	            new Point(-1, -1),
	            new Point(0, -1),
	            new Point(1, -1),
	    };
		
		ArrayList<Case> casesVides = new ArrayList<>();
		
			for(Case Case : casesPions)
			{
				for(Point point : ALL_DIRECTIONS)
				{
					int ActualRow = point.getRow() + Case.getRow();
					int ActualCol = point.getCol() + Case.getCol();
					// On vérifie que dans la direction actuelle la case existe dans la grille
					if(ActualRow >= 0 &&  ActualRow < p.getTaille())
						if(ActualCol >= 0 &&  ActualCol < p.getTaille())
							if(p.getGrille().get(ActualRow).get(ActualCol).getValue() == 0)
								if(!casesVides.contains(p.getGrille().get(ActualRow).get(ActualCol)))
									casesVides.add(p.getGrille().get(ActualRow).get(ActualCol));
				}
			}

		return casesVides;
	}
	

	public ArrayList<Case> getLegalMove(ArrayList<Case> casesVides, Party p, int couleur_adverse)
	{
		Point[] ALL_DIRECTIONS = new Point[]{
	            new Point(1, 0),
	            new Point(1, 1),
	            new Point(0, 1),
	            new Point(-1, 1),
	            new Point(-1, 0),
	            new Point(-1, -1),
	            new Point(0, -1),
	            new Point(1, -1),
	    };
		
		ArrayList<Case> legalMoves = new ArrayList<>();
		int maCouleur = (couleur_adverse == 1) ? 2 : 1 ;
		
		for(Case Case : casesVides)
		{
			for(Point ActualDirection : ALL_DIRECTIONS)
			{
				int ActualRow = ActualDirection.getRow() + Case.getRow();
				int ActualCol = ActualDirection.getCol() + Case.getCol();
				// Tant que les pions sont de la couleur adverse
				while(inGrille(ActualRow,ActualCol,p) && isCaseWithCouleur(ActualRow,ActualCol,p,couleur_adverse))
				{
					ActualRow += ActualDirection.getRow();
					ActualCol += ActualDirection.getCol();
				}
				if(inGrille(ActualRow,ActualCol,p))
				{
					// Si il finit par ma couleur et que le precedent est bien de couleur adverse
					if(isCaseWithCouleur(ActualRow,ActualCol,p,maCouleur) && isCaseWithCouleur(ActualRow-ActualDirection.getRow(),ActualCol-ActualDirection.getCol(),p,couleur_adverse))
					{
						if(!legalMoves.contains(Case))
							legalMoves.add(Case);							
					}
				}
				
					
			}
		}
		return legalMoves;
	}
	
	public void capture(int row, int col, Party p, int couleur_adverse) {
		Point[] ALL_DIRECTIONS = new Point[]{
	            new Point(1, 0),
	            new Point(1, 1),
	            new Point(0, 1),
	            new Point(-1, 1),
	            new Point(-1, 0),
	            new Point(-1, -1),
	            new Point(0, -1),
	            new Point(1, -1),
	    };
		int maCouleur = (couleur_adverse == 1) ? 2 : 1 ;
		
		for(Point ActualDirection : ALL_DIRECTIONS)
		{
			ArrayList<Case> pionsRetournes = new ArrayList<>();
			int ActualRow = ActualDirection.getRow() + row;
			int ActualCol = ActualDirection.getCol() + col;
			while(inGrille(ActualRow,ActualCol,p) && isCaseWithCouleur(ActualRow,ActualCol,p,couleur_adverse))
			{
				pionsRetournes.add(p.getGrille().get(ActualRow).get(ActualCol));
				ActualRow += ActualDirection.getRow();
				ActualCol += ActualDirection.getCol();
			}
			if(inGrille(ActualRow,ActualCol,p))
			{
				if(isCaseWithCouleur(ActualRow,ActualCol,p,maCouleur))
				{
					// On retourne tous les pions de la liste
					for(Case aRetourner : pionsRetournes)
					{
						aRetourner.setValue(maCouleur);
					}
						
				}
			}
			
			pionsRetournes.clear();
		}
		
	}
	
	public boolean inGrille(int row, int col, Party p)
	{
		if(row >= 0 && col >= 0 && row < p.getTaille() && col < p.getTaille())
			return true;
		else
			return false;
	}
	public boolean isCaseWithCouleur(int row, int col, Party p, int couleurAdverse)
	{
		if(p.getGrille().get(row).get(col).getValue() == couleurAdverse)
			return true;
		else
			return false;
	}
	
	public int compteCaseCouleur(Party p, int couleur)
	{
		int cpt = 0;
		for(int row = 0; row < p.getTaille(); row++)
		{
			for(int col = 0; col < p.getTaille(); col++)
			{
				if(p.getGrille().get(row).get(col).getValue() == couleur)
					cpt++;
//				cpt += (p.getGrille().get(row).get(col).getValue() == couleur)? 1 : 0;
			}
		}
		
		return cpt;
	}
	public Score getScore(Party p)
	{
		int nbNoirs = compteCaseCouleur(p, 1);
		int nbBlancs = compteCaseCouleur(p, 2);
		Score s = new Score(nbNoirs,nbBlancs);
		return s;
	}
}
