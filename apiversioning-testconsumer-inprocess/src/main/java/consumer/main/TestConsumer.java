package consumer.main;

import consumer.api.Address;
import consumer.api.Customer;
import consumer.api.impl.CustomerImpl;
import consumer.service.AddressService;
import consumer.service.AddressServiceImpl;

public class TestConsumer {
	
	private AddressService addressService = new AddressServiceImpl();
	
	public static void main(String[] arguments) {
		new TestConsumer().runTest();
	}
	
	private void runTest() {
		Customer customer = new CustomerImpl(17, "Hugo", "Tester");
		Address address = this.addressService.readAddressForCustomer(customer);
		
		System.out.println(address);
		System.out.println(address.getStreet() + " " + address.getNumber());
		System.out.println(address.getCityCode() + " " + address.getCity());
	}

}
