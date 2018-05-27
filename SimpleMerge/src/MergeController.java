import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/*�гΰ��� �޼ҵ带 �����ϴ� ��Ʈ�ѷ�(interpanel controller),
 * compare, traverse, merge �޼ҵ� ���� */
public class MergeController {
	
	private PanelController leftPanel;
	private PanelController rightPanel;
	private ArrayList<String> leftFileController;
	private ArrayList<String> rightFileController;
	private FileComparator fc;
	private int[] diffIndex;
	
	MergeController(){
		System.out.println("No input panels");
	}
	
	MergeController(PanelController leftPanel, PanelController rightPanel) {
		this.leftPanel = leftPanel;
		this.rightPanel = rightPanel;
		
		/* panel contents �޾ƿͼ� parsing �� arraylist��  ���� */
		String[] array = leftPanel.getFileContent().split("\r\n");
		leftFileController = new ArrayList<String>(Arrays.asList(array));

		array = rightPanel.getFileContent().split("\r\n");
		rightFileController = new ArrayList<String>(Arrays.asList(array));
		
		/* left, right Panel�� �Ӽ����� String�� �޾ƿͼ�
		 * ArrayList�� ��������
		 * �׸��� diffIndex�� ����
		 * FileComparator ����
		 */
	}
	
	public int[] getDiffIndex() {
		return this.diffIndex;
	}
	
	public int traversePrevious() {
		
		int index = -1;
		// TODO
		
		return index;
	}
	
	public int traverseNext() {
		
		int index = -1;
		// TODO
		
		return index;
	}
	
	public void copyToLeft() {
		// TODO
	}
	
	public void copyToRight() {
		// TODO
	}	
}
