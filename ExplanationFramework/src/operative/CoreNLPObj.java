package operative;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

/**
 * CLASSE DA MIGLIORARE (NON DEFINITIVA)
 * 
 * @author Pucariello Giovanni
 * @author Plantone Vincenzo
 *
 */
public class CoreNLPObj {

	private Properties props;
	private StanfordCoreNLP pipeline;

	private Dizionario terms;
	private List<String> reviews;
	private List<String> reviewsSplitted;
	private Set<Term> listForTerm;
	private List<String> positivePhrases;
	private List<String> veryPositivePhrases;

	/**
	 * Constructor class.
	 * 
	 * @param terms
	 * @param item
	 */
	public CoreNLPObj(Dizionario terms, Item item) {
		this.terms = terms;
		listForTerm = new HashSet<>();
		reviews = item.getReviewsFilteredById();
		positivePhrases = new ArrayList<String>();
		veryPositivePhrases = new ArrayList<String>();
		reviewsSplitted = new ArrayList<String>();
	}

	/**
	 * 
	 */
	public void splitReviews() {
		for (String s : reviews) {
			String[] parts = s.split("\\.");
			for (String split : parts) {
				reviewsSplitted.add(split);
			}
		}
	}
	
	/**
	 * creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER,
	 * parsing, and sentiment
	 */
	public void initialize() {
		props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, parse, lemma, sentiment");
		pipeline = new StanfordCoreNLP(props);
	}

