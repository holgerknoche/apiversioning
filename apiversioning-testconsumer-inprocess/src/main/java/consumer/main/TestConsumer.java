package consumer.main;

import java.util.concurrent.TimeUnit;

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
		
		this.runBenchmark(10000000, 1000000);
	}
	
	private void runBenchmark(int warmupIterations, int timedIterations) {
		Customer customer = new CustomerImpl(17, "Hugo", "Tester");
		Address address = this.addressService.readAddressForCustomer(customer);
		
		System.out.println("Running benchmark with " + warmupIterations +  " WI and " + timedIterations + " TI.");
		
		for (int iteration = 0; iteration < warmupIterations; iteration++) {
			this.benchmarkMethod(address);
		}
		
		long timeBefore = System.nanoTime();
		for (int iteration = 0; iteration < timedIterations; iteration++) {
			this.benchmarkMethod(address);
		}
		long timeAfter = System.nanoTime();
		
		long durationMus = TimeUnit.NANOSECONDS.toMicros(timeAfter - timeBefore);
		float averageMus = (float) durationMus / timedIterations;
		
		System.out.println("Benchmark took " + durationMus + "µs, avg. " + averageMus + "µs per invocation.");
	}
	
	private void benchmarkMethod(Address address) {
		address.getStreet();
		address.getNumber();
		address.getCityCode();
		address.getCity();
	}

}
