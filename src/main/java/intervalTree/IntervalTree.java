package intervalTree;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * An Interval Tree is essentially a map from intervals to objects, which
 * can be queried for all data associated with a particular interval of
 * time
 * @author Kevin Dolan
 *
 * @param <Type> the type of objects to associate
 */
public class IntervalTree<N extends Number & Comparable<N>, Type> {

	private IntervalNode<N, Type> head;
	private List<Interval<N, Type>> intervalList;
	private boolean inSync;
	private int size;
	private Supplier<N> supplier;
	
	/**
	 * Instantiate a new interval tree with no intervals
	 * @param supplier a lambda initializing a type N number to zero
	 */
	public IntervalTree(Supplier<N> supplier) {
		this.head = new IntervalNode<>(supplier);
		this.intervalList = new ArrayList<>();
		this.inSync = false;
		this.size = 0;
		this.supplier = supplier;
	}
	
	/**
	 * Instantiate an interval tree with a preset list of intervals
	 * @param intervalList the list of intervals to use
	 * @param supplier a lambda initializing a type N number to zero
	 */
	public IntervalTree(List<Interval<N, Type>> intervalList, Supplier<N> supplier) {

		for (Interval<N, Type> interval : intervalList) {
			if ( interval.getEnd().compareTo(interval.getStart()) >= 0) {
				throw new IllegalArgumentException("beginning of range must be less than end");
			}
		}

		this.head = new IntervalNode<>(intervalList, supplier);
		this.intervalList = new ArrayList<>();
		this.intervalList.addAll(intervalList);
		this.inSync = false;
		this.size = intervalList.size();
	}
	
	/**
	 * Perform a stabbing query, returning the associated data
	 * Will rebuild the tree if out of sync
	 * @param queryValue the number to stab
	 * @return	   the data associated with all intervals that contain queryValue
	 */
	public List<Type> get(N queryValue) {
		build();
		List<Interval<N, Type>> intervals = getIntervals(queryValue);
		List<Type> result = new ArrayList<>();
		for(Interval<N, Type> interval : intervals)
			result.add(interval.getData());
		return result;
	}
	
	/**
	 * Perform a stabbing query, returning the interval objects
	 * Will rebuild the tree if out of sync
	 * @param value the value to stab
	 * @return	   all intervals that contain value
	 */
	public List<Interval<N, Type>> getIntervals(N value) {
		build();
		return head.stab(value);
	}
	
	/**
	 * Perform an interval query, returning the associated data
	 * Will rebuild the tree if out of sync
	 * @param start the start of the interval to check
	 * @param end	the end of the interval to check
	 * @return	  	the data associated with all intervals that intersect target
	 */
	public List<Type> get(N start, N end) {
		if ( start.compareTo(end) >= 0) {
			throw new IllegalArgumentException("beginning of range must be less than end");
		}
		build();
		List<Interval<N, Type>> intervals = getIntervals(start, end);
		List<Type> result = new ArrayList<>();
		for(Interval<N, Type> interval : intervals)
			result.add(interval.getData());
		return result;
	}
	
	/**
	 * Perform an interval query, returning the interval objects
	 * Will rebuild the tree if out of sync
	 * @param start the start of the interval to check
	 * @param end	the end of the interval to check
	 * @return	  	all intervals that intersect target
	 */
	public List<Interval<N, Type>> getIntervals(N start, N end) {
		build();
		return head.query(new Interval<N, Type>(start, end, null));
	}
	
	/**
	 * Add an interval object to the interval tree's list
	 * Will not rebuild the tree until the next query or call to build
	 * @param interval the interval object to add
	 */
	public void addInterval(Interval<N, Type> interval) {
		if ( interval.getEnd().compareTo(interval.getStart()) >= 0) {
			inSync = false;
			intervalList.add(interval);
		} else {
			throw new IllegalArgumentException("beginning of range must be less than end");
		}
	}
	
	/**
	 * Add an interval object to the interval tree's list
	 * Will not rebuild the tree until the next query or call to build
	 * @param begin the beginning of the interval
	 * @param end	the end of the interval
	 * @param data	the data to associate
	 */
	public void addInterval(N begin, N end, Type data) {
		if (begin.compareTo(end) < 0) {
			inSync = false;
			intervalList.add(new Interval<>(begin, end, data));
		} else {
			throw new IllegalArgumentException("beginning of range must be less than end");
		}
	}
	
	/**
	 * Determine whether this interval tree is currently a reflection of all intervals in the interval list
	 * @return true if no changes have been made since the last build
	 */
	public boolean inSync() {
		return inSync;
	}
	
	/**
	 * Build the interval tree to reflect the list of intervals,
	 * Will not run if this is currently in sync
	 */
	private void build() {
		if(!inSync) {
			head = new IntervalNode<>(intervalList, supplier);
			inSync = true;
			size = intervalList.size();
		}
	}
	
	/**
	 * @return the number of entries in the currently built interval tree
	 */
	public int currentSize() {
		return size;
	}
	
	/**
	 * @return the number of entries in the interval list, equal to .size() if inSync()
	 */
	public int listSize() {
		return intervalList.size();
	}
	
	@Override
	public String toString() {
		return nodeString(head,0);
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		IntervalTree<?, ?> that = (IntervalTree<?, ?>) o;

		return inSync == that.inSync && size == that.size && (head != null ? head.equals(that.head) : that.head == null) && (intervalList != null ?
				intervalList.equals(that.intervalList) : that.intervalList == null);
	}


	@Override
	public int hashCode() {
		int result = head != null ? head.hashCode() : 0;
		result = 31 * result + (intervalList != null ? intervalList.hashCode() : 0);
		result = 31 * result + (inSync ? 1 : 0);
		result = 31 * result + size;
		return result;
	}


	private String nodeString(IntervalNode<N, Type> node, int level) {
		if(node == null)
			return "";
		
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < level; i++)
			sb.append("\t");
		sb.append(node).append("\n");
		sb.append(nodeString(node.getLeft(), level + 1));
		sb.append(nodeString(node.getRight(), level + 1));
		return sb.toString();
	}
}
