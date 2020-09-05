package old.util;

import java.util.LinkedList;

public class GenericLoadedObject<T> {
	// T stands for "Type"
    private T t;
    public void set(T t) { this.t = t; }
    public T get() { return t; }
    
	protected LinkedList<String> objects = new LinkedList<String>();
	public GenericLoadedObject(){
		
	}
	
	
	public GenericLoadedObject<T> loadSometing(GenericLoadedObject<T> thingToLoad){
		return thingToLoad;
	}
	
	
	public void doStuff(Class<? extends Test1> thing){
		return ;
	}
	
	public static <T extends Test1> T anotherThing(T actual){
		return actual;
	}
	
}
