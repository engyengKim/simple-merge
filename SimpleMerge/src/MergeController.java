import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/*�гΰ��� �޼ҵ带 �����ϴ� ��Ʈ�ѷ�(interpanel controller),
 * compare, traverse, merge �޼ҵ� ���� */
public class MergeController {

	private PanelController leftPanel;
	private PanelController rightPanel;
	private ArrayList<String> leftFileContents;
	private ArrayList<String> rightFileContents;
	/**
	 * �̰� �����ؾߵ�
	 */
	private int[] currentIndex;
	/**
	 * �� Panel�� difference index�� �����ϴ� �迭
	 * index�� 0�� �ƴ� 1���� ����!
	 * ���� ������ ���� �ٿ��� ������ index, �ٸ� ������ ���� �ٿ��� (-1)*index,
	 * �������� 0�� ����
	 */
	private ArrayList<Integer> leftDiffIndex;
	private ArrayList<Integer> rightDiffIndex;
	private ArrayList<Integer> leftViewIndex;
	private ArrayList<Integer> rightViewIndex;
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
		this.leftFileContents = new ArrayList<String>(Arrays.asList(array));
		this.leftFileContents.add(0, "");

		array = rightPanel.getFileContent().split("\r\n");
		this.rightFileContents = new ArrayList<String>(Arrays.asList(array));
		this.rightFileContents.add(0, "");
		
		/* FileComparator�� �̿��Ͽ� compare �� difference�� ������ index �����ޱ� */
		FileComparator fc = new FileComparator(leftFileContents, rightFileContents);
		this.leftDiffIndex = fc.getDiffLeft();
		this.rightDiffIndex = fc.getDIffRight();
		this.leftViewIndex = new ArrayList<Integer>();
		this.rightViewIndex = new ArrayList<Integer>();
		
		
		this.compare();
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
		this.leftFileContents.clear();
		this.leftFileContents.addAll(this.rightFileContents);
	}

	/**
	 * clear all the string and copy from left to right
	 */
	public void copyToRight() {
		this.rightFileContents.clear();
		this.rightFileContents.addAll(this.leftFileContents);
	}

	public void compare() {
		this.leftViewIndex.clear();
		this.rightViewIndex.clear();
		int L = 0, R = 0;
		int L_Max = this.leftDiffIndex.size();
		int R_Max = this.rightDiffIndex.size();
		
		while (L < L_Max && R < R_Max) {
			/* ���� string�� ��� �ش� string�� index�� ����*/
			if (this.leftDiffIndex.get(L) != -1 && this.rightDiffIndex.get(R) != -1) {
				this.leftViewIndex.add(L++);
				this.rightViewIndex.add(R++);				
			}
			/* �ٸ� string������ ���� line�� �ִ� ��� �ش� string�� index*(-1)�� ���� */
			else if (this.leftDiffIndex.get(L) == -1 && this.rightDiffIndex.get(R) == -1) {
				this.leftViewIndex.add(L * (-1)); L++;
				this.rightViewIndex.add(R * (-1)); R++;
			}
			/* ���� �гο��� ������ �����ϴ� ��� ������ �ش� �гο� �����ϰ� �ٸ� �гο��� ����(0)�� ���� */
			else {
				while (this.rightDiffIndex.get(R) == -1) {
					this.leftViewIndex.add(0);
					this.rightViewIndex.add(R++);
				}
				while (this.leftDiffIndex.get(L) == -1) {
					this.leftViewIndex.add(L++);
					this.rightViewIndex.add(0);
				}
			}
		}

	}

	public ArrayList<String> getLeftFileContents() {
		return this.leftFileContents;
	}

	public ArrayList<String> getRightFileContents() {
		return this.rightFileContents;
	}

	public ArrayList<Integer> getLeftDiffIndex() {
		return this.leftDiffIndex;
	}

	public ArrayList<Integer> getRightDiffIndex() {
		return this.leftDiffIndex;
	}
	
	public ArrayList<Integer> getLeftViewIndex() {
		return this.leftViewIndex;
	}
	
	public ArrayList<Integer> getRightViewIndex() {
		return this.rightViewIndex;
	}

	
	/**
	 * Used for the test of main class of this class.
	 * You can delete this method.
	 * It prints the index which includes blanks.
	 */
	public void printArranged() {
		System.out.println("Left Panel=========");
		for (int i = 1; i < this.leftViewIndex.size(); i++)
			System.out.println("["+i+"] "+this.leftViewIndex.get(i));

		System.out.println("\nRight Panel=========");
		for (int i = 1; i < this.rightViewIndex.size(); i++)
			System.out.println("["+i+"] "+this.rightViewIndex.get(i));
	}
	
	/**
	 * Used for the test of main class of this class.
	 * You can delete this method.
	 * It prints the index which does not include blanks.
	 */
	public void printAll() {
		System.out.println("Left Panel=========");
		for (int i = 1; i < this.leftFileContents.size(); i++)
			System.out.println(this.leftFileContents.get(i));

		System.out.println("\nRight Panel=========");
		for (int i = 1; i < this.rightFileContents.size(); i++)
			System.out.println(this.rightFileContents.get(i));
	}

	public static void main(String[] args) {
		/*
		 * This main is a test for this class. You can delete this if you don't need.
		 */
		Scanner s = new Scanner(System.in);
		int[] index = new int[2];
		PanelController left = new PanelController();
		PanelController right = new PanelController();

		left.setFileContent("same part1\r\n" + 
				"same part2\r\n" + 
				"different but same line.1\r\n" + 
				"different but same line.2\r\n" + 
				"same part3\r\n" + 
				"same part4\r\n" + 
				"same part5\r\n" + 
				"same part6\r\n" + 
				"different part-a\r\n" + 
				"different part-a\r\n" + 
				"different part-a\r\n" + 
				"same part7\r\n" + 
				"same part8");

		right.setFileContent("same part1\r\n" + 
				"same part2\r\n" + 
				"different but same line.3\r\n" + 
				"different but same line.4\r\n" + 
				"different part-b\r\n" + 
				"different part-b\r\n" + 
				"different part-b\r\n" + 
				"different part-b\r\n" + 
				"same part3\r\n" + 
				"same part4\r\n" + 
				"same part5\r\n" + 
				"different part-b\r\n" + 
				"different part-b\r\n" + 
				"same part6\r\n" + 
				"same part7\r\n" + 
				"same part8");

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
						System.out.println(mc.leftFileContents.get(i));
				}
				break;
			case 4:
				index = mc.traverseNext();
				if (index[0] != -1 && index[1] != -1) {
					System.out.println("Traverse line " + index[0] + " to " + index[1]);
					for (int i = index[0]; i <= index[1]; i++)
						System.out.println(mc.leftFileContents.get(i));
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