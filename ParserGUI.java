import javax.swing.JFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ParserGUI extends JFrame implements ActionListener
{
	private static final int WIDTH = 650;
	private static final int HEIGHT = 650;

	Parser p; 

	private JLabel inputLabel, outputLabel, grammarLabel;
	private JTextArea ia, oa;
	private JButton parseButton, closeButton;

	public ParserGUI() throws ParseException
	{
		inputLabel = new JLabel("Enter input here"); 
		outputLabel = new JLabel("Console");
		parseButton = new JButton("Parse");
		parseButton.addActionListener(this);
		closeButton = new JButton("Close");
		closeButton.addActionListener(this);
		ia = new JTextArea(); ia.setColumns(40); 
		ia.setLineWrap(true); ia.setRows(15); ia.setWrapStyleWord(true);
		oa = new JTextArea(); oa.setColumns(40);
		oa.setLineWrap(true); oa.setRows(15); oa.setWrapStyleWord(true);
		ia.setEditable(true); oa.setEditable(false);
		setTitle("CSC401 LL(0) Parser");
		
		Container pane = getContentPane();
		pane.setLayout(new FlowLayout());
		
		///////////////////////////////////////////
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.add(inputLabel);
		leftPanel.add(ia);
		leftPanel.add(parseButton);
		leftPanel.add(outputLabel);
		leftPanel.add(oa);
		leftPanel.add(closeButton);
		pane.add(leftPanel);
		//////////////////////////////////////////

		setSize(WIDTH,HEIGHT);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent e){
		if (e.getSource() == parseButton){
			String f = ia.getText();
			try {
				p = new Parser(f);
				String updatedMessage = p.getMyMessage();
				oa.setText(updatedMessage);
			} catch (Exception ex) { 
				String e1 = ""+ex; 
				oa.setText(e1); 
			}
		}
		else if (e.getSource() == closeButton){
			System.exit(0);
		}
	}
	
	public static void main(String[] args) {
		try {
			ParserGUI p = new ParserGUI(); 
		}catch(ParseException e){System.out.println(e.getMessage());} 
	}
}