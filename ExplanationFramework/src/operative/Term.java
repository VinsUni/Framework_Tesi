package operative;

import java.util.List;

/**
 * This class is used to represent terms and store their attributes.
 * 
 * CLASSE DA MIGLIORARE (NON DEFINITIVA)
 * 
 * @author Pucariello Giovanni
 * @author Plantone Vincenzo
 *
 */
public class Term implements Comparable<Term> {

	/*
	 * Enum class that represent the relevance of a Term.
	 */
	public enum Relevance {
		Relevant, NotRelevant
	}

	/**
	 * Get method that return the relevance.
	 * 
	 * @return relevance
	 */
	public Relevance getRelevance() {
		return relevance;
	}

	/**
	 * Set method that modifies the relevance.
	 * 
	 * @param relevance
	 */
	public void setRelevance(Relevance relevance) {
		this.relevance = relevance;
	}

	// Declaration variables
	private Relevance relevance;
	private String wordForm;
	private int counterP;
	private int counterVP;
	private double tf;
	private double idf;
	private double tfIdf;
	private int score;
	private double scoreIdf;

	/**
	 * Class constructor with a parameter.
	 * 
	 * @param wordform
	 */
	public Term(String wordform) {
		this(wordform, null);
		counterP = 0;
		counterVP = 0;
		tf = 0;
		idf = 0;
		tfIdf = 0;
		scoreIdf = 0;
	}

	/**
	 * Class constructor.
	 * 
	 * @param wordForm
	 * @param relevance
	 */
	public Term(String wordForm, Relevance relevance) {
		this.wordForm = wordForm;
		this.relevance = relevance;
		tf = 0;
		idf = 0;
		score = 0;
		tfIdf = 0;
		scoreIdf = 0;
	}

	/**
	 * Get method that return a string that represent the Term.
	 * 
	 * @return wordForm
	 */
	public String getWordForm() {
		return wordForm;
	}

	/**
	 * Get method that return the tf.
	 * 
	 * @return tf
	 */
	public double getTf() {
		return tf;
	}

	/**
	 * Get method that return the idf.
	 * 
	 * @return idf
	 */
	public double getIdf() {
		return idf;
	}

	/**
	 * Get method that return the tfIdf.
	 * 
	 * @return tfIdf
	 */
	public double getTfIdf() {
		return tfIdf;
	}

	/**
	 * Get method that return the score*idf.
	 * 
	 * @return scoreIdf
	 */
	public double getScoreIdf() {
		return scoreIdf;
	}

	/**
	 * Get method that return the score.
	 * 
	 * @return score
	 */
	public int getScore() {
		return this.score;
	}

	/**
	 * Get method that return the counterP.
	 * 
	 * @return counterP
	 */
	public int getCounterP() {
		return counterP;
	}

	/**
	 * Get method that return the counterVP.
	 * 
	 * @return counterVP
	 */
	public int getCounterVP() {
		return counterVP;
	}

	/**
	 * This method calculates the Idf (Inverse Document Frequency).
	 * 
	 * @param reader
	 * @return idf
	 */
	public double calculateIdf(List<String> docs) {
		double n = 0;
		int numDocs = docs.size();
		for (String phrase : docs) {
			String[] splitter = phrase.split(" ");
			for (String s : splitter) {
				if (this.getWordForm().equals(s)) {
					n++;
					break;
				}
			}
		}
		idf = Math.log(numDocs / n);
		return idf;
	}

	/**
	 * This method calculates the Tf (Term Frequency).
	 * 
	 * @param reader
	 * @return tf
	 */
	public double calculateTf(List<String> docs) {
		double result = 0;
		for (String phrase : docs) {
			String[] splitter = phrase.split(" ");
			for (String s : splitter) {
				if (this.getWordForm().equals(s))
					result++;
			}
		}
		tf = result / docs.size();
		return tf;
	}

	/**
	 * This method calculates the Tf-Idf (Term Frequency-Inverse Document
	 * Frequency).
	 * 
	 * @return tfIdf
	 */
	public double calculateTfIdf() {
		tfIdf = tf * idf;
		return tfIdf;
	}

	/**
	 * This method calculates the score of each terms.
	 * 
	 * @return scoreIdf
	 */
	public double calculateFinalScore() {
		scoreIdf = score * tfIdf;
		return scoreIdf;
	}

	/**
	 * This method is used to increment the score of each term.
	 * 
	 * @param n
	 */
	public void countScore(int n) {
		this.score = score + n;
	}

	/**
	 * This method is used to increment counterP.
	 */
	public void increseCounterP() {
		this.counterP++;
	}

	/**
	 * This method is used to increment counterVP.
	 */
	public void increseCounterVP() {
		this.counterVP++;
	}

	/**
	 * Override of hashCode() method
	 * 
	 * @return result
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((wordForm == null) ? 0 : wordForm.hashCode());
		return result;
	}

	/**
	 * Override of equals() method
	 * 
	 * @param obj
	 * @return boolean
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Term other = (Term) obj;
		if (wordForm == null) {
			if (other.wordForm != null)
				return false;
		} else if (!wordForm.equals(other.wordForm))
			return false;
		return true;
	}

	/**
	 * Override of compareTo() method that is used to sort the terms by a specific
	 * propriety.
	 * 
	 * @param o
	 * @return Integer
	 */
	@Override
	public int compareTo(Term o) {
		return -(int) (Math.round(this.calculateFinalScore()) - Math.round(o.calculateFinalScore()));
	}

	/**
	 * Override of toString() method that returns the Term in the form of a String.
	 * 
	 * @return Term.toString()
	 */
	@Override
	public String toString() {
		return "Term [Word Form= " + wordForm + "]";
	}

} // end Class
