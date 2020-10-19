
public class CoordinateRange {

	private int FirstX;
	private int LastX;
	private int FirstY;
	private int LastY;
	
	/**
	 * sets up coordinate range data
	 * @param x1 - first x position value
	 * @param x2 - last x position value
	 * @param y1 - first y position value
	 * @param y2 - last y position value
	 */
	public CoordinateRange(int x1, int x2, int y1, int y2)
	{
		FirstX = x1;
		LastX = x2;
		FirstY = y1;
		LastY = y2;
	}
	
	/**
	 * sets the first x position value
	 * @param x - integer of first x position value
	 */
	public void SetFirstX(int x)
	{
		FirstX = x;
	}
	
	/**
	 * sets the last x position value
	 * @param x - integer of last x position value
	 */
	public void SetLastX(int x)
	{
		LastX = x;
	}
	
	/**
	 * sets the first y position value
	 * @param x - integer of first y position value
	 */
	public void SetFirstY(int y)
	{
		FirstY = y;
	}
	
	/**
	 * sets the last y position value
	 * @param x - integer of last y position value
	 */
	public void SetLastY(int y)
	{
		LastY = y;
	}
	
	/**
	 * gets the first x value
	 * @return - returns integer of first x value
	 */
	public int GetFirstX()
	{
		return FirstX;
	}
	
	/**
	 * gets the last x value
	 * @return - returns integer of last x value
	 */
	public int GetLastX()
	{
		return LastX;
	}
	
	/**
	 * gets the first y value
	 * @return - returns integer of first y value
	 */
	public int GetFirstY()
	{
		return FirstY;
	}
	
	/**
	 * gets the last y value
	 * @return - returns integer of last y value
	 */
	public int GetLastY()
	{
		return LastY;
	}
	
	/**
	 * gets area that is covered by coordinate values
	 * @return - returns integer of calculated area
	 */
	public int GetArea()
	{
		int Area = (LastY - FirstY) * (LastX - FirstX);
		if(Area < 0)
			Area *= -1;
		return Area;
	}
}
