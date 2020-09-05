package old.util;

public class Testloader {
	
	public Testloader(){
		GenericLoadedObject<String> test = new GenericLoadedObject<String>();
		test.set("OMG");
		System.out.println(test.get());
		
		Test1 test_a = new Test1();
		Test1 test_a_still = GenericLoadedObject.anotherThing(test_a);
		System.out.println(test_a_still.getId());
		
		TestABC test_b_still = GenericLoadedObject.anotherThing(new TestABC());
		test_b_still.panic();
		System.out.println(test_b_still.getName());
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Testloader();
	}

}
