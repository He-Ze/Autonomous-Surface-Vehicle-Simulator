package old.util;

import java.util.HashMap;
import java.util.TreeSet;

public class SortedBag<T> extends TreeSet<T> implements Bag<T> {
	
	private static final long serialVersionUID = 616669567992347890L;
	private HashMap<Integer, Integer> map;
	
	public SortedBag() {
		map = new HashMap<Integer, Integer>();
	}

	@Override
	public void add(T element, int quantity) throws BagException {
		int hash;
		if (element == null) {
			throw new BagException();
		}
		if (quantity > 0) {
			hash = element.hashCode();
			if (map.containsKey(hash)) {
				quantity += map.get(hash);
			}
			map.put(hash, quantity);
			super.add(element);
		} else {
			throw new BagException();
		}
	}
	
	@Override
	public boolean add(T element) {
		try {
			add(element, 1);
			return true;
		} catch (BagException e) {
			return false;
		}
	}
	
	@Override
	public void remove(Object element, int quantity) throws BagException {
		int hash, value;
		if (element == null) {
			throw new BagException();
		}
		if (quantity > 0) {
			hash = element.hashCode();
			value = map.get(hash) - quantity;
			if (value <= 0) {
				super.remove(element);
				map.remove(hash);
			} else {
				map.put(hash, value);
			}
		} else {
			throw new BagException();
		}
	}
	
	@Override
	public boolean remove(Object element) {
		try {
			remove(element, 1);
			return true;
		} catch (BagException e) {
			return false;
		}
	}

	@Override
	public int quantity(T element) throws BagException {
		if (element == null) {
			throw new BagException();
		}
		
		return map.get(element.hashCode());
	}
}
