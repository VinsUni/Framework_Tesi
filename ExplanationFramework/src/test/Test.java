package test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import operative.CoreNLPObj;
import operative.Dizionario;
import operative.Item;
import operative.Lettore;
import operative.Term;

/**
 * Class used for testing.
 * 
 * @author Pucariello Giovanni
 * @author Plantone Vincenzo
 *
 */
public class Test {

	/**
	 * Default private constructor.
	 */
	private Test() {
	}

	/**
	 * Main method for testing.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		// Creating input files
		File termsRel = new File("termRelevant.txt");
		File termsNotRel = new File("termNotRelevant.txt"); 
		File itemsBookRed = new File("list_books.txt");
		File recensioni = new File("recensioni_libri.txt"); 

		// Create a new Item identified by id
		Item item = new Item("I:1335");

		// Dictionary that contains relevant and not relevant terms
		Dizionario terms = new Dizionario();

		// Create a Lettore object that reads and stores data from input files
		Lettore lettore = new Lettore(item, terms);
		lettore.readTerms(termsNotRel);
		lettore.readTerms(termsRel);
		lettore.readTitle(itemsBookRed);
		lettore.readReviews(recensioni);

		// Create a CoreNLPObj
		CoreNLPObj o = new CoreNLPObj(terms, item);
		o.splitReviews(); // Extracting terms
		o.countAllTerms();
	 // o.countTermsRelevant();
		
		// Primi k termini con scoreIdf più alto
		ArrayList<Term> bestTerms = new ArrayList<>();
		
		// Tutti i termini estratti da o.countAllTerms()
		ArrayList<Term> usedForOrder = new ArrayList<>();

		usedForOrder.addAll(o.getListForTerm());

		// REMOVE TERM FROM TITLE
		String[] titolo = item.getTitle().split(" ");
		Iterator<Term> itTerm = usedForOrder.iterator();
		while (itTerm.hasNext()) {
			Term t = itTerm.next();
			for (String s : titolo) {
				if ((s.equalsIgnoreCase(t.getWordForm()) || (s.equalsIgnoreCase(t.getWordForm() + 's')))
						&& (!o.getTerms().getRelevantTerms().contains(t))) {
					itTerm.remove();
				}
			}
		}

	 // o.sortList(usedForOrder);

		/**
		 * Da completare: Ordinamento manuale
		 *
		Iterator<Term> itList = usedForOrder.iterator();
		for (int i = 0; i < 4; i++) {
			double max = 0;
			while (itList.hasNext()) {
				Term t = itList.next();
				if (t.getScoreIdf() > max) {
					max = t.getScoreIdf();
				}
			}
		}
		 */
		

		for (int i = 0; i < 5; i++) {
			bestTerms.add(usedForOrder.get(i));
		}

		for (Term t1 : usedForOrder) {
			t1.calculateTf(o.getReviewsSplitted());
			t1.calculateIdf(o.getReviewsSplitted());
			t1.calculateTfIdf();
			t1.calculateFinalScore();
			System.out.println(" Termine: " + t1.getWordForm() + "\n" + " Score: " + t1.getScore() + "\n"
					+ " Numero di frasi positive: " + t1.getCounterP() + " - " + " Numero di frasi very positive: "
					+ t1.getCounterVP() + "\n--------------------------------------");
		}

		System.out.println("Lista di tutti i termini: \n\n");

		Iterator<Term> it = usedForOrder.iterator();
		while (it.hasNext()) {
			Term t1 = it.next();
			double tf = t1.calculateTf(o.getReviewsSplitted());
			double idf = t1.calculateIdf(o.getReviewsSplitted());
			double tfIdf = t1.calculateTfIdf();
			double scoreFinale = t1.calculateFinalScore();
			String tfFormat = String.format("%.5f", tf);
			String idfFormat = String.format("%.5f", idf);
			String tfIdfFormat = String.format("%.5f", tfIdf);
			String scoreFinaleFormat = String.format("%.5f", scoreFinale);

			System.out.println("Termine: " + t1.getWordForm() + " Score Iniziale: " + t1.getScore() + " Tf: " + tfFormat
					+ "\n" + " Idf: " + idfFormat + " TfIdf: " + tfIdfFormat + " Score TfIdf: " + scoreFinaleFormat
					+ "\n--------------------------------------");
		}

		System.out.println("Lista dei termini più importanti : \n\n");

		
		
		Set<Term> hset = new HashSet<>(bestTerms);
	 // Iterator<Term> iter = bestTerms.iterator();
		Iterator<Term> iter = hset.iterator();
		Term t1 = null;
		while (iter.hasNext()) {
			t1 = iter.next();
			double tf = t1.calculateTf(o.getReviewsSplitted());
			double idf = t1.calculateIdf(o.getReviewsSplitted());
			double tfIdf = t1.calculateTfIdf();
			double scoreFinale = t1.calculateFinalScore();
			String tfFormat = String.format("%.5f", tf);
			String idfFormat = String.format("%.5f", idf);
			String tfIdfFormat = String.format("%.5f", tfIdf);
			String scoreFinaleFormat = String.format("%.5f", scoreFinale);

			System.out.println("Termine: " + t1.getWordForm() + ", Score Iniziale: " + t1.getScore() + ", Tf: " + tfFormat
					+ "\n" + " Idf: " + idfFormat + ", TfIdf: " + tfIdfFormat + ", Score TfIdf: " + scoreFinaleFormat
					+ "\n--------------------------------------");
		}
		 
		

		// Print item's title
		System.out.println("\n Titolo Item: " + item.getTitle());

		// Print a list of relevant terms
		System.out.println("Term Relevant: ------->" + terms.getRelevantTerms().toString());

		// Print a list of not relevant terms
		System.out.println("Term Not Relevant: ------->" + terms.getNotRelevantTerms().toString());

		// Print the number of reviews
		System.out.println("Num of reviews:-------->" + item.getReviewsFilteredById().size());

		// Print the number of reviews splitted
		System.out.println("Num of splitted reviews:-------->" + o.getReviewsSplitted().size());

		// PROVA PER VEDERE COSA C'Ã¨ INSIDE

		for (String s : o.getPositivePhrases()) {
			System.out.println(s + "\n\n");
		}

		System.out.println("FRASI VERY POSITIVE : \n\n");

		for (String s : o.getVeryPositivePhrases()) {
			System.out.println(s + "\n\n");
		}
	}

}
