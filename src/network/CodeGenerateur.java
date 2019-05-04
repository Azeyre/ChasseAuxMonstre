package network;

public abstract class CodeGenerateur {
	
	public static int getValue(String s) {
		int resultat = 0;
		int longueur = s.length();
		for(int i = 0 ; i < longueur ; i++) {
			resultat += (s.charAt(i) - 'A') * (Math.pow(26, longueur - i - 1));
		}
		return resultat;
	}
	
	public static String getString(int number) {
		String res = "";
		int temp = number;
		int retenu = 0;
		char c;
		while(temp != 0) {
			retenu = temp % 26;
			temp = temp / 26;
			c = (char) ('A' + retenu);
			res = c + res;
		}
		if(res.length() == 1) res = "A" + res;
		return res;
	}

}
