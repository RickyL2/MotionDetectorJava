import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.util.ArrayList;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
//import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SettingsUI extends JFrame{

	private static final int FRAME_WIDTH = 400;
	private static final int FRAME_HEIGHT = 400;
	
	private String FrameName = "Settings";
	private JLabel FrequencyTitle;
	private JLabel NotifyTitle;
	private JLabel OffSetTitle;
	private JLabel ErrorTitle;
	private JLabel PortionSizeLabel;
	private JTextField FrequencyField;
	private JTextField NotifyField;
	private JTextField OffSetField;
	private JTextField ErrorField;
	private JTextField PortionSizeField;
	private JButton ApplyButton;
	public SecurityWebCam theWebCam;
	boolean SecurityOn;
	//allow to use native webcam motion sensor
	private JLabel AltWebcamTitle;
	private JCheckBox WantAltCam;
	//private JPanel UsersArea;
	private JTextField UserName;
	private JTextField UserPhoneNumber;
	private JComboBox<String> UserCarrier;
	private JButton CreateUserButton;
	private ArrayList<JLabel> OldUsers;
	private ArrayList<JButton> DeleteUserButton;
	
	JPanel MainPanel;
	
	
	/**
	 * Gets everything ready for changing security camera settings
	 * @param theWebCam - security webcam that will be adjusted
	 */
	public SettingsUI(SecurityWebCam theWebCam)
	{
		//Users = new UserManager();
		this.theWebCam = theWebCam;
		SecurityOn = this.theWebCam.GetSecurityState();
		//this.theWebCam.SecurityOff();
		CreateTextAreas();
		UpdateTextFields();
		MakeUsersArea();
		createPanels();
		setTitle(FrameName);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
	}
	
	/**
	 * creates the areas that shows text
	 */
	private void CreateTextAreas()
	{
		FrequencyTitle = new JLabel("frequency: ");
		FrequencyField = new JTextField();
		NotifyTitle = new JLabel("notify time: ");
		NotifyField = new JTextField();
		OffSetTitle = new JLabel("pixel offset: ");
		OffSetField = new JTextField();
		ErrorTitle = new JLabel("error range: ");
		ErrorField = new JTextField();
		PortionSizeLabel = new JLabel("quantity of portions: ");
		PortionSizeField = new JTextField();
		AltWebcamTitle = new JLabel("Use Native Motion Sensor: ");
		WantAltCam = new JCheckBox();
		//creates button for saving values
		ApplyButton = new JButton();
		ApplyButton.setText("Apply");
		ApplyButton.addActionListener(new ApplyButtonListener());
	}
	
	/*
	 * initializes the combo box with all the carrier options
	 */
	private void CreateComboBox()
	{
		UserCarrier = new JComboBox<String>();
		UserCarrier.addItem("AllTel");
    	UserCarrier.addItem("AT&T");
    	UserCarrier.addItem("Boost Mobile");
    	UserCarrier.addItem("Cricket");
    	UserCarrier.addItem("Sprint");
    	UserCarrier.addItem("T-Mobile");
    	UserCarrier.addItem("US Cellular");
    	UserCarrier.addItem("Verizon");
    	UserCarrier.addItem("Virgin Mobile");
    	UserCarrier.setSelectedIndex(7);
	}
	
	/*
	 * will start to set new values from user
	 */
	
	public class ApplyButtonListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			SetValues();
			UpdateTextFields();
		}
		
	}
	
	/**
	 * updates text fields with values from webcam settings
	 */
	private void UpdateTextFields()
	{
		FrequencyField.setText(Integer.toString(theWebCam.GetFrequency()));
		NotifyField.setText(Integer.toString(theWebCam.GetDelayTimeForText()));
		OffSetField.setText(Integer.toString(theWebCam.GetOffSet()));
		ErrorField.setText(Integer.toString(theWebCam.GetErrorRange()));
		PortionSizeField.setText(Integer.toString(theWebCam.GetNumberOfPortions()));
	}
	
	/**
	 * sets the webcam settings values to those that are in the text boxes
	 * webcam settings values may have restrictions so user input may by ignored if too extreme
	 */
	private void SetValues()
	{
		theWebCam.SetFrequency(ConvertStringToInt(FrequencyField.getText(), theWebCam.GetFrequency()));
		theWebCam.SetDelayTimeForText(ConvertStringToInt(NotifyField.getText(), theWebCam.GetDelayTimeForText()));
		theWebCam.SetOffSet(ConvertStringToInt(OffSetField.getText(), theWebCam.GetOffSet()));
		theWebCam.SetErrorRange(ConvertStringToInt(ErrorField.getText(), theWebCam.GetErrorRange()));
		theWebCam.SetNumberOfPortions(ConvertStringToInt(PortionSizeField.getText(), theWebCam.GetNumberOfPortions()));
	}
	
	/**
	 * converts a string into an integer
	 * @param TheText - string that will be converted
	 * @param DefaultValue - integer that will be used as an alternative value if string failed conversion
	 * @return - integer value from either converting string or the default value 
	 */
	private int ConvertStringToInt (String TheText, int DefaultValue)
	{
		int value = 0;
		try
		{
			value = Integer.parseInt(TheText);
		}
		catch (NumberFormatException exception)
		{
			value = DefaultValue;
		}
		
		return value;
	}
	
	/*
	 * Creates area where user can create new users or delete previously added users
	 */
	private void MakeUsersArea()
	{
		OldUsers = new ArrayList<JLabel>();
		DeleteUserButton = new ArrayList<JButton>();
		//for displaying old users and give the option to delete each one
		for(int i = 0; i < SecurityWebCam.Users.GetNumberOfUsers(); i++)
		{
			//gets user data text
			JLabel temp = new JLabel();
			temp.setText(SecurityWebCam.Users.GetUser(i).toString());
			OldUsers.add(temp);
			//creates delete button
			JButton tempDeleteButton = new JButton();
			tempDeleteButton.setText("X");
			tempDeleteButton.addActionListener(new DeleteButtonListener(i));
			DeleteUserButton.add(tempDeleteButton);
			
		}
		//stuff for creating a new user
		UserName = new JTextField();
		UserPhoneNumber = new JTextField();
		CreateComboBox();
		CreateUserButton = new JButton();
		CreateUserButton.setText("Add User");
		CreateUserButton.addActionListener(new CreateUserButtonListener());
	}

	/*
	 * deletes the selected user from contact list
	 */
	private class DeleteButtonListener implements ActionListener
	{
		int UserNumber;
		public DeleteButtonListener(int i)
		{
			UserNumber = i;
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			SecurityWebCam.Users.RemoveUser(SecurityWebCam.Users.GetUser(UserNumber).GetName());
			//refreshes panel
			MakeUsersArea();
			remove(MainPanel);
			createPanels();
			revalidate();
		}
		
	}
	
	/*
	 * creates a new user based on info entered by user
	 */	
	public class CreateUserButtonListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			SecurityWebCam.Users.AddUser(UserName.getText(), 
					UserPhoneNumber.getText(), UserCarrier.getSelectedItem().toString());
			//refreshes panel
			MakeUsersArea();
			remove(MainPanel);
			createPanels();
			revalidate();
		}
		
	}	
	
	
	/**
	 * creates the panels and adds them to window
	 */
	private void createPanels()
	{
		//removeAll();
		MainPanel = new JPanel();
		MainPanel.setLayout(new BorderLayout());
		JPanel CameraSettings = new JPanel();
		CameraSettings.setLayout(new GridLayout(6, 2));
		CameraSettings.add(FrequencyTitle);
		CameraSettings.add(FrequencyField);
		CameraSettings.add(NotifyTitle);
		CameraSettings.add(NotifyField);
		CameraSettings.add(OffSetTitle);
		CameraSettings.add(OffSetField);
		CameraSettings.add(ErrorTitle);
		CameraSettings.add(ErrorField);
		CameraSettings.add(PortionSizeLabel);
		CameraSettings.add(PortionSizeField);
		CameraSettings.add(AltWebcamTitle);
		CameraSettings.add(WantAltCam);
		MainPanel.add(CameraSettings, BorderLayout.NORTH);
		//MainPanel.add(UsersArea, BorderLayout.CENTER);
		MainPanel.add(ApplyButton, BorderLayout.CENTER);
		//creates the user area
		JPanel UsersArea = new JPanel();
		JPanel OldUserArea = new JPanel();
		OldUserArea.setLayout(new GridLayout(SecurityWebCam.Users.GetNumberOfUsers(), 2));
		for(int i = 0; i < SecurityWebCam.Users.GetNumberOfUsers(); i++)
		{
			OldUserArea.add(OldUsers.get(i));
			OldUserArea.add(DeleteUserButton.get(i));
		}
		JPanel NewUserArea = new JPanel();
		NewUserArea.setLayout(new GridLayout(1, 4));
		NewUserArea.add(UserName);
		NewUserArea.add(UserPhoneNumber);
		NewUserArea.add(UserCarrier);
		NewUserArea.add(CreateUserButton);
		UsersArea.setLayout(new BorderLayout());
		UsersArea.add(OldUserArea, BorderLayout.CENTER);
		UsersArea.add(NewUserArea, BorderLayout.SOUTH);
		MainPanel.add(UsersArea, BorderLayout.SOUTH);
		add(MainPanel);
	}
	
}
