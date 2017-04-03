package intervalTree;

/**
 * The Interval class maintains an interval with some associated data
 * @author Kevin Dolan
 * 
 * @param <Type> The type of data being stored
 */
public class Interval<N extends Number & Comparable<N>, Type> implements Comparable<Interval<N, Type>> {

	private N start;
	private N end;
	private Type data;
	
	public Interval(N start, N end, Type data) {
		this.start = start;
		this.end = end;
		this.data = data;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Interval<?, ?> other = (Interval<?, ?>) o;

		return (start != null ? start.equals(other.getStart()) : other.getStart() == null) && (end != null ? end.equals(other.getEnd()) :
				other.getEnd() == null) && (data != null ? data.equals(other.getData()) : other.getData() == null);
	}


	@Override
	public int hashCode() {
		int result = start != null ? start.hashCode() : 0;
		result = 31 * result + (end != null ? end.hashCode() : 0);
		result = 31 * result + (data != null ? data.hashCode() : 0);
		return result;
	}


	N getStart() {
		return start;
	}

	public void setStart(N start) {
		this.start = start;
	}

	N getEnd() {
		return end;
	}

	public void setEnd(N end) {
		this.end = end;
	}

	Type getData() {
		return data;
	}

	public void setData(Type data) {
		this.data = data;
	}
	
	/**
	 * @param queryValue
	 * @return	true if this interval contains queryValue (inclusive)
	 */
	boolean contains(N queryValue) {
		return queryValue.compareTo(end) <= 0 && queryValue.compareTo(start) >= 0;
	}
	
	/**
	 * @param other
	 * @return	return true if this interval intersects other
	 */
	boolean intersects(Interval<N, ?> other) {
		return other.getEnd().compareTo(start) >= 0 && other.getStart().compareTo(end) <= 0;
	}
	
	/**
	 * Return -1 if this interval's start time is less than the other, 1 if greater
	 * In the event of a tie, -1 if this interval's end time is less than the other, 1 if greater, 0 if same
	 * @param other
	 * @return 1 or -1
	 */
	@Override
	public int compareTo(Interval<N, Type> other) {

		int startComparison = start.compareTo(other.getStart());
		int endComparison = end.compareTo(other.getEnd());

		if (startComparison != 0) {
			return startComparison;
		} else {
			return endComparison;
		}
	}
	
}
