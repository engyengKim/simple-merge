import java.io.*;
import java.util.ArrayList;

/*�гΰ��� �޼ҵ带 �����ϴ� ��Ʈ�ѷ�(interpanel controller),
 * compare, traverse, merge �޼ҵ� ���� */
public class MergeController {
	
	private PanelInfo leftPanel;
	private PanelInfo rightPanel;
	private ArrayList<String> leftFileController;
	private ArrayList<String> rightFileController;
	private FileComparator fc;
	private int[] diffIndex;
	
	MergeController(){
		System.out.println("No input panels");
	}
	
	MergeController(PanelInfo leftPanel, PanelInfo rightPanel) {
		this.leftPanel = leftPanel;
		this.rightPanel = rightPanel;
		/* left, right Panel�� �Ӽ����� String�� �޾ƿͼ�
		 * ArrayList�� ��������
		 * �׸��� diffIndex�� ����
		 * FileComparator ����
		 */
	}
	
	int[] getDiffIndex() {
		return this.diffIndex;
	}
	
	int traversePrevious() {
		
		int index = -1;
		// TODO
		
		return index;
	}
	
	int traverseNext() {
		
		int index = -1;
		// TODO
		
		return index;
	}
	
	void copyToLeft() {
		// TODO
	}
	
	void copyToRight() {
		// TODO
	}	
}
