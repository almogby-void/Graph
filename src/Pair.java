/**
 * 
 */

/**
 * @author almog
 *
 */
public class Pair {
	private final int first;
	private final int second;

	/**
	 * @param first
	 * @param second
	 */
	public Pair(int first, int second) {
		this.first = first;
		this.second = second;
	}

	/**
	 * @return the first
	 */
	public int getFirst() {
		return this.first;
	}

	/**
	 * @return the second
	 */
	public int getSecond() {
		return this.second;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.first;
		result = prime * result + this.second;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pair other = (Pair) obj;
		if (this.first != other.first)
			return false;
		if (this.second != other.second)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "(" + this.first + ", " + this.second + ")";
	}
}