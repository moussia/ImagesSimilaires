import java.util.Arrays;

import fr.unistra.pelican.Image;

public class Filtres {
	
	// Filtre Median pour débruiter
	public static Image filtreMedian(Image img, int canal){
		int largeur = img.getXDim();
		int hauteur = img.getYDim();
		int x1 = 0;
		int y1 = 0;
		int[] tab = new int[10];
		int median = 0;

		Image img2 = img;

		for(int y = 0; y < hauteur; ++y){
			for (int x = 0; x < largeur; ++x){
				if(x == 0)
					x1 = 1;
				else if (x + 1 == largeur)
					x1 = largeur - 2;
				else
					x1 = x;
				if(y == 0)
					y1 = 1;
				else if (y + 1 == hauteur)
					y1 = hauteur - 2;
				else
					y1 = y;

				tab[0] = img.getPixelXYBByte(x1, y1, canal);
				tab[1] = img.getPixelXYBByte(x1 + 1, y1, canal);
				tab[2] = img.getPixelXYBByte(x1, y1 + 1, canal);
				tab[3] = img.getPixelXYBByte(x1 - 1, y1, canal);
				tab[4] = img.getPixelXYBByte(x1, y1 - 1, canal);
				tab[5] = img.getPixelXYBByte(x1 + 1, y1 + 1, canal);
				tab[6] = img.getPixelXYBByte(x1 - 1, y1 - 1, canal);
				tab[7] = img.getPixelXYBByte(x1 - 1, y1 + 1, canal);
				tab[8] = img.getPixelXYBByte(x1 + 1, y1 - 1, canal);

				Arrays.sort(tab);
				median = tab[4];
				img2.setPixelXYBByte(x, y, canal, median);
			}
		}
		return img2;
	}
}
