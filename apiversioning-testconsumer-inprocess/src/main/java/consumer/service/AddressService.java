package consumer.service;

import consumer.api.Customer;

import consumer.api.Address;

public interface AddressService {
	
	public Address readAddressForCustomer(Customer customer);

}
