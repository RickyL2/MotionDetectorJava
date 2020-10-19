
public class User {

	private static int PHONE_NUMBER_LENGTH = 10;
	private String Name;
	private String PhoneNumber;
	private String Carrier;
	
	/**
	 * makes blank user
	 */
	public User()
	{
		Name = null;
		PhoneNumber = null;
		Carrier = null;
	}
	
	/**
	 * sets values of user
	 * @param Name - string of user's name
	 * @param PhoneNumber - string of user's phone number
	 * @param Carrier - string of user's phone number
	 */
	public User(String Name, String PhoneNumber, String Carrier)
	{
		SetName(Name);
		SetPhoneNumber(PhoneNumber);
		SetCarrier(Carrier);
	}
	
	/**
	 * sets name of user
	 * @param Name - string with user's name
	 */
	public void SetName(String Name)
	{
		this.Name = Name;
	}
	
	/**
	 * sets the phone number of user
	 * @param PhoneNumber - string with user's phone number
	 */
	public void SetPhoneNumber(String PhoneNumber)
	{
		this.PhoneNumber = PhoneNumberDigitIsolator(PhoneNumber);
	}
	
	/**
	 * sets the carrier of user's phone
	 * @param Carrier - string of carrier
	 */
	public void SetCarrier(String Carrier)
	{
		this.Carrier = Carrier;
	}
	
	/**
	 * gets the name of the user
	 * @return - returns string of user's name
	 */
	public String GetName()
	{
		return Name;
	}
	
	/**
	 * gets the phone number of the user
	 * @return - returns string of user's phone number
	 */
	public String GetPhoneNumber()
	{
		return PhoneNumber;
	}
	
	/**
	 * gets the carrier of user's phone
	 * @return - returns string of carrier
	 */
	public String GetCarrier()
	{
		return Carrier;
	}
	
	public String toString()
	{
		return "" + Name + " " + PhoneNumber + " " + Carrier;
	}
	/**
	 * Uses recursion and removes any non-digit characters from phone number
	 * @param PhoneNumber - string that contains phone number
	 * @return - returns string of phone number with only the digits or returns null if phone number did not have enough digits
	 */
	private String PhoneNumberDigitIsolator(String PhoneNumber)
	{
	    StringBuilder PhoneNumberDigits = new StringBuilder(PhoneNumber);
	    for(int i = 0; i < PhoneNumber.length(); i++)
	    {
	    	if (!Character.isDigit(PhoneNumber.charAt(i)))
	    	{
	    		PhoneNumberDigits.deleteCharAt(i);
	    		return PhoneNumberDigitIsolator(PhoneNumberDigits.toString());
	    	}
	    }
	    PhoneNumber = PhoneNumberDigits.toString();
	    if (PhoneNumber.length() > PHONE_NUMBER_LENGTH)
			PhoneNumber = PhoneNumber.substring(0, PHONE_NUMBER_LENGTH-1);
	    if (PhoneNumber.length() < PHONE_NUMBER_LENGTH)
			PhoneNumber = null;
	    return PhoneNumber;
	}
}
