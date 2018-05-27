import java.util.ArrayList;

public class FileComparator {
	private int[][] C;
	
	/** <sequence alignment>
	 * ���̰� m�� text1�� ���̰� n�� text2�� �Է¹޾� ��� 1 <= i <= m �� 1 <= j <= n�� ����
	 * text1[1..i] �� text2[1..j] ������ ���� ���� LCS�� �����ϰ�,
	 * C[i, j]�� �����Ѵ�. C[m, n]�� text1�� text2�� ���� LCS ���� ������ �ȴ�.
	 */
	public int LCSLength(ArrayList<String> text1, ArrayList<String> text2) {
		int m = text1.size();
		int n = text2.size();
		C = new int[m + 1][n + 1];
		
		/* ���̳� ���� index�� 0�� element�� 0���� �ʱ�ȭ�Ѵ� */
		for (int i = 0; i <= m; i++)
			C[i][0] = 0;
		for (int j = 0; j <= n; j++)
			C[0][j] = 0;
	
		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= n; j++) {
				if (text1.get(i).equals(text2.get(j)))
					C[i][j] = C[i-1][j-1] + 1;
				else
					C[i][j] = (C[i][j-1] > C[i-1][j])? C[i][j-1] : C[i-1][j];
			}
		}
		
		return C[m][n];
	}
	
	/**
	 *	backtracking�� �̿��Ͽ� C�� ǥ�� �����Ѵ�. 
	 */
	public String backtrack(int[][] C, ArrayList<String> text1, ArrayList<String> text2, int i, int j) {
		if (i == 0 || j == 0)
			return "";
		else if (text1.get(i).equals(text2.get(j)))
			return backtrack(C, text1, text2, i - 1, j - 1) + text1.get(i);
		else {
			if (C[i][j - 1] > C[i - 1][j])
				return backtrack(C, text1, text2, i, j-1);
			else
				return backtrack(C, text1, text2, i-1, j);
		}
	}
	
	
}