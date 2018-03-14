import java.io.IOException;

import fr.unistra.pelican.Image;

public class ModifHisto {
	
	// Creer un histogramme a partir d'une image
	public static double[] histo(Image img, int canal, String nom) throws IOException {
		int largeur = img.getXDim();
		int hauteur = img.getYDim();
		double[] tab = new double[256];

		for(int i = 0; i < tab.length; ++i){
			tab[i] = 0;
		}
		for(int y = 0; y < hauteur; ++y){
			for (int x = 0; x < largeur; ++x){
				tab[img.getPixelXYBByte(x, y, canal)] += 1;
			}
		}
		HistogramTools.saveHistogram(tab, "C:\\Users\\jeje\\workspace\\IA_Projet\\Histogrammes\\Histogrammes\\" + nom + ".jpg");
		return tab;
	}
	
	
	// Discretise un histogramme à partir d'un autre histomgramme
	public static double[][] discretiser(double[][] histo) throws IOException {
		double histo2[][] = new double[3][10];
		int cpt = 0;
		
		for(int i = 0; i < 3; ++i){
			for(int j = 0; j < 10; ++j){
				histo2[i][j] = 0;
				cpt = j * 25;
				for(int k = cpt ; k < cpt + 25; ++k){
					histo2[i][j] += histo[i][k];
				}
			}
		}
		HistogramTools.saveHistogram(histo2[0], "C:\\Users\\jeje\\workspace\\IA_Projet\\Histogrammes\\HistogrammesDiscretises\\rouge.jpg");
		HistogramTools.saveHistogram(histo2[1], "C:\\Users\\jeje\\workspace\\IA_Projet\\Histogrammes\\HistogrammesDiscretises\\vert.jpg");
		HistogramTools.saveHistogram(histo2[2], "C:\\Users\\jeje\\workspace\\IA_Projet\\Histogrammes\\HistogrammesDiscretises\\bleu.jpg");
		return histo2;
	}
	
	
	// Normalise un histogramme à partir d'un autre histomgramme
	public static double[][] normaliser(double[][] histo, int nbPix) throws IOException{
		double histo2[][] = new double[histo.length][histo[0].length];
		
		for(int y = 0; y < histo.length; ++y){
			for (int x = 0; x < histo[y].length; ++x){
				histo2[y][x] = histo[y][x] / nbPix;
			}
		}
		HistogramTools.saveHistogram(histo2[0], "C:\\Users\\jeje\\workspace\\IA_Projet\\Histogrammes\\HistogrammesNormalises\\rouge.jpg");
		HistogramTools.saveHistogram(histo2[1], "C:\\Users\\jeje\\workspace\\IA_Projet\\Histogrammes\\HistogrammesNormalises\\vert.jpg");
		HistogramTools.saveHistogram(histo2[2], "C:\\Users\\jeje\\workspace\\IA_Projet\\Histogrammes\\HistogrammesNormalises\\bleu.jpg");
		return histo2;
	}
}
