package apiversioning.model;

import java.util.ArrayList;
import java.util.List;

public class Namespace {
	
	private final String name;
	
	private final List<Type> types = new ArrayList<>();
	
	public Namespace(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void addType(Type type) {
		this.types.add(type);
	}

}
