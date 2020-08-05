package provider.api.impl;

import provider.api.PostalAddress;

public class PostalAddressImpl implements PostalAddress {

	private final String streetName;
	
	private final int number;
	
	private final int postalCode;
	
	private final String cityName;
	
	public PostalAddressImpl(String streetName, int number, int postalCode, String cityName) {
		this.streetName = streetName;
		this.number = number;
		this.postalCode = postalCode;
		this.cityName = cityName;
	}

	@Override
	public String getStreetName() {
		return this.streetName;
	}

	@Override
	public int getNumber() {
		return this.number;
	}

	@Override
	public int getPostalCode() {
		return this.postalCode;
	}

	@Override
	public String getCityName() {
		return this.cityName;
	}

}
