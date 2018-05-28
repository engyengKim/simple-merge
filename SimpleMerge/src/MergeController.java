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
	 * currentIndex[2] = (start index, end index) ���� �ٸ� �κ��� ���۰� ���� ����Ű�� index�迭 (-1,
	 * -1)�� �ʱ�ȭ��
	 */
	private int[] currentIndex;
	/**
	 * �� Panel�� difference index�� �����ϴ� �迭 ���� ������ ���� ���� ������ index�� �����ϰ� �ٸ� ������ ���� �ٿ���
	 * -1�� �����Ѵ�. ex) <Left> <Right> 0 abcdef abcdef 1 aaa bbb 2 same part same part
	 * 3 diff part for a hihihi 4 hihihi
	 * 
	 * <leftDiffIndex> <rightDiffIndex> [0] abcdef [0] abcdef [-1] aaa [-1] bbb [2]
	 * same part [2] same part [-1] diff ���� a [4] hihihi [3] hihihi
	 */
	private ArrayList<Integer> leftDiffIndex;
	private ArrayList<Integer> rightDiffIndex;

	MergeController() {
		System.out.println("No input panels");
	}

	MergeController(PanelController leftPanel, PanelController rightPanel) {
		this.leftPanel = leftPanel;
		this.rightPanel = rightPanel;
		this.currentIndex = new int[] { -1, -1 };

		/* panel contents �޾ƿͼ� parsing �� arraylist�� ���� */
		String[] array = leftPanel.getFileContent().split("\r\n");
		leftFileController = new ArrayList<String>(Arrays.asList(array));

		array = rightPanel.getFileContent().split("\r\n");
		rightFileController = new ArrayList<String>(Arrays.asList(array));

		/* FileComparator�� �̿��Ͽ� compare �� difference�� ������ index �����ޱ� */
		FileComparator fc = new FileComparator(leftFileController, rightFileController);
		leftDiffIndex = fc.getDiffLeft();
		rightDiffIndex = fc.getDIffRight();
	}

	/**
	 * @return ���� index ������ index�� ���� -1�� �κ��� start, end index�� ã�Ƽ� return 
	 * 			ã�� ���ϰų� �� ó���� �ٴٸ���  currentIndex���� �ٽ� return
	 */
	public int[] traversePrevious() {
		int index = this.currentIndex[0];
		int[] previous = new int[2];

		/* ���� ���� �κ��̸� �� �κ��� �ٽ� ��ȯ */
		if (this.currentIndex[0] < 1 || this.currentIndex[1] < 1)
			return this.currentIndex;

		/* currentIndex ������ index�� -1���� ���� ���� ����� index�� end index�� ���� */
		while (index >= 0) {
			index--;
			if (this.leftDiffIndex.get(index) == -1)
				previous[1] = index;
		}

		/* end index ������ index �� ���� ó������ ���� �κ��� ��Ÿ���� index�� ���� index�� start index�� ���� */
		while (index >= 0) {
			index--;
			if (this.leftDiffIndex.get(index) != -1)
				previous[0] = index + 1;
		}

		return previous;
	}

	
	/**
	 * @return ���� index ������ index�� ���� -1�� �κ��� start, end index�� ã�Ƽ� return 
	 * 			ã�� ���ϰų� �������� �ٴٸ���  currentIndex���� �ٽ� return
	 */
	public int[] traverseNext(int currentIndex) {
		int index = this.currentIndex[1];
		int[] next = new int[2];
		int maxIndex = (this.leftFileController.size() > this.rightFileController.size())
				? (this.leftFileController.size() - 1)
				: (this.rightFileController.size() - 1);

		/* ���� ���� �κ��̸� �� �κ��� �ٽ� ��ȯ */
		if (this.currentIndex[0] == maxIndex || this.currentIndex[1] == maxIndex)
			return this.currentIndex;

		/* currentIndex ������ index�� -1���� ���� ���� ����� index�� start index�� ���� */
		while (index <= maxIndex) {
			index++;
			if (this.leftDiffIndex.get(index) == -1)
				next[0] = index;
		}

		/* end index ������ index �� ���� ó������ ���� �κ��� ��Ÿ���� index�� ���� index�� end index�� ���� */
		while (index <= maxIndex) {
			index++;
			if (this.leftDiffIndex.get(index) != -1)
				next[0] = index - 1;
		}

		return next;
	}

	/**
	 * clear all the string and copy from right to left
	 */
	public void copyToLeft() {
		this.leftFileController.clear();
		this.leftFileController.addAll(this.rightFileController);
	}

	/**
	 * clear all the string and copy from left to right
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

	public static void main(String[] args) {
		/*
		 * This main is a test for this class. You can delete this if you don't need.
		 */

	}
}
