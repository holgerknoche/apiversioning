package consumer.api.impl;

import consumer.api.Customer;

public class CustomerImpl implements Customer {

	private final int id;
	
	private final String firstName;
	
	private final String lastName;
	
	public CustomerImpl(int id, String firstName, String lastName) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public String getFirstName() {
		return this.firstName;
	}

	@Override
	public String getLastName() {
		return this.lastName;
	}

}
