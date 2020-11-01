package provider.api.impl;

import provider.api.CitySpec;

public class CitySpecImpl implements CitySpec {

	private final int postalCode;
	
	private final String cityName;

	public CitySpecImpl(int postalCode, String cityName) {
		this.postalCode = postalCode;
		this.cityName = cityName;
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
