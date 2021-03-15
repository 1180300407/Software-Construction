package P2;

public class Person {
	private final String name;
	private boolean visit;
	public Person(String name) {
		this.name=name;
	}
	
	public String getname() {
		String copyname=this.name;
		return copyname;
	}
	
	public void initvisit() {
		this.visit=false; 
	}
	
	public boolean getvisit() {
		return visit;
	}
	
	public void setvisit() {
		this.visit=true;
	}
}
