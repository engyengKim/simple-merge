import java.util.ArrayList;

public class FileComparator {
	/**
	 * LCS length�� �����ϴ� �迭
	 * sequence alignment �˰����̳� LCS �˰��� ����
	 */
	private int[][] C;
	/**
	 * �� Panel�� difference index�� �����ϴ� �迭
	 * ���� ������ ���� ���� ������ index�� �����ϰ�
	 * �ٸ� ������ ���� �ٿ��� -1�� �����Ѵ�.
	 * ex)
	 * <Left>	<leftDiffIndex>	<Right>	<rightDiffIndex>
	 * abc		0				abc		0
	 * de		-1				fgh		2
	 * fgh		1
	 */
	private ArrayList<Integer> leftDiffIndex;
	private ArrayList<Integer> rightDiffIndex;
	private final Integer diff = -1;

	public FileComparator(ArrayList<String> left, ArrayList<String> right) {
		leftDiffIndex = new ArrayList<Integer>();
		rightDiffIndex = new ArrayList<Integer>();
		
		this.LCSLength(left, right);
		this.computeDiff(this.C, left, right, left.size(), right.size());
	}

	/**
	 * Computing the length of the LCS sequence alignment�� ���� �˰��� ���̰� m�� text1�� ���̰�
	 * n�� text2�� �Է¹޾� ��� 1 <= i <= m �� 1 <= j <= n�� ���� text1[1..i] �� text2[1..j] ������
	 * ���� ���� LCS�� �����ϰ�, C[i, j]�� �����Ѵ�. C[m, n]�� text1�� text2�� ���� LCS ���� ������ �ȴ�.
	 */
	public int LCSLength(ArrayList<String> left, ArrayList<String> right) {
		int m = left.size(); 
		int n = right.size();
		C = new int[m + 1][n + 1];

		/* ���̳� ���� index�� 0�� element�� 0���� �ʱ�ȭ�Ѵ� */
		for (int i = 0; i <= m; i++)
			C[i][0] = 0;
		for (int j = 0; j <= n; j++)
			C[0][j] = 0;

		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= n; j++) {
				if (left.get(i - 1).equals(right.get(j - 1)))
					C[i][j] = C[i - 1][j - 1] + 1;
				else
					C[i][j] = (C[i][j - 1] > C[i - 1][j]) ? C[i][j - 1] : C[i - 1][j];
			}
		}
		return C[m][n];
	}

	/**
	 * @param C: matrix of LCS length between text 1 and text2
	 * @param left
	 * @param right
	 * @param i: length(size) of text1
	 * @param j: length(size) of text2
	 */
	public void computeDiff(int[][] C, ArrayList<String> left, ArrayList<String> right, int i, int j) {
		this.leftDiffIndex.clear();
		this.rightDiffIndex.clear();
		/* if two strings have same line, store mutual index */
		if (i > 0 && j > 0 && left.get(i - 1).equals(right.get(j - 1))) {
			computeDiff(C, left, right, i - 1, j - 1);
			this.leftDiffIndex.add(i - 1, j - 1);
			this.rightDiffIndex.add(j - 1, i - 1);
		}
		
		/* if two strings have different line, store diff(-1) */
		else if (j > 0 && (i == 0 || C[i][j - 1] >= C[i - 1][j])) {
			computeDiff(C, left, right, i, j - 1);
			this.rightDiffIndex.add(j - 1, diff);
		} 
		else if (i > 0 && (j == 0 || C[i][j - 1] < C[i - 1][j])) {
			computeDiff(C, left, right, i - 1, j);
			this.leftDiffIndex.add(i - 1, diff);
		}
	}

	/**
	 * @return C
	 */
	public int[][] getC() {
		return this.C;
	}
	
	/**
	 * @return diffLeft
	 */
	public ArrayList<Integer> getDiffLeft(){
		return this.leftDiffIndex;
	}
	
	/**
	 * @return diffRight
	 */
	public ArrayList<Integer> getDIffRight(){
		return this.rightDiffIndex;
	}

	public static void main(String[] args) {
		/* This main is a test for this class.
		 * You can delete this if you don't need.
		 * */
		ArrayList<String> s1 = new ArrayList<String>();
		ArrayList<String> s2 = new ArrayList<String>();
		s1.add("same part1");
		s1.add("same part2");
		s1.add("same part3");
		s1.add("different part-a");
		s1.add("different part-a");
		s1.add("same part4");
		s1.add("same part5");
		s1.add("same part6");
		s1.add("different part-a");
		s1.add("different part-a");
		s1.add("different part-a");
		s1.add("same part7");
		s1.add("same part8");
		s1.add(0, "");

		s2.add("same part1");
		s2.add("same part2");
		s2.add("different part-b");
		s2.add("different part-b");
		s2.add("different part-b");
		s2.add("different part-b");
		s2.add("same part3");
		s2.add("same part4");
		s2.add("same part5");
		s2.add("different part-b");
		s2.add("different part-b");
		s2.add("same part6");
		s2.add("same part7");
		s2.add("same part8");
		s2.add(0, "");


		FileComparator fc = new FileComparator(s1, s2);
		fc.LCSLength(s1, s2);
		fc.computeDiff(fc.getC(), s1, s2, s1.size(), s2.size());

		System.out.println("Left Panel=========");
		for (int i = 1; i < s1.size(); i++) {
			System.out.println("["+i+"](="+fc.leftDiffIndex.get(i)+")\t"+s1.get(i));
		}

		System.out.println("\nRight Panel=========");
		for (int i = 1; i < s2.size(); i++) {
			System.out.println("["+i+"](="+fc.rightDiffIndex.get(i)+")\t"+s2.get(i));
		}

	}
}