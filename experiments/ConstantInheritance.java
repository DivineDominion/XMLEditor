package experiments;

/*
 * shit :(
 */
public class ConstantInheritance {
	public static void main(String[] args) {
		class A {
			private static final String CONSTANT = "abc";	
			
			public A() {
				System.out.println(CONSTANT);
			}
		}
		
		class B extends A {
			private static final String CONSTANT = "def";
			
			public B() {
				super();
			}
		}
		
		A a = new A();
		B b = new B();
	}

}
