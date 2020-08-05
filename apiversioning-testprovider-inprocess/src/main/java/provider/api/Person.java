package provider.api;

import java.time.LocalDate;

import apiversioning.annotations.APIKey;

public interface Person {
	
	@APIKey(0x00010001)
	public int getId();
	
	@APIKey(0x00010002)
	public String getFirstName();
	
	@APIKey(0x00010003)
	public String getLastName();
	
	@APIKey(0x00010004)
	public LocalDate getDateOfBirth();

}
