package consumer.api;

import apiversioning.annotations.APIKey;

public interface Address {
	
	@APIKey(0x00020001)
	public String getStreet();
	
	@APIKey(0x00020002)
	public int getNumber();
	
	@APIKey(0x00020003)
	public int getCityCode();
	
	@APIKey(0x00020004)
	public String getCity();

}
