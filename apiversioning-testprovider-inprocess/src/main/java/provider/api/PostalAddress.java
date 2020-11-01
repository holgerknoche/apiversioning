package provider.api;

import apiversioning.annotations.APIKey;

public interface PostalAddress {
	
	@APIKey(0x00020001)
	public String getStreetName();
	
	@APIKey(0x00020002)
	public int getNumber();
	
	@APIKey(0x00020005)
	public CitySpec getCitySpec();

}
