package consumer.service;

import consumer.api.Address;
import consumer.api.Customer;
import provider.services.PostalAddressService;
import provider.services.stubs.PostalAddressServiceStub;

import static apiversioning.typing.Cast.castTo;

public class AddressServiceImpl implements AddressService {

	private PostalAddressService delegateService = new PostalAddressServiceStub();
	
	@Override
	public Address readAddressForCustomer(Customer customer) {
		var address = this.delegateService.readPostalAddressForPerson(customer);
		return castTo(Address.class, address);
	}

}
