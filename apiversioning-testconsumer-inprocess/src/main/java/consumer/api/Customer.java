package consumer.api;

import apiversioning.annotations.APIKey;

public interface Customer {
	
	@APIKey(0x00010001)
	public int getId();
	
	@APIKey(0x00010002)
	public String getFirstName();
	
	@APIKey(0x00010003)
	public String getLastName();

}
