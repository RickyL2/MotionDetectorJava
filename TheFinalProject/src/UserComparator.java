import java.util.Comparator;

public class UserComparator implements Comparator<User>{

	/**
	 * returns value that represents which user should come first
	 */
	public int compare(User a, User b)
	{
		String aName = a.GetName();
		String bName = b.GetName();
		return aName.compareTo(bName);
	}
}
