package csv.data.operations;

public class Baby {

	private char gender;
	private int totBabies;
	private int rank;
	/**
	 * @return the rank
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * @param rank the rank to set
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	/*
	 * @param gender
	 * @param totBabies
	 */
	public Baby(char gender, int totBabies, int rank) {
		super();
		this.gender = gender;
		this.totBabies = totBabies;
		this.rank = rank;
	}

	/**
	 * @return the gender
	 */
	public char getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(char gender) {
		this.gender = gender;
	}

	/**
	 * @return the totBabies
	 */
	public int getTotBabies() {
		return totBabies;
	}

	/**
	 * @param totBabies the totBabies to set
	 */
	public void setTotBabies(int totBabies) {
		this.totBabies = totBabies;
	}
	

}
