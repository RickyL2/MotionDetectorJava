import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SecurityCamUI extends JFrame{

	private static final int FRAME_WIDTH = 400;
	private static final int FRAME_HEIGHT = 300;
	
	private String FrameName = "Security Camera";
	private JButton OnButton;
	private JButton SettingsButton;
    private boolean SettingsOpen = false;
    private SettingsUI SettingsFrame;
    private SecurityWebCam SecurityCam = new SecurityWebCam();
	
    /**
     * prepares user interface for security camera JFrame
     */
	public SecurityCamUI()
	{
		createButtons();
		createPanels();
		setTitle(FrameName);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setMinimumSize(new Dimension(200, 200));
	}
	
	/**
	 * creates the buttons that will be used
	 */
	private void createButtons()
	{
		OnButton = new JButton("On");
		SettingsButton = new JButton("Settings");
		OnButton.addActionListener(new OnButtonListener());
		SettingsButton.addActionListener(new SettingsButtonListener());
	}
	
	public class OnButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			//if security already on, will turn off
			if(SecurityCam.GetSecurityState())
			{
				OnButton.setText("On");
				SecurityCam.SecurityOff();
			}
			else
			{
				OnButton.setText("Off");
				SecurityCam.SecurityOn();
			}
			System.out.println("real camera: " + SecurityCam.GetFrequency());
		}
	}
	
	public class SettingsButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			//settings windows will only open if no other settings window open and 
			//webcam security is off
			if(!SettingsOpen && OnButton.getText() != "Off")
			{
				SettingsFrame = new SettingsUI(SecurityCam);
				SettingsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				SettingsFrame.addWindowListener(new SettingsWindowListener());
				SettingsFrame.setVisible(true);
			}
		}
		
	}
	
	public class SettingsWindowListener implements WindowListener
	{

		@Override
		public void windowActivated(WindowEvent arg0) {
			// TODO Auto-generated method stub
			SettingsOpen = true;
		}
		
		@Override
		public void windowClosed(WindowEvent arg0) {
			// TODO Auto-generated method stub
			SettingsOpen = false;
		}
		@Override
		public void windowClosing(WindowEvent arg0) {
			// TODO Auto-generated method stub

		}
		@Override
		public void windowDeactivated(WindowEvent arg0) {
			// TODO Auto-generated method stub
		}
		@Override
		public void windowDeiconified(WindowEvent arg0) {
			// TODO Auto-generated method stub
		}
		@Override
		public void windowIconified(WindowEvent arg0) {
			// TODO Auto-generated method stub
		}
		@Override
		public void windowOpened(WindowEvent arg0) {
			// TODO Auto-generated method stub
		}
		
	}
	
	/**
	 * makes panels with user interface things and displays them
	 */
	private void createPanels()
	{
		JPanel MainPanel = new JPanel();
		MainPanel.setLayout(new BorderLayout());
		//will organize buttons
		JPanel ButtonOrganizer = new JPanel();
		ButtonOrganizer.add(OnButton);
		ButtonOrganizer.add(SettingsButton);
		//places JPanels into main panel
		MainPanel.add(ButtonOrganizer, BorderLayout.SOUTH);
		MainPanel.add(SecurityCam.GetWebCamPanel(), BorderLayout.CENTER);
		add(MainPanel);
	}
}