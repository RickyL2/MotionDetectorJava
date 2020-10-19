import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamMotionDetector;
import com.github.sarxos.webcam.WebcamMotionEvent;
import com.github.sarxos.webcam.WebcamMotionListener;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.io.IOException;


public class SecurityWebCam {

	private static final int ONE_SECOND = 1000;
	private static final int MIN_FREQUENCY = 1  * ONE_SECOND;
	
	private Webcam SecurityCam;
	private static Texter TexterMannager;
	public static UserManager Users;
	private int frequency = 2 * ONE_SECOND;
	private int DelayTimeForText = 30 * ONE_SECOND;
	//initializes time so it it ready to go
	private long TimeATLastText = 0;
	private static MovementDetector MotionSensor;
	private BufferedImage CurrentImage = null;
	private BufferedImage LastImage = null;
	private Timer TheTimer;
	private JLabel WarningImage;
	private ActionListener listener;
	private int ImageCounter = 0;
	
	public boolean UseNativeMotionSense = false;
	
	/**
	 * prepares everything for the security webcam
	 */
	public SecurityWebCam()
	{
		Users = new UserManager();
		MakeImage();
		if(Webcam.getWebcams().size() != 0)
		{
			SecurityCam = Webcam.getDefault();
			SecurityCam.setViewSize(WebcamResolution.VGA.getSize());
			SecurityCam.open();
			CurrentImage = SecurityCam.getImage();
			LastImage = CurrentImage;
			TexterMannager = new Texter();
			MotionSensor = new MovementDetector(CurrentImage, LastImage);
			listener = new MotionTimer();
			TheTimer = new Timer(frequency, listener);
		}
		else
			System.out.println("no webcam found");
	}
	
	/**
	 * turns on security functions if there is a webcam connected
	 */
	public void SecurityOn()
	{
		if (Webcam.getWebcams().size() != 0)
			TheTimer.start();
	}
	
	/**
	 * turns off security functions if there is a webcam connected
	 */
	public void SecurityOff()
	{
		if (Webcam.getWebcams().size() != 0)
			TheTimer.stop();
	}
	
	/**
	 * checks if security camera functions are running
	 * @return - returns boolean representing if the security camera functions are running
	 */
	public boolean GetSecurityState()
	{
		if (Webcam.getWebcams().size() == 0)
			return false;
		return TheTimer.isRunning();
	}
	
	/**
	 * sets the frequency of how often to check for movements
	 * @param time - integer for how many milliseconds to wait for frequency
	 */
	public void SetFrequency(int time)
	{
		if(time < MIN_FREQUENCY)
			frequency = MIN_FREQUENCY;
		else
			frequency = time;
		// if security was on, needs to turn off first to make change happen
		if (GetSecurityState())
		{	SecurityOff();
			TheTimer = new Timer(frequency, listener);
			SecurityOn();
		}
		else
			TheTimer = new Timer(frequency, listener);
		
	}
	
	/**
	 * gets the value of the frequency
	 * @return - returns integer value of frequency
	 */
	public int GetFrequency()
	{
		return frequency;
	}
	
	/**
	 * sets the delat time
	 * @param time - integer for time to wait
	 */
	public void SetDelayTimeForText(int time)
	{
		if(time > 0)
		{
			DelayTimeForText = time;
			TimeATLastText = 0;
		}
	}
	
	/**
	 * gets the delay time
	 * @return - returns integer of delay time
	 */
	public int GetDelayTimeForText()
	{
		return DelayTimeForText;
	}
	
	/**
	 * sets the value of offset if there is a webcam present
	 * @param offset - integer value that will be the offset
	 */
	public void SetOffSet(int offset)
	{
		if (Webcam.getWebcams().size() != 0)
			MotionSensor.SetOffSet(offset);
	}
	
	/**
	 * gets the offset value
	 * @return - returns offset value if a webcam is present. if not, will return a value of zero
	 */
	public int GetOffSet()
	{
		if (Webcam.getWebcams().size() == 0)
			return 0;
		return MotionSensor.GetOffSet();
	}
	
	/**
	 * Sets the error range value if a webcam is present
	 * @param range - integer that will be the error range
	 */
	public void SetErrorRange(int range)
	{
		if (Webcam.getWebcams().size() != 0)
			MotionSensor.SetErrorRange(range);
		}
	
	/**
	 * gets the error range
	 * @return - returns the error range if webcam is present. if not, will return a value of zero
	 */
	public int GetErrorRange()
	{
		if (Webcam.getWebcams().size() == 0)
			return 0;
		return MotionSensor.GetErrorRange();
	}
	
	/**
	 * sets the number of portions for image analysis if webcam is present
	 * @param Quantity - integer that represents the number of portions
	 */
	public void SetNumberOfPortions(int Quantity)
	{
		if (Webcam.getWebcams().size() != 0)
			MotionSensor.SetNumberOfPortions(Quantity);
	}
	
	/**
	 * gets the number of portions
	 * @return - the number of portions if webcam is present. if not, will return a value of zero
	 */
	public int GetNumberOfPortions()
	{
		if (Webcam.getWebcams().size() == 0)
			return 0;
		return MotionSensor.GetNumberOfPortions();
	}
	
	/**
	 * makes warning image
	 */
	private void MakeImage()
	{
		WarningImage = new JLabel(new ImageIcon("CameraNotFoundImage.png"));
	}
	
	/**
	 * creates panel that will show the webcam's live feed or a warning image
	 * @return - returns jpanel of warning image if no webcams are connected. if one is, will return live feed from webcam
	 */
	public JPanel GetWebCamPanel()
	{
		if (Webcam.getWebcams().size() == 0)
		{
			JPanel ImagePanel = new JPanel();
			ImagePanel.add(WarningImage);
			return ImagePanel;
		}
		else
		{
			WebcamPanel TheWebCamPanel = new WebcamPanel(SecurityCam);
			TheWebCamPanel.setFPSDisplayed(true);
			TheWebCamPanel.setDisplayDebugInfo(false);
			TheWebCamPanel.setImageSizeDisplayed(true);
			TheWebCamPanel.setMirrored(true);
			return TheWebCamPanel;
		}
	}
	
	/**
	 * Notifies the users that movement was detected via console and text messages
	 */
	private void NotifyUsers()
	{
		Date now = new Date();
		System.out.println("Motion Detected at " + now);
		long CurrentTime = new Date().getTime();
		if ((CurrentTime - TimeATLastText) >= DelayTimeForText)
		{
			int NumberOfUsers = Users.GetNumberOfUsers();
			for(int i = 0; i < NumberOfUsers; i ++)
			{
				User CurrentUser = Users.GetUser(i);
				System.out.println(CurrentUser.GetName() + " " + CurrentUser.GetPhoneNumber() + " " + CurrentUser.GetCarrier());
				TexterMannager.ThreadSendMessage(CurrentUser, "Motion Detected at around " + now);
			}
			TimeATLastText = CurrentTime;
			//saves image to a PNG file
			String imageName =  "image" + ImageCounter + ".png";
			ImageCounter++;
			try
			{
				ImageIO.write(CurrentImage, "PNG", new File(imageName));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public class MotionTimer implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			LastImage = CurrentImage;
			CurrentImage = SecurityCam.getImage();
			MotionSensor.UpdateImages(CurrentImage, LastImage);
			//boolean MotionFound = MotionSensor.MovementDetected();
			boolean MotionFound = MotionSensor.StandardDetect();
			if (MotionFound)
			{
				NotifyUsers();
			}
		}
		
	}
	
}