	/**
	 * 
	 */
	public void countAllTerms() {
		initialize();
		for (String text : reviewsSplitted) {
			Annotation document = new Annotation(text);
			pipeline.annotate(document);
			List<CoreMap> sentences = document.get(SentencesAnnotation.class);
			for (CoreMap sentence : sentences) {
				String sentimentRecensione = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
				if (sentimentRecensione.equals("Positive")) {
					String phrase = sentence.get(TextAnnotation.class);
					positivePhrases.add(phrase);
					for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
						// this is the text of the token
						String lemma = token.get(LemmaAnnotation.class);
						String pos = token.get(PartOfSpeechAnnotation.class);
						if (pos.equals("NN") || pos.equals("NNS")) {
							Term t1 = new Term(lemma);
							if (!listForTerm.contains(t1)) {
								listForTerm.add(t1);
								t1.countScore(1);
								t1.increseCounterP();
							} else {
								Iterator<Term> it = listForTerm.iterator();
								while (it.hasNext()) {
									Term t2 = it.next();
									if (t2.equals(t1)) {
										t2.countScore(1);
										t2.increseCounterP();

									}
								}

							}

						}
					}
				} else if (sentimentRecensione.equals("Very positive")) {
					String phrase = sentence.get(TextAnnotation.class);
					veryPositivePhrases.add(phrase);
					for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
						// this is the text of the token
						String lemma = token.get(LemmaAnnotation.class);
						String pos = token.get(PartOfSpeechAnnotation.class);
						if (pos.equals("NN") || pos.equals("NNS")) {
							Term t1 = new Term(lemma);
							if (!listForTerm.contains(t1)) {
								listForTerm.add(t1);
								t1.countScore(3);
								t1.increseCounterP();
							} else {
								Iterator<Term> it = listForTerm.iterator();
								while (it.hasNext()) {
									Term t2 = it.next();
									if (t2.equals(t1)) {
										t2.countScore(3);
										t2.increseCounterP();
									}
								}

							}

						}
					}
				}
			}
		}
		removeSpecialCharacters(); // remove special characters (like a filter)
		
	} // end extractTerms() method
	
	/**
	 * Extraction of relevant and not relevant terms from all reviews of the item
	 */
	public void countTermsRelevant() {
		initialize();
		for (String text : reviewsSplitted) {
			Annotation document = new Annotation(text);
			pipeline.annotate(document);
			List<CoreMap> sentences = document.get(SentencesAnnotation.class);
			for (CoreMap sentence : sentences) {
				String sentimentRecensione = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
				if (sentimentRecensione.equals("Positive")) {
					for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
						// this is the text of the token
						String word = token.get(TextAnnotation.class);
						for (Term t : terms.getRelevantTerms()) {
							if (t.getWordForm().equals(word)) {
								Term t1 = new Term(word);
								if (listForTerm.contains(t1)) {
									Iterator<Term> it = listForTerm.iterator();
									while (it.hasNext()) {
										Term t2 = it.next();
										if (t2.equals(t1)) {
											t2.countScore(1);
											t2.increseCounterP();
										}
									}
								} else {
									listForTerm.add(t1);
									t1.countScore(1);
									t1.increseCounterP();
								}

							}

						}
					}
				} else if (sentimentRecensione.equals("Very positive")) {
					for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
						// this is the text of the token
						String word = token.get(TextAnnotation.class);
						for (Term t : terms.getRelevantTerms()) {
							if (t.getWordForm().equals(word)) {
								Term t1 = new Term(word);
								if (listForTerm.contains(t1)) {
									Iterator<Term> it = listForTerm.iterator();
									while (it.hasNext()) {
										Term t2 = it.next();
										if (t2.equals(t1)) {
											t2.countScore(3);
											t2.increseCounterVP();
										}
									}
								} else {
									listForTerm.add(t1);
									t1.countScore(3);
									t1.increseCounterVP();
								}

							}

						}
					}
				}
			}
		}

		// remove special characters (like a filter)
		removeSpecialCharacters();

	} // end extractTerms() method

	/**
	 * This method id used to filter and remove not-relevant terms into the
	 * listForTerm list (HashSet<Term>)
	 */
	public void removeSpecialCharacters() {
		Iterator<Term> it = listForTerm.iterator();
		Term t = null;
		while (it.hasNext()) {
			t = it.next();
			if (t.getWordForm().contains(".") || t.getWordForm().contains("%") || t.getWordForm().contains("'")
					|| t.getWordForm().contains("?") || t.getWordForm().contains("!") || t.getWordForm().contains("\"")
					|| t.getWordForm().contains("-") || t.getWordForm().contains("/")
					|| t.getWordForm().contains("_")) {
				it.remove();
			} else if (t.getWordForm().length() <= 2) {
				it.remove();
			} else if (t.getWordForm().matches(".*\\d+.*")) {
				it.remove();
			} else {
				if(terms.getNotRelevantTerms().contains(t)) {
					it.remove();
				}
			}
		}

	} // end removeSpecialCharacters() method

	/**
	 * This method id used to order listForTerm by term's score.
	 * 
	 */
	public void sortList(ArrayList<Term> term) {
		Collections.sort(term, new TermComparator());
		Collections.reverse(term);

	}

	public List<String> getPositivePhrases() {
		return positivePhrases;
	}

	public List<String> getVeryPositivePhrases() {
		return veryPositivePhrases;
	}

	/**
	 * It return the result of the text's sentiment
	 * 
	 * @param text
	 */
	public void getSentimentResult(String text) {
		if (text != null && text.length() > 0) {

			// run all Annotators on the text
			Annotation annotation = pipeline.process(text);

			for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
				// this is the parse tree of the current sentence
				Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);

				String sentimentType = sentence.get(SentimentCoreAnnotations.SentimentClass.class);

				double score = RNNCoreAnnotations.getPredictedClass(tree);
				System.out.println("\nSentiment score: " + score);
				System.out.println("Sentiment type: " + sentimentType);
			}

		}
	}

	/**
	 * 
	 * @return
	 */
	public Properties getProps() {
		return props;
	}

	/**
	 * 
	 * @return
	 */
	public StanfordCoreNLP getPipeline() {
		return pipeline;
	}

	/**
	 * 
	 * @return
	 */
	public Dizionario getTerms() {
		return terms;
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getReviews() {
		return reviews;
	}

	/**
	 * 
	 * @return
	 */
	public Set<Term> getListForTerm() {
		return listForTerm;
	}

	

	public List<String> getReviewsSplitted() {
		return reviewsSplitted;
	}

} // end Class
