package old.util;

import java.util.Set;

public interface Bag<T> extends Set<T> {

	public abstract void add(T element, int quantity) throws BagException;
	
	public abstract void remove(Object element, int quantity) throws BagException;
	
	public abstract int quantity(T element) throws BagException;
}
