import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

public class CompareTable extends JTable {
	private DefaultTableModel model;
	private Color highlightColor;
	private Color focusColor;

	private CompareTableRenderer renderer;

	public CompareTable() {

	}

	public CompareTable(ArrayList<String> fileContentList, ArrayList<int[]> blocks, ArrayList<Integer> diffIndices, Color highlightColor, Color focusColor) {
		super();

		// Set highlight color
		this.highlightColor = highlightColor;
		this.focusColor = focusColor;
		
		// Set header
		Vector<String> head = new Vector<String>();
		head.addElement("line");
		head.addElement("Content");
		model = new DefaultTableModel(head, 0);

		// Set contents
		int ctr = 0;
		for (int i = 1; i < fileContentList.size(); i++) {
			Vector<String> contents = new Vector<String>();
			if (diffIndices.get(i) == 0) {
				contents.addElement("-");
			}
			else {
			contents.addElement(String.valueOf(++ctr));
			}
			contents.addElement(fileContentList.get(i));
			model.addRow(contents);
		}

		// Highlight Lines
		highlightBlocks(blocks, diffIndices);
		System.out.println(diffIndices);

		this.setModel(model);

		// Set Color or Grid and Header
		this.setShowHorizontalLines(false);
		this.setGridColor(Color.LIGHT_GRAY);
		JTableHeader header = this.getTableHeader();
		header.setBackground(Color.WHITE);

		// TODO column width
		// Set column size
		TableColumnModel col = this.getColumnModel();
		col.getColumn(0).setPreferredWidth(40);
		col.getColumn(1).setPreferredWidth(550);
		this.setRowHeight(20);
		TableColumnModel columnModel = this.getColumnModel();  
				int width = 545, col1 = 1;
				// Min width 
				for (int row = 0; row < this.getRowCount(); row++) { 
					TableCellRenderer renderer = this.getCellRenderer(row, col1); 
					Component comp = this.prepareRenderer(renderer, row, col1); 
					width = Math.max(comp.getPreferredSize().width +1 , width); 
				} 
				columnModel.getColumn(col1).setPreferredWidth(width); 

		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}

	public void highlightBlocks(ArrayList<int[]> blocks, ArrayList<Integer> greyIndices, int traverseIndex) {

		ArrayList<Integer> highlightIndices = new ArrayList<Integer>();
		
		for (int i = 0; i < blocks.size(); i++) {
			int[] block = blocks.get(i);
			int start = block[0];
			int end = block[1];

			for (int j = start; j <= end; j++) {
				if (i == traverseIndex)
					highlightIndices.add(-j);
				else 
					highlightIndices.add(j);
			}
			
		}
		renderer = new CompareTableRenderer(highlightIndices, greyIndices, highlightColor, focusColor);
		try {
			this.setDefaultRenderer(Class.forName("java.lang.Object"), renderer);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		repaint();

	}
	
	public void updateModel(ArrayList<String> fileContentList, ArrayList<int[]> blocks, ArrayList<Integer> diffIndices) {
		Vector<String> head = new Vector<String>();
		head.addElement("line");
		head.addElement("Content");
		DefaultTableModel model = new DefaultTableModel(head, 0);

		// Set contents
		int ctr = 0;
		for (int i = 1; i < fileContentList.size(); i++) {
			Vector<String> contents = new Vector<String>();
			if (diffIndices.get(i) == 0) {
				contents.addElement("-");
			}
			else {
			contents.addElement(String.valueOf(++ctr));
			}
			contents.addElement(fileContentList.get(i));
			model.addRow(contents);
		}

		// Highlight Lines
		highlightBlocks(blocks, diffIndices);

		this.setModel(model);
		
		// Set Color or Grid and Header
		this.setShowHorizontalLines(false);
		this.setGridColor(Color.LIGHT_GRAY);
		JTableHeader header = this.getTableHeader();
		header.setBackground(Color.WHITE);

		// TODO column width
		// Set column size
		TableColumnModel col = this.getColumnModel();
		col.getColumn(0).setPreferredWidth(40);
		col.getColumn(1).setPreferredWidth(550);
		this.setRowHeight(20);
	}
	
	public void highlightBlocks(ArrayList<int[]> blocks, ArrayList<Integer> greyIndices) {

		highlightBlocks(blocks, greyIndices, 0);

	}


	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}

}