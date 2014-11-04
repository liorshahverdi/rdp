import javax.swing.JFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ParserGUI extends JFrame implements ActionListener
{
	private static final int WIDTH = 435;
	private static final int HEIGHT = 670;
	private Parser p; 
	private JLabel inputLabel, outputLabel, fil1, fil2, fil3;
	private JTextArea ia, oa;
	private JButton parseButton, closeButton;

	public ParserGUI() throws ParseException
	{

		inputLabel = new JLabel("Input Test File");
		outputLabel = new JLabel("Console");
		fil1 = new JLabel("            "); fil2 = new JLabel("              "); fil3 = new JLabel("              "); 
		parseButton = new JButton("Parse");
		parseButton.addActionListener(this);
		parseButton.setMargin(new Insets(1,1,1,1));
		closeButton = new JButton("Close");
		closeButton.addActionListener(this);
		closeButton.setMargin(new Insets(1,1,1,1));
		ia = new JTextArea(); ia.setColumns(40); 
		ia.setLineWrap(true); ia.setRows(15); ia.setWrapStyleWord(true);
		oa = new JTextArea(); oa.setColumns(40);
		oa.setLineWrap(true); oa.setRows(15); oa.setWrapStyleWord(true);
		ia.setEditable(true); oa.setEditable(false);
		setTitle("CSC401 LL(0) Parser");
		add(getJContentPane());
		setSize(WIDTH,HEIGHT);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private JPanel getJContentPane(){
		JPanel temp = new JPanel();
		temp.setLayout(null);

		JPanel pan = new JPanel();
		pan.setBounds(15, 15, 385, 600);
		pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
		temp.add(pan);
		pan.add(inputLabel);
		pan.add(ia);
		pan.add(fil1);
		pan.add(parseButton);
		pan.add(fil2);
		pan.add(outputLabel);
		pan.add(oa);
		pan.add(fil3);
		pan.add(closeButton);
		return temp;
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
		else if (e.getSource() == closeButton){ System.exit(0); }
	}
	
	public static void main(String[] args) {
		try {
			ParserGUI p = new ParserGUI(); 
		}catch(ParseException e){System.out.println(e.getMessage());} 
	}
}