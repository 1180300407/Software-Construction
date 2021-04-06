package P3;

public class Person {
	private String name;
	private boolean visit;
	public Person(String name) {
		this.name=name;
	}
	public String getname() {
		return name;
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
