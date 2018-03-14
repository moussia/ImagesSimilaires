import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import fr.unistra.pelican.Image;
import fr.unistra.pelican.algorithms.io.ImageLoader;
import fr.unistra.pelican.algorithms.visualisation.Viewer2D;

public class Appli {
	public static final int nbPhotoSimilaire = 10;
	public static final String path = "C:\\Users\\jeje\\workspace\\IA_Projet\\Images\\";
	public static final String pathHisto = path + "\\histogrammes\\";

	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Saissisez le path de l'image :");
		String pathImg = sc.nextLine(); //"C:\Users\jeje\workspace\IA_Projet\Images\001.jpg"

		Image img1 = lecteurImg(pathImg);

		// On verifie que l'image est en couleur
		if(!img1.color){
			System.out.println("L'image n'est pas en couleur");
			sc.close();
			return;
		}

		// On applique le traitement sur l'image qui sera comparé aux autres
		double histo[][] = traiter(pathImg);

		// On parcours toutes les images pour trouver celle qui sont similaire
		parcours(pathImg, histo);

		sc.close();
	}

	public static Image lecteurImg(String path){
		Image img = ImageLoader.exec(path);
		return img;
	}

	public static void afficher(Image img){
		Viewer2D.exec(img);
	}

	public static void parcours(String pathImg, double[][] histo1) throws IOException{
		Comparateur comparateur = new Comparateur();
		//creation de la treeMap avec un double en Key et un String en valeur
		TreeMap<Double, String> similaire = new TreeMap<Double, String>(comparateur);

		// Parcourir tout le dossier images pour les comparer
		File repImages = new File(path);
		String[] liste = repImages.list();
		if(liste == null)
			return;

		for(int i = 0; i < liste.length; ++i){
			String pathImg2 = path + repImages.list()[i];

			// Si le path de l'image vaut celle en parametre on passe a la suivante (ou si ce n'est pas une image
			if(pathImg.equals(pathImg2) || !estImage(pathImg2)){
				i++;
			}

			else if(BD.estDansLaBD(repImages.list()[i], pathHisto)){
				String pathH = pathHisto + repImages.list()[i] + ".txt";
				double histo2[][] = BD.lireHisto(pathH);
				double distance = comparer(histo1, histo2);
				similaire.put(distance, pathImg2);
			}
			else{
				// Sinon on le calcule 
				double histo2[][] = traiter(pathImg2);
				// On l'y met
				BD.stockerHisto(pathHisto, repImages.list()[i] + ".txt", histo2);
				double distance = comparer(histo1, histo2);
				similaire.put(distance, pathImg2);
			}
		}

		// On les affiches

		// Obtenir l'iterator pour parcourir la liste
		Set set = similaire.entrySet();
		Iterator it = set.iterator();

		// Afficher les pairs clé-valeur
		int i = 0;
		while(it.hasNext() && i < nbPhotoSimilaire) {
			Map.Entry<Double, String> mentry = (Map.Entry<Double, String>)it.next();
			System.out.print("clé: "+ mentry.getKey() + " - ");
			System.out.println("Valeur: "+ mentry.getValue());
			Viewer2D.exec(lecteurImg(mentry.getValue()));
			i++;
		} 
	}


	public static double[][] traiter(String pathImg) throws IOException {
		double histo[][] = new double[3][256];
		Image img = lecteurImg(pathImg);
		int largeur = img.getXDim();
		int hauteur = img.getYDim();

		//Filtre median sur les trois canaux
		img = Filtres.filtreMedian(img, 0);
		img = Filtres.filtreMedian(img, 1);
		img = Filtres.filtreMedian(img, 2);

		// Histogramme des trois canaux
		histo[0] = ModifHisto.histo(img, 0, "rouge");
		histo[1] = ModifHisto.histo(img, 1, "vert");
		histo[2] = ModifHisto.histo(img, 2, "bleu");

		// Discretisation
		double[][] histo2 = ModifHisto.discretiser(histo);

		// Normalisation
		histo2 = ModifHisto.normaliser(histo2, largeur * hauteur);
		return histo2;
	}

	private static boolean estImage(String pathImg) {
		if(pathImg.endsWith(".jpg"))
			return true;
		if(pathImg.endsWith(".jpeg"))
			return true;
		if(pathImg.endsWith(".png"))
			return true;
		else
			return false;
	}

	public static double comparer(double[][] histo1, double[][] histo2){
		double distance;
		distance = distance(histo1[0], histo2[0]);
		distance += distance(histo1[1], histo2[1]);
		distance += distance(histo1[2], histo2[2]);
		return distance;
	}

	private static double distance(double[] h1, double[] h2) {
		double distance = 0;
		for(int i = 0; i < h1.length; ++i){
			distance += (h1[i] - h2[i]) * (h1[i] - h2[i]);
		}
		// math.sqrt --> racine carré
		distance = Math.sqrt(distance);
		return distance;
	}
}