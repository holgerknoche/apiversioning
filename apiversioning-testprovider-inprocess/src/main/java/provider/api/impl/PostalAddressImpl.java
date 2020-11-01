package provider.api.impl;

import provider.api.CitySpec;
import provider.api.PostalAddress;

public class PostalAddressImpl implements PostalAddress {

	private final String streetName;
	
	private final int number;
	
	private final CitySpec citySpec;
		
	public PostalAddressImpl(String streetName, int number, CitySpec citySpec) {
		this.streetName = streetName;
		this.number = number;
		this.citySpec = citySpec;
	}

	@Override
	public String getStreetName() {
		return this.streetName;
	}

	@Override
	public int getNumber() {
		return this.number;
	}
	
	public CitySpec getCitySpec() {
		return this.citySpec;
	}

}
