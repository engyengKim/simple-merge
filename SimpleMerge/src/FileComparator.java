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

	FileComparator(ArrayList<String> left, ArrayList<String> right) {
		leftDiffIndex = new ArrayList<Integer>();
		rightDiffIndex = new ArrayList<Integer>();
		
		this.LCSLength(left, right);
		this.compare();
	}

	/**
	 * Computing the length of the LCS sequence alignment�� ���� �˰��� ���̰� m�� text1�� ���̰�
	 * n�� text2�� �Է¹޾� ��� 1 <= i <= m �� 1 <= j <= n�� ���� text1[1..i] �� text2[1..j] ������
	 * ���� ���� LCS�� �����ϰ�, C[i, j]�� �����Ѵ�. C[m, n]�� text1�� text2�� ���� LCS ���� ������ �ȴ�.
	 */
	int LCSLength(ArrayList<String> left, ArrayList<String> right) {
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
	void compare() {
		/* if two strings have same line, store mutual index */
		int i = C.length-1;
		int j = C[0].length-1;
		
		while(C[i][j] != 0){
			
			if(C[i][j] == C[i-1][j]){
				
				i--;
			}
			
			else if(C[i][j] == C[i][j-1]){
				
				j--;
			}
			
			else{
				
				leftDiffIndex.add(0,i-1);						//i�� �ƴ� i-1�� diffLeft index�� ����
				rightDiffIndex.add(0,j-1);						//j�� �ƴ� j-1�� diffRight index�� ����
				i--;
				j--;
				
			}
		}
	}

	void arrange() {
		
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
	
	/**
	 * @return diffLeft
	 */
	ArrayList<Integer> getDiffLeft(){
		return this.leftDiffIndex;
	}
	
	/**
	 * @return diffRight
	 */
	ArrayList<Integer> getDIffRight(){
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

		FileComparator fc = new FileComparator(s1, s2);

		System.out.println("Left Panel=========");
		for (int i = 0; i < fc.leftDiffIndex.size(); i++) {
			System.out.println("["+i+"](="+fc.leftDiffIndex.get(i)+")");
		}

		System.out.println("\nRight Panel=========");
		for (int i = 0; i < fc.rightDiffIndex.size(); i++) {
			System.out.println("["+i+"](="+fc.rightDiffIndex.get(i)+")");
		}

	}
}