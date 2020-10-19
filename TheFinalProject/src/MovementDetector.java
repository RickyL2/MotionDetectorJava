import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MovementDetector {
	
	private static final int MIN_PORTIONS = 1, MAX_PORTIONS = 15;
	private static final int MIN_OFFSET = 1, MAX_OFFSET = 10;
	private static final int MIN_ERROR_RANGE = 0, MAX_ERROR_RANGE = 20;

	private BufferedImage Image1 = null;
	private BufferedImage Image2 = null;
	private int NumberOfPortions = 8;
	private int ErrorRange = 5;
	//how many pixels to till the next time it gets a pixels color, should always be at least 1
	private int offset = 3;
	private ArrayList<CoordinateRange> PhotoPortionManager = new ArrayList<CoordinateRange>();
	
	/**
	 * sets up stuff for movement detecting through the comparison of two images
	 * @param Image1 - first image
	 * @param Image2 - second image
	 */
	public MovementDetector(BufferedImage Image1, BufferedImage Image2)
	{
		UpdateImages(Image1, Image2);
		SetUpPortionCoordinates(PhotoPortionManager, NumberOfPortions, Image1.getWidth(), Image1.getHeight());
	}
	
	/**
	 * updates images to those that are entered
	 * @param Image1 - first image
	 * @param Image2 - second image
	 */
	public void UpdateImages(BufferedImage Image1, BufferedImage Image2)
	{
		this.Image1 = Image1;
		this.Image2 = Image2;
	}
	
	/**
	 * sets the offset value to that which is entered. if entered value too big or too small, will be set to maximum or minimum value
	 * @param offset - integer representing how many pixels to move through till next color check
	 */
	public void SetOffSet(int offset)
	{
		if(offset < MIN_OFFSET)
			this.offset = MIN_OFFSET;
		else if(offset > MAX_OFFSET)
			this.offset = MAX_OFFSET;
		else
			this.offset = offset;
	}
	
	/**
	 * gets the offset value
	 * @return - returns the offset value
	 */
	public int GetOffSet()
	{
		return offset;
	}
	
	/**
	 * sets the range of error variable. if entered value too big or too small, will be set to maximum or minimum value
	 * @param range - integer representing the range of error for a color
	 */
	public void SetErrorRange(int range)
	{
		if(range < MIN_ERROR_RANGE)
			ErrorRange = MIN_ERROR_RANGE;
		else if(range > MAX_ERROR_RANGE)
			ErrorRange = MAX_ERROR_RANGE;
		else
			ErrorRange = range;
	}
	
	/**
	 * gets the error range value
	 * @return - returns error range value
	 */
	public int GetErrorRange()
	{
		return ErrorRange;
	}
	
	/**
	 * sets the quantity of portions value. if entered value too big or too small, will be set to maximum or minimum value
	 * @param Quantity - integer representing the number of portions for an image
	 */
	public void SetNumberOfPortions(int Quantity)
	{
		if(Quantity < MIN_PORTIONS)
			NumberOfPortions = MIN_PORTIONS;
		else if(Quantity > MAX_PORTIONS)
			NumberOfPortions = MAX_PORTIONS;
		else
			NumberOfPortions = Quantity;
		SetUpPortionCoordinates(PhotoPortionManager, NumberOfPortions, Image1.getWidth(), Image1.getHeight());
	}
	
	/**
	 * gets the number of portions value
	 * @return - returns the number of portions
	 */
	public int GetNumberOfPortions()
	{
		return NumberOfPortions;
	}
	
	/**
	 * detects if there has been any motion by comparing the average colors from each portion from two photos
	 * @return - returns boolean representing if motion has been detected
	 */
	public boolean StandardDetect()
	{
		for(int i = 0; i < PhotoPortionManager.size(); i++)
		{
			//gets average color from both pictures' portion
			Color AverageColorFromPortionImage1 = GetAveragePortionColor(Image1, PhotoPortionManager.get(i));
			Color AverageColorFromPortionImage2 = GetAveragePortionColor(Image2, PhotoPortionManager.get(i));
			//if a significant difference is found in red, green, or blue color values, will return true because most likely change in color was caused by movement
			if(Math.abs(AverageColorFromPortionImage1.getRed() - AverageColorFromPortionImage2.getRed()) > ErrorRange)
			{
				return true;
			}
			else if(Math.abs(AverageColorFromPortionImage1.getBlue() - AverageColorFromPortionImage2.getBlue()) > ErrorRange)
			{
				return true;
			}
			else if(Math.abs(AverageColorFromPortionImage1.getGreen() - AverageColorFromPortionImage2.getGreen()) > ErrorRange)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Determines how to split images into multiple portions based on image dimensions and requested number of portions
	 * @param Portions - ArrayList of CoordinateRange objects where coordinates of portions will be stored
	 * @param QuantityOfPortions - integer of requested amount of portion, actual amount may be different
	 * @param width - width of images
	 * @param height - height of images
	 */
	private void SetUpPortionCoordinates(ArrayList<CoordinateRange> Portions, int QuantityOfPortions, int width, int height)
	{
		Portions.clear();
		QuantityOfPortions = (int) (Math.sqrt(QuantityOfPortions + 0.0) * 2);
		//determines number of rows and columns, will work with even and odd number of portions
		int QuantityOfRows = QuantityOfPortions / 2;
		int QuantityOfColumns = QuantityOfPortions - QuantityOfRows;
		int XIncrement = width / QuantityOfColumns;
		int YIncrement = height / QuantityOfRows;
		//will assign coordinate ranges to portions queue
		//will go through each row
		for (int row = 0; row < QuantityOfRows; row++)
		{
			for (int column = 0; column < QuantityOfColumns; column++)
			{
				CoordinateRange CurrentArea = new CoordinateRange(column * XIncrement, (column + 1) * XIncrement, row * YIncrement, (row + 1) * YIncrement);
				//System.out.println("Rows: " + (row + 1) + ", Columns: " + (column + 1));
				Portions.add(CurrentArea);
			}
		}
		//System.out.println("Rows: " + QuantityOfRows + ", Columns: " + QuantityOfColumns);
		//System.out.println("XInc: " + XIncrement + ", YInc: " + YIncrement);
		//System.out.println("Number of portions: " + Portions.size());
	}
	
	/**
	 * gets the average color of a portion from an image
	 * @param image - image that will be analyzed
	 * @param CoordinateArea - CoordinateArea object that has data of coordinates to check
	 * @return - returns color object that represents the average color
	 */
	private Color GetAveragePortionColor(BufferedImage image, CoordinateRange CoordinateArea)
	{
		int RedValues = 0, BlueValues = 0, GreenValues = 0;
		//goes through each row of pixels
		for(int y = CoordinateArea.GetFirstY(); y < CoordinateArea.GetLastY(); y++)
		{
			//goes through each column of pixels
			for(int x = CoordinateArea.GetFirstX(); x < CoordinateArea.GetLastX(); x += offset)
			{
				//gets color of current pixel
				Color CurrentPixelColor = new Color(image.getRGB(x, y));
				RedValues += CurrentPixelColor.getRed();
				BlueValues += CurrentPixelColor.getBlue();
				GreenValues += CurrentPixelColor.getGreen();
				//System.out.println("X : " + x + ", Y : " + y);
			}
		}
		//the 1 is the alpha value
		return new Color((offset * RedValues)/CoordinateArea.GetArea(), (offset * GreenValues)/CoordinateArea.GetArea(), (offset * BlueValues)/CoordinateArea.GetArea(), 1);
	}
	
}
