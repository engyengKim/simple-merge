import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;


public class MainView extends JFrame{
	private JPanel mainPanel;
	private int comparePressed;

	// tool panel
	private JPanel toolPanel;
	private JButton compareBtn;
	private JButton upBtn;
	private JButton downBtn;
	private JButton copyToLeftBtn;
	private JButton copyToRightBtn;
	
	// image icon for ImageBtn
	private ImageIcon compare_icon;
	private ImageIcon up_icon;
	private ImageIcon down_icon;
	private ImageIcon left_icon;
	private ImageIcon right_icon;
	private ImageIcon view_icon;
	
	
	// specific panel (right, left)
	private JPanel holderPanel;
	private PanelView leftPV;
	private PanelView rightPV;
	
	public MainView() throws Exception {
		super("Simple Merge");
		
		mainPanel = new JPanel();
		toolPanel = new JPanel();
		toolPanel.setBorder(new MatteBorder(0,0,1,0, Color.GRAY));
		
		comparePressed = 0; // comparePressed: even=NOT pressed, odd= pressed
		
		// set image icon
		compare_icon = new ImageIcon("res/compare.png");
		up_icon = new ImageIcon("res/up.png");
		down_icon = new ImageIcon("res/down.png");
		left_icon = new ImageIcon("res/left.png");
		right_icon = new ImageIcon("res/right.png");
		view_icon = new ImageIcon("res/not_compare.png");
		
		// set size of image button
		Image compare_img = compare_icon.getImage(); compare_img = compare_img.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
		Image up_img = up_icon.getImage(); up_img = up_img.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
		Image down_img = down_icon.getImage(); down_img = down_img.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
		Image left_img = left_icon.getImage(); left_img = left_img.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
		Image right_img = right_icon.getImage(); right_img = right_img.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
		Image view_img = view_icon.getImage(); view_img = view_img.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
		
		compare_icon = new ImageIcon(compare_img);
		up_icon = new ImageIcon(up_img);
		down_icon = new ImageIcon(down_img);
		left_icon = new ImageIcon(left_img);
		right_icon = new ImageIcon(right_img);
		view_icon = new ImageIcon(view_img);
		
		
		// set image button
		compareBtn = new JButton(compare_icon); compareBtn.setContentAreaFilled(false);
		upBtn = new JButton(up_icon); upBtn.setContentAreaFilled(false);
		downBtn = new JButton(down_icon); downBtn.setContentAreaFilled(false);
		copyToLeftBtn = new JButton(left_icon); copyToLeftBtn.setContentAreaFilled(false);
		copyToRightBtn = new JButton(right_icon); copyToRightBtn.setContentAreaFilled(false);
		
		
		// make Image Button's border invisible
		compareBtn.setBorderPainted(false);
//		compareBtn.setFocusPainted(false);
		upBtn.setBorderPainted(false);
//		upBtn.setFocusPainted(false);
		downBtn.setBorderPainted(false);
//		downBtn.setFocusPainted(false);
		copyToLeftBtn.setBorderPainted(false);
//		copyToLeftBtn.setFocusPainted(false);
		copyToRightBtn.setBorderPainted(false);
//		copyToRightBtn.setFocusPainted(false);
		
		add(compareBtn);
		add(upBtn);
		add(downBtn);
		add(copyToLeftBtn);
		add(copyToRightBtn);
		
		// set button's initial state
		compareBtn.setEnabled(false);
		upBtn.setEnabled(false);
		downBtn.setEnabled(false);
		copyToLeftBtn.setEnabled(false);
		copyToRightBtn.setEnabled(false);
		
		
		holderPanel = new JPanel();
		leftPV = new PanelView();
		rightPV = new PanelView();
		
		// set color of panel
		leftPV.setPanelColor(255,0,0);
		leftPV.setBorder(new MatteBorder(0,0,0,1, Color.GRAY));
		rightPV.setPanelColor(0, 255, 0);
		
		
		leftPV.loadBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				load(leftPV, rightPV);
				leftPV.setXpressed(false);
			}
		});
		
		rightPV.loadBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				load(rightPV, leftPV);
				rightPV.setXpressed(false);
			}
		});
		
		
		compareBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("compare button pressed.");
				
				if(leftPV.getXpressed() || rightPV.getXpressed()){
					//if at least xbutton pressed, can't be compare mode
					setMVbutton(false);
					leftPV.setXpressed(false);
					rightPV.setXpressed(false);

					return;
				}
				
				comparePressed++;
				
				if(comparePressed % 2 == 1) {
					// compareBtn pressed once->do compare
					compareBtn.setIcon(view_icon);
					
					setMode(Mode.COMPARE);
				}
				else{
					// compareBtn pressed twice->try to escape compare mode
					if(comparePressed >= 0) {
						int a = checkUpdated(leftPV);
						int b = checkUpdated(rightPV);
						
						if(a == 2 || b == 2) {
							// keep compare mode
							comparePressed++;
						} else{
							// convert to view mode
							compareBtn.setIcon(compare_icon);
							setMode(Mode.VIEW);
						}
					}
				}
			}
			
		});
		
		upBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("up button pressed.");
				
				if(leftPV.getXpressed() || rightPV.getXpressed()){
					//if at least xbutton pressed, can't be compare mode
					setMVbutton(false);
					leftPV.setXpressed(false);
					rightPV.setXpressed(false);

					return;
				}
				
			
			}
			
		});
		
		downBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("down button pressed.");
				
				if(leftPV.getXpressed() || rightPV.getXpressed()){
					//if at least xbutton pressed, can't be compare mode
					setMVbutton(false);
					leftPV.setXpressed(false);
					rightPV.setXpressed(false);

					return;
				}
				
			}
			
		});
		
		copyToLeftBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("copy to left button pressed.");
				
				if(leftPV.getXpressed() || rightPV.getXpressed()){
					//if at least xbutton pressed, can't be compare mode
					setMVbutton(false);
					leftPV.setXpressed(false);
					rightPV.setXpressed(false);

					return;
				}
				
			}
			
		});
		
		copyToRightBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("copy to right button pressed.");
				
				if(leftPV.getXpressed() || rightPV.getXpressed()){
					//if at least xbutton pressed, can't be compare mode
					setMVbutton(false);
					leftPV.setXpressed(false);
					rightPV.setXpressed(false);

					return;
				}
				
			}
			
		});
		
		toolPanel.setLayout(new GridLayout(1, 5));
		
		toolPanel.add(compareBtn);
		toolPanel.add(upBtn);
		toolPanel.add(downBtn);
		toolPanel.add(copyToLeftBtn);
		toolPanel.add(copyToRightBtn);
		
		holderPanel.setLayout(new GridLayout(1, 2, 0, 0));
		holderPanel.add(leftPV);
		holderPanel.add(rightPV);
		
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(toolPanel, BorderLayout.NORTH);
		mainPanel.add(holderPanel, BorderLayout.CENTER);
		
		this.add(mainPanel);
		this.pack();
		this.setSize(1200, 900);
		this.setVisible(true);
	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void load(PanelView mine, PanelView yours) {
		System.out.println("Load button pressed.");
		// Check if dirty!!!!!!!!!!!!!!
		checkUpdated(mine);
		// Load file via fileDialog
		FileDialog fd = new FileDialog(this, "Open File", FileDialog.LOAD);
		fd.setVisible(true);
		
		if (fd.getFile() != null) {
			String filePath = fd.getDirectory() + fd.getFile();
        	if (yours.pc.getFile() == null) {
        		if (mine.pc.load(filePath)) {
        			if (accept(mine.pc.getFile())){
        				mine.setMode(Mode.VIEW);
            			mine.myTextArea.setText(mine.pc.getFileContent());
            			mine.myfname.setText(mine.pc.getFile().getName());
            			mine.myfname.setFont(new Font("Arial",Font.BOLD,20));
        			}
        			else {
        				JOptionPane.showMessageDialog(null, "This file format is incorrect.", "ERROR!", JOptionPane.ERROR_MESSAGE);
        				System.out.println("This file format is incorrect.");
        			}
        		}
        	} else if (!filePath.equals(yours.pc.getFile().toString())) {
        		if(mine.pc.load(filePath)) {
        			if (mine.pc.load(filePath)) {
        				mine.setMode(Mode.VIEW);
            			mine.myTextArea.setText(mine.pc.getFileContent());
            			mine.myfname.setText(mine.pc.getFile().getName());
            			mine.myfname.setFont(new Font("Arial",Font.BOLD,20));
        			}
        			else {
        				JOptionPane.showMessageDialog(null, "This file format is incorrect.", "ERROR!", JOptionPane.ERROR_MESSAGE);
        				System.out.println("This file format is incorrect.");
        			}
        		}
        	} else {
        		JOptionPane.showMessageDialog(null, "File is already open in another panel.", "ERROR!", JOptionPane.ERROR_MESSAGE);
        		System.out.println("File is already open in another panel.");
        	}
        } else 
            System.out.println("File load canceled.");
		
		// set enable [compare/merge/traverse] button only when two panel loaded
		if(mine.pc.fileIsOpen() && yours.pc.fileIsOpen()){
			setMVbutton(true);
		}
        
	}
	
	public void setMVbutton(boolean tf){
		compareBtn.setEnabled(tf);
		upBtn.setEnabled(tf);
		downBtn.setEnabled(tf);
		copyToLeftBtn.setEnabled(tf);
		copyToRightBtn.setEnabled(tf);
	}
	
	
	// Check file format
	private boolean accept(File file) {
		if(file.isFile()) {
			String fileName = file.getName();
			if(fileName.endsWith(".txt")) return true; // txt format
		}
		
		return false;
	}

	private void setMode(Mode mode) {
		switch(mode) {
		case VIEW:
			leftPV.setMode(Mode.VIEW);
			rightPV.setMode(Mode.VIEW);
			break;
			
		case COMPARE:
			leftPV.setMode(Mode.COMPARE);
			rightPV.setMode(Mode.COMPARE);
			break;
		default:
			break;
		}
	}
	
	
	private int checkUpdated(PanelView pv) {
		Object[] options = {"Save", "Don't Save", "Cancel"};
		int n=0;
		
		if (pv.pc.isUpdated()) {
			n = JOptionPane.showOptionDialog(this, "The file has been edited. Do you want to save the file and continue?", "Question", 
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
			if (n == 0) { //YES: save and convert to view mode
				pv.save();
				
			} else if(n == 1) { //NO: not save, convert to view mode
				pv.pc.setUpdated(false);
				pv.pc.load(pv.pc.getFile().toString()); //show panel state before escapeBtn pressed
				
				
			} else if(n == 2){ //CANCLE: save, keep compare mode
				pv.pc.setUpdated(true);
				
			}
		}
		return n;
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Start!");
		
		MainView mv = new MainView();
		
		System.out.println("End!");
	}
}