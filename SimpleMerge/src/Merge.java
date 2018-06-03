import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/*�гΰ��� �޼ҵ带 �����ϴ� ��Ʈ�ѷ�(interpanel controller),
 * compare, traverse, merge �޼ҵ� ���� */
public class Merge {

   private TextEditorModel leftPanel;
   private TextEditorModel rightPanel;
   private ArrayList<String> leftFileContents;
   private ArrayList<String> rightFileContents;
   /**
    * �� Panel�� difference index�� �����ϴ� �迭
    * index�� 0�� �ƴ� 1���� ����!
    * ���� ������ ���� �ٿ��� ������ index, �ٸ� ������ ���� �ٿ��� (-1)*index,
    * �������� 0�� ����
    */
   private ArrayList<Integer> leftDiffIndex;
   private ArrayList<Integer> rightDiffIndex;
   private ArrayList<int[]> blocks;
   
   /*
    * ���� Ŀ���� ��ġ int[2] => [begin index, end index]
    */
   private int traverseCursor;
   /*
    * flagPrevious ��  ���̻� ������ �̵��� ���̾��ٸ� ǥ���ϴ� flag. true�̸� ���� �ִ�. false�� ���̻����.
    * flagNext ��������.
    * MainView �� �� Flag ���� �̿��� traverse ��ư�� ��Ȱ��ȭ/Ȱ��ȭ ��Ų��.
    */
   private boolean flagPrevious;
   private boolean flagNext;
 
   Merge() {
      System.out.println("No input panels");
   }

   Merge(TextEditorModel leftPanel, TextEditorModel rightPanel) {
      this.leftPanel = leftPanel;
      this.rightPanel = rightPanel;
      this.traverseCursor = -1;

      /* panel contents �޾ƿͼ� parsing �� arraylist�� ���� */
      this.leftFileContents = new ArrayList<String>(leftPanel.getFileContentBufferList());
      this.leftFileContents.add(0, "");

      this.rightFileContents = new ArrayList<String>(rightPanel.getFileContentBufferList());
      this.rightFileContents.add(0, "");
      
      /* FileComparator�� �̿��Ͽ� compare �� difference�� ������ index �����ޱ� */
      FileComparator fc = new FileComparator(leftFileContents, rightFileContents);
      this.leftDiffIndex = fc.getDiffLeft();
      this.rightDiffIndex = fc.getDiffRight();
      this.blocks = fc.getBlocks();

      setFlag();
   }
   
   /*
    * �̰� �� �� �� if���� flagPrevious Ȥ�� flagNext �̿��ؼ� ���� �ٶ�.
    */
   private void setFlag(){
      if(blocks.size() == 0)
         flagPrevious = false;
      
      else if(traverseCursor != 1)
    	  flagPrevious = true;
      
      else
    	  flagPrevious = false;
      
	  int size = blocks.size() ;
      if(size == 0)
         flagNext = false;
      
      else if(traverseCursor != size-1)
    	  flagNext = true;
      
      else
    	  flagNext = false;
   }

   void traversePrevious() {
      	--traverseCursor;
   }   

   void traverseNext() {
       	++traverseCursor;
   }

   void copyToLeft() {
	  for(int i = blocks.get(traverseCursor)[0]; i <= blocks.get(traverseCursor)[1]; i++){
		  leftDiffIndex.set(i, rightDiffIndex.get(i));
	  }
	  blocks.remove(traverseCursor);
	  
	  traverseCursor--;
	  setFlag();
   }

   void copyToRight() {
	  for(int i = blocks.get(traverseCursor)[0]; i <= blocks.get(traverseCursor)[1]; i++){
		  rightDiffIndex.set(i, leftDiffIndex.get(i));
	  }
	  blocks.remove(traverseCursor);
	  
	  traverseCursor--;
	  setFlag();
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
   
   public boolean getFlagPrevious(){
      return this.flagPrevious;
   }
   
   public boolean getFlagNext(){
      return this.flagNext;
   }
   
   public int getTraverseCursor(){
      return this.traverseCursor;
   }
   
   /**
    * Used for the test of main class of this class.
    * You can delete this method.
    * It prints the index which includes blanks.
    */
   void printArranged() {
      System.out.println("Left Panel=========");
      for (int i = 1; i < this.leftDiffIndex.size(); i++)
         System.out.println("["+i+"] "+this.leftDiffIndex.get(i));

      System.out.println("\nRight Panel=========");
      for (int i = 1; i < this.rightDiffIndex.size(); i++)
         System.out.println("["+i+"] "+this.rightDiffIndex.get(i));
   }
   //
   /**
    * Used for the test of main class of this class.
    * You can delete this method.
    * It prints the index which does not include blanks.
    */
   void printAll() {
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
      TextEditorModel left = new TextEditorModel();
      TextEditorModel right = new TextEditorModel();

      left.setFileContentBuffer("same part1\r\n" + 
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

      right.setFileContentBuffer("same part1\r\n" + 
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

      Merge mc = new Merge(left, right);

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
            mc.traversePrevious();
            if (index[0] != -1 && index[1] != -1) {
               System.out.println("Traverse line " + index[0] + " to " + index[1]);
               for (int i = index[0]; i <= index[1]; i++)
                  System.out.println(mc.leftFileContents.get(i));
            }
            break;
         case 4:
            mc.traverseNext();
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