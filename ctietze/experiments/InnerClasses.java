package ctietze.experiments;

public class InnerClasses {
	public static void main(String[] args) {
		class Inner {
			class Innerer {
				private void dosth() {
					System.out.println("fu" + num());
				}
			}
			protected int num() { return 3; }
			private void output() {
				Innerer b = new Innerer();
				b.dosth();
				System.out.println("out");
			}
		}
		
		Inner a = new Inner();
		a.output();
	}
}
