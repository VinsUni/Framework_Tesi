package operative;

import java.util.ArrayList;
import java.util.Iterator;

import exceptions.EmptyListException;
import exceptions.ObjectAlreadyAddedException;
import exceptions.ReviewNotExistsException;

/**
 * This class is used to store informations about items.
 * 
 * @author Pucariello Giovanni
 * @author Plantone Vincenzo
 *
 */
public class Item implements Comparable<Item> {

	private String id;
	private String title;
	private ArrayList<String> reviewsFilteredById;

	/**
	 * Class constructor.
	 * 
	 * @param id
	 */
	public Item(String id) {
		this.id = id;
		title = null;
		reviewsFilteredById = new ArrayList<>();
	}

	/**
	 * Get method that returns a string that represent the title of the item.
	 * 
	 * @return title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set method that modifies the item's title.
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Get method that returns a list of reviews filtered by id.
	 * 
	 * @return reviewsFilteredById
	 */
	public ArrayList<String> getReviewsFilteredById() {
		return reviewsFilteredById;
	}

	/**
	 * Set method that modifies the item's list of reviews.
	 * 
	 * @param reviewsFilteredById
	 */
	public void setReviewsFilteredById(ArrayList<String> reviewsFilteredById) {
		this.reviewsFilteredById = reviewsFilteredById;
	}

	/**
	 * Get method that returns a string that represent the item's id
	 * 
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * This method adds a review.
	 * 
	 * @param review
	 * @throws ObjectAlreadyAddedException
	 */
	public void addReview(String review) throws ObjectAlreadyAddedException {
		if (reviewsFilteredById.contains(review)) {
			throw new ObjectAlreadyAddedException();
		} else {
			reviewsFilteredById.add(review);
		}

	}

	/**
	 * This method removes a review.
	 * 
	 * @param review
	 * @throws EmptyListException
	 * @throws ReviewNotExists
	 */
	public void removeReview(String review) throws EmptyListException, ReviewNotExistsException {
		if (reviewsFilteredById.isEmpty()) {
			throw new EmptyListException();
		} else if (reviewsFilteredById.contains(review)) {
			reviewsFilteredById.remove(review);
		} else {
			throw new ReviewNotExistsException();
		}
	}

	/**
	 * This method returns a listIterator of the reviews
	 * 
	 * @return reviewsFilteredById.listIterator();
	 */
	public Iterator<String> reviewsIterator() {
		return reviewsFilteredById.listIterator();
	}

	/**
	 * Override of toString() method
	 * 
	 * @return Item.toString()
	 */
	@Override
	public String toString() {
		return "Item [I: " + getId() + ",	Title:" + getTitle() + ",	Reviews:" + getReviewsFilteredById() + "]";
	}

	/**
	 * Override of compareTo() method
	 * 
	 * @param o
	 * @return a int value between -1, 0 and 1
	 */
	@Override
	public int compareTo(Item o) {
		return this.getId().compareTo(o.getId());
	}

} // end Class
