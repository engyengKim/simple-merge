import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class CompareTableRenderer extends DefaultTableCellRenderer {

	private ArrayList<Integer> arri = new ArrayList<Integer>();
	private Color lineColor;

	@SuppressWarnings("unchecked")
	public CompareTableRenderer(ArrayList<Integer> arr, Color color) {
		this.arri = (ArrayList<Integer>) arr.clone();
		this.lineColor = color;
	}

	public Component getTableCellRendererComponent(JTable table, Object object, boolean isSelected, boolean hasFocus,
			int row, int col) {
		Component cell = super.getTableCellRendererComponent(table, object, isSelected, hasFocus, row, col);

		this.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		
		if (!isSelected) {
			if (arri.contains(row + 1)) {
				cell.setBackground(lineColor);
			} else
				cell.setBackground(Color.white);
		}
		return cell;
	}
}