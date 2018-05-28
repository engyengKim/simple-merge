import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

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
	 * -1�� �����Ѵ�.
	 */
	private ArrayList<Integer> leftDiffIndex;
	private ArrayList<Integer> rightDiffIndex;
	private ArrayList<Integer> leftView;
	private ArrayList<Integer> rightView;
	private boolean hasTraversed;

	MergeController() {
		System.out.println("No input panels");
	}

	MergeController(PanelController leftPanel, PanelController rightPanel) {
		this.leftPanel = leftPanel;
		this.rightPanel = rightPanel;
		this.currentIndex = new int[] { -1, -1, -1, -1 };

		/* panel contents �޾ƿͼ� parsing �� arraylist�� ���� */
		String[] array = leftPanel.getFileContent().split("\r\n");
		this.leftFileController = new ArrayList<String>(Arrays.asList(array));

		array = rightPanel.getFileContent().split("\r\n");
		this.rightFileController = new ArrayList<String>(Arrays.asList(array));

		/* FileComparator�� �̿��Ͽ� compare �� difference�� ������ index �����ޱ� */
		FileComparator fc = new FileComparator(leftFileController, rightFileController);
		this.leftDiffIndex = fc.getDiffLeft();
		this.rightDiffIndex = fc.getDIffRight();
		this.leftView = new ArrayList<Integer>();
		this.rightView = new ArrayList<Integer>();
		this.arrange();
		this.hasTraversed = false;
	}

	/**
	 * previous = { left start point, left end point, right start point, right end
	 * point }
	 * 
	 * @return ���� index ������ index�� ���� -1�� �κ��� start, end index�� ã�Ƽ� return ã�� ���ϰų� ��
	 *         ó���� �ٴٸ��� currentIndex���� �ٽ� return
	 */
	public int[] traversePrevious() {
		int[] previous = new int[4];

		/* ó������ Ž���ϴ� ��� traverseNext���� return �� */
		if (this.hasTraversed == false) {
			previous = this.traverseNext();
			return previous;
		}

		for (int i = 0; i < 4; i++)
			this.currentIndex[i] = previous[i];
		this.hasTraversed = true;

		return previous;
	}

	/**
	 * @return ���� index ������ index�� ���� -1�� �κ��� start, end index�� ã�Ƽ� return ã�� ���ϰų�
	 *         �������� �ٴٸ��� currentIndex���� �ٽ� return
	 */
	public int[] traverseNext() {
		int[] next = new int[4];

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

	public void arrange() {
		this.leftView.clear();
		this.rightView.clear();
		int L = 0, R = 0;
		int L_Max = this.leftDiffIndex.size();
		int R_Max = this.rightDiffIndex.size();
		
		while (L < L_Max && R < R_Max) {
			/* ���� string�� ��� */
			if (this.leftDiffIndex.get(L) != -1 && this.rightDiffIndex.get(R) != -1) {
				this.leftView.add(L++);
				this.rightView.add(R++);				
			}
			else if (this.leftDiffIndex.get(L) == -1 && this.rightDiffIndex.get(R) == -1) {
				this.leftView.add(-2); L++;
				this.rightView.add(-2); R++;
			}
			else {
				/* ������ �гο� �ٸ� �κ��� �ִ� ��� ���� �гο� �ٸ� line �� ��ŭ �������� �־���*/
				while (this.rightDiffIndex.get(R) == -1) {
					this.leftView.add(-1);
					this.rightView.add(R++);
				}
				/* ���� �гο� �ٸ� �κ��� �ִ� ��� ������ �гο� �ٸ� line �� ��ŭ �������� �־���*/
				while (this.leftDiffIndex.get(L) == -1) {
					this.leftView.add(L++);
					this.rightView.add(-1);
				}
			}
		}

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

	public void printArranged() {
		System.out.println("Left Panel=========");
		for (int i = 0; i < this.leftView.size(); i++)
			System.out.println("["+i+"] "+this.leftView.get(i));

		System.out.println("\nRight Panel=========");
		for (int i = 0; i < this.rightView.size(); i++)
			System.out.println("["+i+"] "+this.rightView.get(i));
	}
	
	public void printAll() {
		System.out.println("Left Panel=========");
		for (int i = 0; i < this.leftFileController.size(); i++)
			System.out.println(this.leftFileController.get(i));

		System.out.println("\nRight Panel=========");
		for (int i = 0; i < this.rightFileController.size(); i++)
			System.out.println(this.rightFileController.get(i));
	}

	public static void main(String[] args) {
		/*
		 * This main is a test for this class. You can delete this if you don't need.
		 */
		Scanner s = new Scanner(System.in);
		int[] index = new int[2];
		PanelController left = new PanelController();
		PanelController right = new PanelController();

		left.setFileContent(
				"same part1\r\n" + "same part2\r\n" +  "different part a\r\n" + "same part3");

		right.setFileContent("same part1\r\n" + "same part2\r\n" 
				+ "different part b\r\n" + "same part3");

		MergeController mc = new MergeController(left, right);

		boolean iterate = true;
		while (iterate) {
			System.out.println("\n1. print");
			System.out.println("2. print arranged");
			System.out.println("3. traverse previous");
			System.out.println("4. traverse next");
			System.out.println("5. copy to left");
			System.out.println("6. copy to right");
			System.out.println("7. exit");
			System.out.print("Select menu: ");
			int menu = s.nextInt();
			switch (menu) {

			case 1:
				mc.printAll();
				break;
			case 2:
				mc.printArranged();
				break;
			case 3:
				index = mc.traversePrevious();
				if (index[0] != -1 && index[1] != -1) {
					System.out.println("Traverse line " + index[0] + " to " + index[1]);
					for (int i = index[0]; i <= index[1]; i++)
						System.out.println(mc.leftFileController.get(i));
				}
				break;
			case 4:
				index = mc.traverseNext();
				if (index[0] != -1 && index[1] != -1) {
					System.out.println("Traverse line " + index[0] + " to " + index[1]);
					for (int i = index[0]; i <= index[1]; i++)
						System.out.println(mc.leftFileController.get(i));
				}
				break;
			case 5:
				mc.copyToLeft();
				break;
			case 6:
				mc.copyToRight();
				break;
			case 7:
				iterate = false;
				break;
			default:
				System.out.println("wrong choice");
				continue;
			}

		}
		System.out.println("Exit Program.");
		s.close();
	}

}