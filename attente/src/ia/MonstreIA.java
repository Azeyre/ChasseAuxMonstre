package ia;

/**
 * 
 * @author lantoing
 *
 */

public abstract class MonstreIA {
	
	/*
	 * Génère un nombre aléatoire correspondant a haut/bas/gauche/droite
	 * Vérifie si la case est bloquée, dans ce cas, génère un autre nombre aléatoire
	 * Se déplace
	 */	
	public static int move() {	
		return (int)(Math.random()*4);		
	}
}
