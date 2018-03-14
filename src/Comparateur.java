import java.util.Comparator;

public class Comparateur implements Comparator<Double>{

	@Override
	public int compare(Double d1, Double d2) {
		if(d1 < d2)
			return -1;
		else
			return 1;
	}

}
