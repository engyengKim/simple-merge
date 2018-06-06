import java.util.ArrayList;

public class FileComparator {
   /**
    * LCS length�� �����ϴ� �迭 sequence alignment �˰����̳� LCS �˰��� ����
    */
   private int[][] C;
   /**
    * �� Panel�� difference index�� �����ϴ� �迭 ���� ������ ���� ���� ������ index�� �����ϰ� �ٸ� ������ ���� �ٿ���
    * -1�� �����Ѵ�. ex) <Left> <leftDiffIndex> <Right> <rightDiffIndex> abc 0 abc 0 de
    * -1 fgh 2 fgh 1
    */
   private ArrayList<Integer> leftDiffIndex;
   private ArrayList<Integer> rightDiffIndex;
   private ArrayList<int[]> blocks;

   FileComparator(ArrayList<String> left, ArrayList<String> right) {
      leftDiffIndex = new ArrayList<Integer>();
      rightDiffIndex = new ArrayList<Integer>();
      blocks = new ArrayList<int[]>();

      LCSLength(left, right);
      compare();
      arrange();
      computeBlocks();
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
    * @param C:
    *            matrix of LCS length between text 1 and text2
    * @param left
    * @param right
    * @param i:
    *            length(size) of text1
    * @param j:
    *            length(size) of text2
    */
   private void compare() {
      /* if two strings have same line, store mutual index */
	  boolean flag =  C.length < C[0].length;
	   
      if( flag ){
    	int[][] B = new int[C[0].length][C.length];
    	for(int a = 0; a < C[0].length; a++){
    		for(int b =0; b < C.length; b++){
    			B[a][b] = C[b][a];
    		}
    	}
    	C=B;
      }
      
      int i = C.length - 1;
      int j = C[0].length - 1;
      
      while (C[i][j] != 0) {
         if (C[i][j] == C[i - 1][j]) {			//decrease row
             leftDiffIndex.add(0, -1);
             i--;
          }

         else if (C[i][j] == C[i][j - 1]) {		//decrease column
            rightDiffIndex.add(0, -1);
            j--;
         }

         else {
            leftDiffIndex.add(0, i);
            rightDiffIndex.add(0, j);
            i--;
            j--;

         }
      }

      while (i != 0) {

         leftDiffIndex.add(0, -1);
         i--;
      }

      while (j != 0) {

         rightDiffIndex.add(0, -1);
         j--;
      }

      leftDiffIndex.add(0, 0);
      rightDiffIndex.add(0, 0);
      
      if(flag){
    	  ArrayList<Integer> temp;
    	  temp = leftDiffIndex;
    	  leftDiffIndex = rightDiffIndex;
    	  rightDiffIndex = temp;
      }
   }

   private void arrange() {

      ArrayList<Integer> leftViewIndex = new ArrayList<Integer>();
      ArrayList<Integer> rightViewIndex = new ArrayList<Integer>();

      int L = 0, R = 0;
      int L_Max = leftDiffIndex.size();
      int R_Max = rightDiffIndex.size();
      while (L < L_Max && R < R_Max) {
         /* ���� string�� ��� �ش� string�� index�� ���� */
         if (this.leftDiffIndex.get(L) != -1 && this.rightDiffIndex.get(R) != -1) {
            leftViewIndex.add(L++);
            rightViewIndex.add(R++);
         }
         /* �ٸ� string������ ���� line�� �ִ� ��� �ش� string�� index*(-1)�� ���� */
         else if (this.leftDiffIndex.get(L) == -1 && this.rightDiffIndex.get(R) == -1) {
            leftViewIndex.add(L * (-1));
            L++;
            rightViewIndex.add(R * (-1));
            R++;
         }
         /* ���� �гο��� ������ �����ϴ� ��� ������ �ش� �гο� �����ϰ� �ٸ� �гο��� ����(0)�� ���� */
         else {
            while (this.rightDiffIndex.get(R) == -1) {
               leftViewIndex.add(0);
               rightViewIndex.add(R++);
            }
            while (this.leftDiffIndex.get(L) == -1) {
               leftViewIndex.add(L++);
               rightViewIndex.add(0);
            }
         }
      }
      
      while(L< L_Max){
          leftViewIndex.add(L++);
          rightViewIndex.add(0);
      }
      
      while(R< R_Max){
          leftViewIndex.add(0);
          rightViewIndex.add(R++);
      }
      
      leftDiffIndex = leftViewIndex;
      rightDiffIndex = rightViewIndex;

   }

   
   /**
    * block�� start index�� end index�� �����ϴ� �޼ҵ�
    * blocks arraylist�� (start, end)�� ����
    */
   private void computeBlocks() {
      int start = 0;
      int end = 0;
      int i = 1;
      int size = leftDiffIndex.size();

      while (i < size) {
         /* ���� ������ ��� */
         while ((i < size && leftDiffIndex.get(i) > 0) && (rightDiffIndex.get(i) > 0)) {
            i++;
         }

         /* ������ ������ ��� */
         if (i < size && leftDiffIndex.get(i) == 0) {
            start = i++;
            while (i < size && leftDiffIndex.get(i) == 0) {
               i++;
            }
            end = i - 1;

            blocks.add(new int[] { start, end });
         }
         /* �������� ������ ��� */
         else if (i < size && rightDiffIndex.get(i) == 0) {
            start = i++;
            while (i < size && rightDiffIndex.get(i) == 0) {
               i++;
            }
            end = i - 1;

            blocks.add(new int[] { start, end });
         }
         /* �ٸ� string�� ���� line�� �ִ� ��� */
         else if (i < size && leftDiffIndex.get(i) < 0 && rightDiffIndex.get(i) < 0) {
            start = i++;
            while (i < size && leftDiffIndex.get(i) < 0 && rightDiffIndex.get(i) < 0) {
               i++;
            }
            end = i - 1;

            blocks.add(new int[] { start, end });
         }
      }
   }

   /**
    * leftDiffIndex�� element�� ��� 0 ������ ������ �ٲپ return ��
    * @return diffLeft
    */
   public ArrayList<Integer> getDiffLeft() {
      for (int i = 1; i < leftDiffIndex.size(); i++) {
         int num = leftDiffIndex.get(i);
         if (num > 0) {
            leftDiffIndex.set(i, num*(-1));
         }
      }      
      return this.leftDiffIndex;
   }

   /**
    * rightDiffIndex�� element�� ��� 0 �̻��� ������ �ٲپ return ��
    * @return diffRight
    */
   ArrayList<Integer> getDiffRight() {
      for (int i = 1; i < rightDiffIndex.size(); i++) {
         int num = rightDiffIndex.get(i);
         if (num < 0) {
            rightDiffIndex.set(i, num * (-1));
         }
      }
      
      return this.rightDiffIndex;
   }

   ArrayList<int[]> getBlocks() {
      return this.blocks;
   }

   public static void main(String[] args) {
      /*
       * This main is a test for this class. You can delete this if you don't need.
       */
      ArrayList<String> s2 = new ArrayList<String>();
      ArrayList<String> s1 = new ArrayList<String>();
      
      s1.add("Your job is to work on these two files:");
      s1.add(" - Board.JAVA");
      s1.add(" - Solver.JAVA");
      s1.add("");
      s1.add("Nothing to do in these three files");
      s1.add(" - MinPQ.JAVA");
      s1.add(" - Queue.JAVA");
      s1.add(" - Stack.JAVA");
      s1.add("");
      s1.add("Hello!");
      s1.add("Kim Soyeon test :)");
      s1.add("");
      s1.add("ischanged");
      s1.add("");
      s1.add("");
      s1.add("1");
      
      s2.add("Your job is to work on these two files:");
      s2.add(" - Board.JAVA");
      s2.add(" - Solver.JAVA");
      s2.add("");
      s2.add("Nothing to do in these three files");
      s2.add(" - MinPQ.JAVA");
      s2.add(" - Queue.JAVA");
      s2.add(" - Stack.JAVA");
      s2.add("");
      s2.add("");
      s2.add("Hello!");
      s2.add("isChanged");
      
      
      FileComparator fc = new FileComparator(s2, s1);

      System.out.println("Left Panel=========");
      for (int i = 1; i < fc.getDiffLeft().size(); i++) {
         System.out.println("[" + i + "](=" + fc.getDiffLeft().get(i) + ")\t");
      }

      System.out.println("\nRight Panel=========");
      for (int i = 1; i < fc.getDiffRight().size(); i++) {
         System.out.println("[" + i + "](=" + fc.getDiffRight().get(i) + ")\t");
      }

      System.out.println("\nDiff blocks=========");
      for (int i = 0; i < fc.blocks.size(); i++) {
         System.out.println("block[" + i + "]: (" + fc.blocks.get(i)[0] + ", " + fc.blocks.get(i)[1] + ")");
      }
      System.out.println("\nCTABLE=========");
      for (int i = 0; i < fc.C.length; i++) {
    	  for(int j =0; j < fc.C[0].length; j++)
    		  System.out.printf("%2d ", fc.C[i][j]);
    	  System.out.println();
       }
      System.out.println("\nCTABLE Trans=========");
      for (int i = 0; i < fc.C[0].length; i++) {
    	  for(int j =0; j < fc.C.length; j++)
    		  System.out.printf("%2d ", fc.C[j][i]);
    	  System.out.println();
       }
   }
}