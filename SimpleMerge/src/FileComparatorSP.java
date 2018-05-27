import java.util.ArrayList;

public class FileComparatorSP {
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
	 * <Left>	<diffLeft>	<Right>	<diffRight>
	 * abc		0			abc		0
	 * de		-1			fgh		2
	 * fgh		1
	 */
	private ArrayList<Integer> diffLeft;
	private ArrayList<Integer> diffRight;

	FileComparatorSP() {
		
		System.out.println("We need two files");
	}

	FileComparatorSP(String s1, String s2){
		
		LCSLength(s1.toCharArray(),s2.toCharArray());
		diffLeft = new ArrayList<Integer>();
		diffRight = new ArrayList<Integer>();
		compare();
	}
	
	/**
	 * Computing the length of the LCS sequence alignment�� ���� �˰��� ���̰� m�� text1�� ���̰�
	 * n�� text2�� �Է¹޾� ��� 1 <= i <= m �� 1 <= j <= n�� ���� text1[1..i] �� text2[1..j] ������
	 * ���� ���� LCS�� �����ϰ�, C[i, j]�� �����Ѵ�. C[m, n]�� text1�� text2�� ���� LCS ���� ������ �ȴ�.
	 */
	void LCSLength(char[] text1, char[] text2) {
		int m = text1.length;
		int n = text2.length;
		C = new int[m + 1][n + 1];

		/* ���̳� ���� index�� 0�� element�� 0���� �ʱ�ȭ�Ѵ� */
		for (int i = 0; i <= m; i++)
			C[i][0] = 0;
		for (int j = 0; j <= n; j++)
			C[0][j] = 0;

		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= n; j++) {
				if (text1[i - 1] == text2[j - 1])
					C[i][j] = C[i - 1][j - 1] + 1;
				else
					C[i][j] = (C[i][j - 1] > C[i - 1][j]) ? C[i][j - 1] : C[i - 1][j];
			}
		}
		;
	}
	
	/*
	 diffLeft�� diffRight���� ���� �κ��� ������ index�� ����. ���, �� ArrayList�� size�� ���� �ȴ�.
	 (�̷��� �ϴ� ������ MergeController ���� �� �� �ִ�.)
	 */
	void compare(){
		
		int i = C.length-1;										//Last index of rows
		int j = C[0].length-1;									//Last index of columns
		
		
		while(C[i][j]!=0){										//terminate at 0th row, 0th column
			
			if(C[i][j] == C[i][j-1]){							
				
				j--;
			}
			
			else if(C[i][j] == C[i-1][j]){
				
				i--;
			}
			
			else{
				
				this.diffLeft.add(0,i-1);						//i�� �ƴ� i-1�� diffLeft index�� ����
				this.diffRight.add(0,j-1);						//j�� �ƴ� j-1�� diffRight index�� ����
				
				i--;
				j--;
			}
		}
	}

	
	/**
	 * @return diffLeft
	 */
	ArrayList<Integer> getDiffLeft(){
		return this.diffLeft;
	}
	
	/**
	 * @return diffRight
	 */
	ArrayList<Integer> getDiffRight(){
		return this.diffRight;
	}

	public static void main(String[] args) {
		/* This main is a test for this class.
		 * You can delete this if you don't need.
		 * */
		FileComparatorSP fc = new FileComparatorSP("AGGTAB","GXXTAYYB");					//�� ������θ� ���� {1,3,4,5} & {0,3,4,7}
		ArrayList<Integer> s1 = fc.getDiffLeft();
		ArrayList<Integer> s2 = fc.getDiffRight();

		System.out.println("Left Panel=========");
		for (int i = 0; i < s1.size(); i++) {
			System.out.println(s1.get(i));
		}

		System.out.println("\nRight Panel=========");
		for (int i = 0; i < s2.size(); i++) {
			System.out.println(s2.get(i));
		}

	}
}
