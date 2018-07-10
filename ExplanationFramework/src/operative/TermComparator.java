package operative;

import java.util.Comparator;

public class TermComparator implements Comparator<Term> {

	@Override
	public int compare(Term t1, Term t2) {
	    return t1.getScore()-t2.getScore();
	}


}
