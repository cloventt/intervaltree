package intervalTree;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Supplier;

/**
 * The Node class contains the interval tree information for one single node
 * 
 * @author Kevin Dolan
 */
public class IntervalNode<N extends Number & Comparable<N>, Type> {

	private SortedMap<Interval<N, Type>, List<Interval<N, Type>>> intervals;
	private N center;
	private IntervalNode<N, Type> leftNode;
	private IntervalNode<N, Type> rightNode;
	private Supplier<N> supplier;
	
	IntervalNode(Supplier<N> supplier) {
		intervals = new TreeMap<>();
		this.supplier = supplier;
        center = supplier.get();
		leftNode = null;
		rightNode = null;
	}


    IntervalNode(List<Interval<N, Type>> intervalList, Supplier<N> supplier) {
		
		intervals = new TreeMap<>();
		this.supplier = supplier;
		
		SortedSet<N> endpoints = new TreeSet<>();
		
		for(Interval<N, Type> interval: intervalList) {
			endpoints.add(interval.getStart());
			endpoints.add(interval.getEnd());
		}
		
		N median = getMedian(endpoints);
		center = median;
		
		List<Interval<N, Type>> left = new ArrayList<>();
		List<Interval<N, Type>> right = new ArrayList<>();
		
		for(Interval<N, Type> interval : intervalList) {
			if(interval.getEnd().compareTo(median) == -1)
				left.add(interval);
			else if(interval.getStart().compareTo(median) == 1)
				right.add(interval);
			else {
                intervals.computeIfAbsent(interval,  l -> new ArrayList<>() );
				List<Interval<N, Type>> posting = intervals.get(interval);
				posting.add(interval);
			}
		}
		if(left.size() > 0)
			leftNode = new IntervalNode<>(left, supplier);
		if(right.size() > 0)
			rightNode = new IntervalNode<>(right, supplier);
	}

	/**
	 * Perform a stabbing query on the node
	 * @param queryValue the queryValue to query at
	 * @return	   all intervals containing queryValue
	 */
	List<Interval<N, Type>> stab(N queryValue) {
		List<Interval<N, Type>> result = new ArrayList<>();

		for(Entry<Interval<N, Type>, List<Interval<N, Type>>> entry : intervals.entrySet()) {
			if(entry.getKey().contains(queryValue))
			    result.addAll(entry.getValue());
			else if(entry.getKey().getStart().compareTo(queryValue) == 1)
				break;
		}

        if (queryValue.compareTo(center) == -1 && leftNode != null)
            result.addAll(leftNode.stab(queryValue));
        else if (queryValue.compareTo(center) == 1 && rightNode != null)
            result.addAll(rightNode.stab(queryValue));
		return result;
	}
	
	/**
	 * Perform an interval intersection query on the node
	 * @param target the interval to intersect
	 * @return		   all intervals containing time
	 */
	List<Interval<N, Type>> query(Interval<N, ?> target) {
		List<Interval<N, Type>> result = new ArrayList<>();
		
		for(Entry<Interval<N, Type>, List<Interval<N, Type>>> entry : intervals.entrySet()) {
			if(entry.getKey().intersects(target))
			    result.addAll(entry.getValue());
			else if(entry.getKey().getStart().compareTo(target.getEnd()) == 1)
				break;
		}
		
		if(target.getStart().compareTo(center) == -1 && leftNode != null)
			result.addAll(leftNode.query(target));
		if(target.getEnd().compareTo(center) == 1 && rightNode != null)
			result.addAll(rightNode.query(target));
		return result;
	}
	
	public N getCenter() {
		return center;
	}

	public void setCenter(N center) {
		this.center = center;
	}

	IntervalNode<N, Type> getLeft() {
		return leftNode;
	}

	public void setLeft(IntervalNode<N, Type> left) {
		this.leftNode = left;
	}

	IntervalNode<N, Type> getRight() {
		return rightNode;
	}

	public void setRight(IntervalNode<N, Type> right) {
		this.rightNode = right;
	}
	
	/**
	 * @param set the set to look on
	 * @return	  the median of the set, not interpolated
	 */
	private N getMedian(SortedSet<N> set) {
		int i = 0;
		int middle = set.size() / 2;
		for(N point : set) {
			if(i == middle)
				return point;
			i++;
		}
		return supplier.get();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(center).append(": ");
		for(Entry<Interval<N, Type>, List<Interval<N, Type>>> entry : intervals.entrySet()) {
			sb.append("[").append(entry.getKey().getStart()).append(",").append(entry.getKey().getEnd()).append("]:{");
			for(Interval<N, Type> interval : entry.getValue()) {
				sb.append("(").append(interval.getStart()).append(",").append(interval.getEnd()).append(",").append(interval.getData()).append(")");
			}
			sb.append("} ");
		}
		return sb.toString();
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IntervalNode<?, ?> that = (IntervalNode<?, ?>) o;

        return (intervals != null ? intervals.equals(that.intervals) : that.intervals == null) && (center != null ? center.equals(that.center) :
                that.center == null) && (leftNode != null ? leftNode.equals(that.leftNode) : that.leftNode == null) && (rightNode != null ?
                rightNode.equals(that.rightNode) : that.rightNode == null);
    }


    @Override
    public int hashCode() {
        int result = intervals != null ? intervals.hashCode() : 0;
        result = 31 * result + (center != null ? center.hashCode() : 0);
        result = 31 * result + (leftNode != null ? leftNode.hashCode() : 0);
        result = 31 * result + (rightNode != null ? rightNode.hashCode() : 0);
        return result;
    }
	
}
