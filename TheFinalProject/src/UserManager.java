import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class UserManager {

	private static String FileName = "users.txt";
	private static ArrayList<User> UserDataList;
	
	public UserManager()
	{
		UserDataList = new ArrayList<User>();
		GetUserDataFromFile();
		OrganizeUsers();
	}
	/**
	 * Gets attempt to get user data from file
	 */
	private void GetUserDataFromFile()
	{
		try
		{
			File inFile = new File(FileName);
			UserDataList = ExtractUserDataFromFile(inFile);
			
		}
		catch (FileNotFoundException exception)
		{
			
		}
		catch (NoSuchElementException exception)
		{
			
		}
	}
	
	/**
	 * Gets user data from file and puts users into an arraylist
	 * @param inFile - file with user data
	 * @return - arraylist with all user data from file
	 * @throws FileNotFoundException - if file not found, error is thrown
	 */
	private ArrayList<User> ExtractUserDataFromFile (File inFile) throws FileNotFoundException
	{
		Scanner in = new Scanner(inFile);
		ArrayList<User> Data = new ArrayList<User>();
		while(in.hasNext())
		{
			String[] CurrentLine = in.nextLine().split(" ");
			//will only add user from file if line is properly formatted
			if (CurrentLine.length == 3)
			{
				Data.add(new User(CurrentLine[0], CurrentLine[1], CurrentLine[2]));
			}
		}
		in.close();
		return Data;
	}
	/**
	 * Adds a user to the arraylist of users
	 * @param Name - string that holds name of user
	 * @param PhoneNumber - string of user's phone number
	 * @param Carrier - string of user's phone carrier
	 */
	public void AddUser(String Name, String PhoneNumber, String Carrier)
	{
		UserDataList.add(new User(Name, PhoneNumber, Carrier));
		OrganizeUsers();
		SaveUsersToFile();
	}
	
	/**
	 * Saves user data from arraylist to file
	 */
	public void SaveUsersToFile()
	{
		try
		{
			PrintWriter out = new PrintWriter(FileName);
			for (int i = 0; i < UserDataList.size(); i ++)
			{
				User CurrentUser = UserDataList.get(i);
				out.println(CurrentUser.GetName() + " " 
						+ CurrentUser.GetPhoneNumber() + " " + CurrentUser.GetCarrier());
			}
			out.close();
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Removes user from arraylist
	 * @param Name - string with name of user that will be removed
	 */
	public void RemoveUser(String Name)
	{
		for (int i = 0; i < UserDataList.size(); i ++)
		{
			if (UserDataList.get(i).GetName().toLowerCase().equals(Name.toLowerCase()))
			{
				UserDataList.remove(i);
			}
		}
		
		OrganizeUsers();
		SaveUsersToFile();
	}
	
	/**
	 * Retrieve the number of users in arraylist
	 * @return - returns the size of user arraylist
	 */
	public int GetNumberOfUsers()
	{
		return UserDataList.size();
	}
	
	/**
	 * returns specific user from arraylist
	 * @param i - integer that represents which user is wanted
	 * @return - returns user that is wanted
	 */
	public User GetUser(int i)
	{
		//will return an invalid user if int i does not represent an actual user
		if (i < 0 || i > UserDataList.size())
			return new User("0", "0", "0");
		else
			return UserDataList.get(i);
	}
	
	/**
	 * Organize users alphabetically
	 */
	public void OrganizeUsers()
	{
		UserDataList.sort(new UserComparator());
	}
}
