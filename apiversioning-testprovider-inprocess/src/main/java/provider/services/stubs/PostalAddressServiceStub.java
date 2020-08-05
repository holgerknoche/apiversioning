package provider.services.stubs;

import provider.api.Person;
import provider.services.PostalAddressService;
import provider.services.PostalAddressServiceImpl;

import static apiversioning.typing.Cast.castTo;

public class PostalAddressServiceStub implements PostalAddressService {

	private PostalAddressServiceImpl delegate = new PostalAddressServiceImpl();
	
	@Override
	public Object readPostalAddressForPerson(Object person) {
		var castPerson = castTo(Person.class, person);
		return this.delegate.readPostalAddressForPerson(castPerson);
	}

}
