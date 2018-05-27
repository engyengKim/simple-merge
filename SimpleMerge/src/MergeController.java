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
	/**
	 * �� Panel�� difference index�� �����ϴ� �迭
	 * ���� ������ ���� ���� ������ index�� �����ϰ�
	 * �ٸ� ������ ���� �ٿ��� -1�� �����Ѵ�.
	 * ex)
	 * 		<Left>			<Right>	
	 * 0	abcdef			abc		
	 * 1	aaa				bbb		
	 * 2	same part		same part
	 * 3	diff part for a	hihihi
	 * 4	hihihi
	 * 
	 * <leftDiffIndex>	<rightDiffIndex>
	 * [0] abcdef		[0] abcdef
	 * [-1] aaa			[-1] bbb
	 * [2] same part	[2] same part
	 * [-1] diff ���� a	[4] hihihi
	 * [3] hihihi
	 */
	private ArrayList<Integer> leftDiffIndex;
	private ArrayList<Integer> rightDiffIndex;
	
	
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
	
		/* FileComparator�� �̿��Ͽ� compare �� difference�� ������ index �����ޱ�*/
		FileComparator fc = new FileComparator(leftFileController, rightFileController);
		leftDiffIndex = fc.getDiffLeft();
		rightDiffIndex = fc.getDIffRight();
	}
	
	/**
	 * @param currentIndex: current index value
	 * @return ���� index ������ index�� ���� -1�� index�� ã�� return��
	 * 		index�� 0�� �� ������ �ٸ� �κ��� ã�� ���ϸ� currentIndex���� �ٽ� return
	 */
	public int traversePrevious(int currentIndex) {
		int index = currentIndex;
		
		while(index >= 0) {
			index--;
			if(index == -1)
				return index;
		}
		
		return currentIndex;
	}
	
	/**
	 * @param currentIndex: current index value
	 * @return ���� index ������ index�� ���� -1�� index�� ã�� return��
	 * 		index�� �������� �ٴٸ� ������ �ٸ� �κ��� ã�� ���ϸ� currentIndex���� �ٽ� return
	 */
	public int traverseNext(int currentIndex) {
		int index = currentIndex;
		int maxIndex = (this.leftFileController.size() > this.rightFileController.size()) ?
				this.leftFileController.size() : this.rightFileController.size();
		
		while(index < maxIndex) {
			index++;
			if(index == -1)
				return index;
		}
		
		return currentIndex;
	}
	
	
	/**
	 *	clear all the string and copy from right to left 
	 */
	public void copyToLeft() {
		this.leftFileController.clear();
		this.leftFileController.addAll(this.rightFileController);
	}
	

	/**
	 *	clear all the string and copy from left to right
	 */
	public void copyToRight() {
		this.rightFileController.clear();
		this.rightFileController.addAll(this.leftFileController);
	}	
	
	public ArrayList<String> getLeftFileController() {
		return this.leftFileController;
	}

	public ArrayList<String> getRightFileController() {
		return this.rightFileController;
	}
	
	public ArrayList<Integer> getLeftDiffIndex() {
		return this.leftDiffIndex;
	}
	
	public ArrayList<Integer> getRightDiffIndex() {
		return this.leftDiffIndex;
	}
}
