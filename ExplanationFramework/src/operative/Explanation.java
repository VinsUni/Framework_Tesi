package operative;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

/**
 * 
 * @author Giovanni Pucariello
 * @author Vincenzo Plantone
 *
 */
public class Explanation {

	private CoreNLPObj coreObj;
	private ArrayList<Term> bestTerms;

	/**
	 * 
	 * @param coreObj
	 * @param bestTerms
	 */
	public Explanation(CoreNLPObj coreObj, ArrayList<Term> bestTerms) {
		this.coreObj = coreObj;
		this.bestTerms = bestTerms;
	}

	/**
	 * 
	 * @return mappaTermFrasi
	 */
	public HashMap<Term, ArrayList<String>> createMap() {

		HashMap<Term, ArrayList<String>> mappaTermFrasi = new HashMap<Term, ArrayList<String>>();

		Random rand = new Random();

		Iterator<String> iteratorPositive = coreObj.getPositivePhrases().iterator();

		Iterator<String> iteratorVeryPositive = coreObj.getVeryPositivePhrases().iterator();

		for (Term t : bestTerms) {
			mappaTermFrasi.put(t, new ArrayList<String>());
		}

		for (Term t : mappaTermFrasi.keySet()) {
			for (int i = 0; i < 5; i++) {
				switch (rand.nextInt(2)) {
				case 0:
					String sP = null;
					while (sP == null) {
						while (iteratorPositive.hasNext()) {
							String s = iteratorPositive.next();
							if (s.contains(t.getWordForm())) {
								sP = s;
								iteratorPositive.remove();
							}
						}
						mappaTermFrasi.get(t).add(sP);
					}
					break;
				case 1:
					String sVP = null;

					while (sVP == null) {
						while (iteratorVeryPositive.hasNext()) {
							String s = iteratorPositive.next();
							if (s.contains(t.getWordForm())) {
								sVP = s;
								iteratorVeryPositive.remove();
							}
						}
						mappaTermFrasi.get(t).add(sVP);
					}
					break;
				}
			}
		}
		return mappaTermFrasi;
	}

	
	
} // end class
