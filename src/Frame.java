

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;


/**
 * Frame to hold everything.
 */
public class Frame
{
    private boolean buttonsVisible;
    private Component mancalaComponent;
	
	/**
     * Creates a new GameFrame
     */
    public Frame()
    {
        buttonsVisible = true; //holds if the buttons are visible. if they are both false display board
                
        final JFrame frame = new JFrame();
        final Model model = new Model();
        mancalaComponent = new Component(model, new RoundFormatter());
        
        final JPanel lowerPanel = new JPanel();
        JButton undo = new JButton("undo");
        undo.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                model.undo();
            }
        });
        
        final JPanel stylePanel = new JPanel();
        final JPanel panel = new JPanel();        

        
        /**
         * Choose the style of Mancala Frame
         * When a user click the style button, stylePanel hide
         * And the system asks the number of stones
         */
        JLabel styleLabel = new JLabel("Select Mancala game theme. ");
        stylePanel.add(styleLabel);
        
        JButton roundButton = new JButton(" Round ");
        JButton squareButton = new JButton(" Square ");
       
        roundButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                mancalaComponent.setFormatter(new RoundFormatter());
                stylePanel.setVisible(false);
                frame.add(panel, BorderLayout.NORTH);
                panel.setVisible(true);
                
            }
        });
        
        squareButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                mancalaComponent.setFormatter(new SquareFormatter());
                stylePanel.setVisible(false);
                frame.add(panel, BorderLayout.NORTH);
                panel.setVisible(true);
            }
        });
        
        stylePanel.add(roundButton);
        stylePanel.add(squareButton);
        
        
        
        /**
         * Choose the number of stones
         */
        JLabel stoneLabel = new JLabel("Enter number of stones and click start button (You can choose only 3 or 4): ");
        panel.add(stoneLabel);
        
        JTextField stoneTextfield = new JTextField("3");
        panel.add(stoneTextfield);
        
        
        JButton startButton = new JButton("Start"); 
        
        startButton.addActionListener(new ActionListener()
        {
        	@Override
        	public void actionPerformed(ActionEvent e)
        	{
        		String numStone = stoneTextfield.getText();
        		if(checkStones(getNumStones(numStone)) == false)
        		{
        			errStonePopup();
        		}
        		else
	        	{
	        		model.setNumStones(getNumStones(numStone));
	        		model.setCurrentState("STARTED");
	        		buttonsVisible = false;
	        		setButtonVisible(false, panel);
	        		setButtonVisible(true, lowerPanel);
	        	}
        	}
        });
        
        panel.add(startButton);
        
        lowerPanel.add(undo);
        frame.setSize(1050, 750);
        frame.setTitle("Mancala");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.add(panel, BorderLayout.NORTH);
        frame.add(stylePanel, BorderLayout.NORTH);
        frame.add(mancalaComponent, BorderLayout.CENTER);
        frame.add(lowerPanel, BorderLayout.SOUTH);
        setButtonVisible(false, lowerPanel);
        frame.setVisible(true);
        
    }
    
    public void errStonePopup()
    {
    	JOptionPane.showMessageDialog(null, "Error: Check the number of stones.");
    }
    
    /**
     * converts string to integer
     * @param numStone String
     * @return numStone Integer
     */
    public int getNumStones(String numStone)
    {
    	if(numStone.isEmpty()){
    		return 0;
    	}
    	else{
    		return Integer.parseInt(numStone);
    	}
    }
    
    
    /**
     * checks the value of stones is correct
     * true: 3 or 4
     * false: other
     * @param numStone number of stones
     * @return true if numStones are correct
     */
    public boolean checkStones(int numStone)
    {
    	if(numStone == 3 || numStone == 4)
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    	
    }
    
    
    /**
     * sets the buttons on the frame visible or not depending on if the game is in session
     * @param visible true if visible
	 * @param p the JPanel
     */
    public void setButtonVisible(boolean visible, JPanel p)
    {
        p.setVisible(visible);     
        //if both are false, start the Game.
        if(!buttonsVisible)
        {
            mancalaComponent.startGame();
        }
    }
    
}