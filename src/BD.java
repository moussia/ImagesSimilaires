import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class BD {

	public static boolean estDansLaBD(String nomImage, String pathHisto){
		File repHistoBD = new File(pathHisto);
		String[] listeHisto = repHistoBD.list();
		if(listeHisto == null){
			return false;
		}
		for(int i = 0; i < listeHisto.length; ++i){
			if(repHistoBD.list()[i].equals(nomImage + ".txt")){
				System.out.println(nomImage + ".txt" + " == " + repHistoBD.list()[i]);
				return true;
			}
		}
		return false;
	}

	public static void stockerHisto(String pathRep, String pathImg, double[][]histo){
		File rep = new File(pathRep);
		if(!rep.exists()){
			rep.mkdirs();
		}
		sauvegardeDansFichier(pathRep + pathImg, histo[0]);
		sauvegardeDansFichier(pathRep + pathImg, histo[1]);
		sauvegardeDansFichier(pathRep + pathImg, histo[2]);
	}

	private static void sauvegardeDansFichier(String chemin, double[] histo) {
		BufferedWriter writer = null;

		try {
			File f = new File(chemin);
			writer = new BufferedWriter(new FileWriter(f, true));
			System.out.println(f.getCanonicalPath());
			for(int i = 0; i < histo.length; ++i){
				writer.write(arrondi(histo[i]) + "-");
			}
			writer.newLine();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
			}
		}
	}

	private static String arrondi(double d) {
		NumberFormat nf = new DecimalFormat("0.#####################");
		String s = nf.format(d);
		s = s.replace(',', '.');
		return s;
	}

	public static double[][] lireHisto(String path) throws IOException {
		File f = new File (path);
		double[][] histo = new double[3][10];
		String next = "";
		double val = 0.0;
		int i = 0;
		int numH = 0;
		int numVal = 0;
		try{
			FileReader fileReader = new FileReader (f);
			try{
				while((i = fileReader.read()) != -1){
					if((char)i != '-' && (char)i != '\n'){
						next += (char)i;
					}
					else if((char)i == '-'){
						System.out.println(next);
						val = Double.parseDouble(next);
						histo[numH][numVal] = val;
						next = "";
						
						numVal++;
					}
					else if((char)i == '\n'){
						numH++;
						numVal = 0;
					}
				}
				fileReader.read();

			}
			catch (IOException exception){
				System.out.println ("Erreur lors de la lecture : " + exception.getMessage());
			}
			fileReader.close();
		}
		catch (FileNotFoundException exception){
			System.out.println ("Fichier introuvable");
		}
		return histo;
	}
}
