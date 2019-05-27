package menu;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

import javax.swing.JOptionPane;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import mode.DeuxJoueurs;
import mode.SoloChasseur;
import mode.SoloMonstre;

/**
 * Classe de Menu
 * Description des r�gles dans Readme.md
 * @author KOZLOV-PC
 *
 */
public class MenuConsole {
	
	//Chemin du scores.csv
	public static final String PATH_TO_CSV = "csv/scores.csv";
	
	//Taille du plateau
	public static int SIZE;
	
	//Mode de jeux | Description : Readme.md 
	public static boolean 
			modeBR = false, 
			debloquer = false, 
			monstreMange = false;
	
	//Pseudo des joueurs
	public static String pseudoJoueur1 = "", pseudoJoueur2 = "";
	
	/**
	 * Permet au joueur(s) de choisir un mode de jeux
	 * @param args - none
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		Object[] debut = { "Jouer" , "Meilleurs Scores", "Quitter"};
		
		int choixDebut;
		String[][] scores;
		String scoresAffiche;
		
		//Permet au joueur de choisir entre "Jouer" | "Meilleurs scores" | "Quitter" 
		do {
			choixDebut = JOptionPane.showOptionDialog(null, "", "Menu principal",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, debut, debut[0]);
			if(choixDebut == 1) {
				scores = getTop3Scores();
				scoresAffiche = "1. " + scores[0][0] + ", Points : " + scores[0][1] + ", " + scores[0][2] + "\n"
						+ "2. " + scores[1][0] + ", Points : " + scores[1][1] + ", " + scores[1][2] + "\n"
						+ "3. " + scores[2][0] + ", Points : " + scores[2][1] + ", " + scores[2][2] + "\n";
				JOptionPane.showMessageDialog( null, scoresAffiche, 
					      "Meilleurs scores", JOptionPane.QUESTION_MESSAGE);
			} else if(choixDebut == 2) {
				System.exit(0);
			}
		} while(choixDebut != 0);
		
		//Diff�rentes options pour les JOptionPane
		Object[] options = {"Solo : Monstre", "Solo : Chasseur"};
		Object[] reglesOptions = { "Oui","Non"};
		Object[] taille = {4,5,6,7,8,9,10};
		//Demande les pseudos
		pseudoJoueur1 = (String) JOptionPane.showInputDialog(null,"Pseudo du 1er joueur : ","Pseudo",JOptionPane.QUESTION_MESSAGE,null,null,"");
		pseudoJoueur2 = (String) JOptionPane.showInputDialog(null,"Pseudo du 2e joueur : (laisser vide si vous jouer en solo)","Pseudo",JOptionPane.QUESTION_MESSAGE,null,null,"");
		
		//Demande la taille du plateau
		Object actuel = null;
		while(actuel == null) {
			actuel = JOptionPane.showInputDialog(null,"Taille du plateau","Jeu",JOptionPane.QUESTION_MESSAGE,null,taille,5);
		}
		SIZE = Integer.valueOf(actuel.toString());
		
		//Demande pour mettre en place le mode battle royale
		modeBR = modeToBoolean(JOptionPane.showOptionDialog(null, "Mode battle royale :", "Regle optionnelle",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, reglesOptions, reglesOptions[1]));
		
		//Demande pour mettre en place le mode monstreTP
		debloquer = modeToBoolean(JOptionPane.showOptionDialog(null, "Teleporte le monstre s'il est bloque :", "Regle optionnelle",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, reglesOptions, reglesOptions[1]));
		
		//Demande pour mettre en place le mode monstre mange chasseur
		monstreMange = modeToBoolean(JOptionPane.showOptionDialog(null, "Le monstre peut manger le chasseur :", "Regle optionnelle",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, reglesOptions, reglesOptions[1]));
		
		//S'il y a 2 joueurs lance directement le jeu pour 2 joueurs sinon demande quel mode solo il veut jouer
		if(pseudoJoueur2.equals("")) {
			int choix = JOptionPane.showOptionDialog(null, "Choix du mode de jeu", "Menu principal",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			
			if(choix == 0) SoloMonstre.start();
			else SoloChasseur.start();
		} else DeuxJoueurs.start();
	}
	
	private static boolean modeToBoolean(int n) {
		return n==0;
	}

	private static String[][] getTop3Scores() throws IOException {
		String[][] scores;
		scores = new String[][] {
			{"A","0","A"},
			{"B","0","B"},
			{"C","0","C"}
		};
		CSVReader reader = new CSVReader(new FileReader(PATH_TO_CSV));
		List<String[]> s = reader.readAll();
		//Tri tous les scores pour avoir les 3 meilleurs
		for(int i = 0 ; i < s.size() ; i++) {
			if(StringToInt(s.get(i)[1]) > StringToInt(scores[0][1])) {
				scores[2] = scores[1];
				scores[1] = scores[0];
				scores[0] = s.get(i);
			} 
			else if(StringToInt(s.get(i)[1]) > StringToInt(scores[1][1])) {
				scores[2] = scores[1];
				scores[1] = s.get(i);
			} 
			else if(StringToInt(s.get(i)[1]) > StringToInt(scores[2][1])) {
				scores[2] = s.get(i);
			}
		}
		return scores;
	}
	
	private static int StringToInt(String s) {
		return Integer.parseInt(s);
	}
	
	/**
	 * Pour sauvegarder les scores dans le mode duo
	 * @param scoreMonstre int
	 * @param scoreChasseur int
	 * @throws IOException
	 */
	public static void saveScore(int scoreMonstre, int scoreChasseur) throws IOException {
		String[] score1 = {MenuConsole.pseudoJoueur1, String.valueOf(scoreMonstre), "Monstre"};
        String[] score2 = {MenuConsole.pseudoJoueur2, String.valueOf(scoreChasseur), "Chasseur"};
        CSVWriter writer = new CSVWriter(new FileWriter(MenuConsole.PATH_TO_CSV, true));

        writer.writeNext(score1);
        writer.writeNext(score2);

        writer.close();
	}
	
	/**
	 * Pour sauvegarder les scores dans un mode solo
	 * @param score int
	 * @param c 'M' pour monstre sinon chasseur
	 * @throws IOException
	 */
	public static void saveScore(int score, char c) throws IOException {
		String[] score1 = new String[3];
		
		if(c == 'M') {
			score1[0] = MenuConsole.pseudoJoueur1;
			score1[1] = String.valueOf(score);
			score1[2] = "Monstre";
		} else {
			score1[0] = MenuConsole.pseudoJoueur1;
			score1[1] = String.valueOf(score);
			score1[2] = "Chasseur";
		}
        
        CSVWriter writer = new CSVWriter(new FileWriter(MenuConsole.PATH_TO_CSV, true));

        writer.writeNext(score1);

        writer.close();
	}
}