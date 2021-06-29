/**
 * 
 */

/**
 * @author almog
 *
 */
public class Pair<U, V> {
	private final U first; // the first field of a pair
	private final V second; // the second field of a pair

	/**
	 * @param first
	 * @param second
	 */
	public Pair(U first, V second) {
		this.first = first;
		this.second = second;
	}

	/**
	 * @return the first
	 */
	public U getFirst() {
		return this.first;
	}

	/**
	 * @return the second
	 */
	public V getSecond() {
		return this.second;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.first == null) ? 0 : this.first.hashCode());
		result = prime * result + ((this.second == null) ? 0 : this.second.hashCode());
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
		Pair<?, ?> other = (Pair<?, ?>) obj;
		if (this.first == null) {
			if (other.first != null)
				return false;
		} else if (!this.first.equals(other.first))
			return false;
		if (this.second == null) {
			if (other.second != null)
				return false;
		} else if (!this.second.equals(other.second))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "(" + this.first + ", " + this.second + ")";
	}
}