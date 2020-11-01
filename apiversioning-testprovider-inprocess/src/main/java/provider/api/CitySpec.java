package provider.api;

import apiversioning.annotations.APIKey;

public interface CitySpec {
	
	@APIKey(0x00020003)
	public int getPostalCode();
	
	@APIKey(0x00020004)
	public String getCityName();

}
