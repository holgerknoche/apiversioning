package provider.services;

import provider.api.Person;
import provider.api.PostalAddress;
import provider.api.impl.CitySpecImpl;
import provider.api.impl.PostalAddressImpl;

public class PostalAddressServiceImpl {
	
	public PostalAddress readPostalAddressForPerson(Person person) {
		switch (person.getId()) {
		case 17:
			return new PostalAddressImpl("Test St.", 1245, new CitySpecImpl(4711, "Test City"));
			
		default:
			return new PostalAddressImpl("Demo St.", 42, new CitySpecImpl(4711, "Test City"));
		}
	}

}
