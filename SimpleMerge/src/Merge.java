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
   private int[] traverseCursor;
   /*
    * flagPrevious ��  ���̻� ������ �̵��� ���̾��ٸ� ǥ���ϴ� flag. true�̸� ���� �ִ�. false�� ���̻����.
    * flagNext ��������.
    * MainView �� �� Flag ���� �̿��� traverse ��ư�� ��Ȱ��ȭ/Ȱ��ȭ ��Ų��.
    */
   private boolean flagPrevious;
   private boolean flagNext;
   /*
    * diffFirstIndex��  ���� ó�� block(SRS glossory)�� begin ����.
    * diffLastIndex�� ���� ������ block(SRS glossory)�� end ����.
    */
   private int diffFirstIndex;
   private int diffLastIndex;
   
   Merge() {
      System.out.println("No input panels");
   }

   Merge(TextEditorModel leftPanel, TextEditorModel rightPanel) {
      this.leftPanel = leftPanel;
      this.rightPanel = rightPanel;
      this.traverseCursor = new int[] {0, 0};

      /* panel contents �޾ƿͼ� parsing �� arraylist�� ���� */
      this.leftFileContents = new ArrayList<String>(leftPanel.getFileContentList());
      this.leftFileContents.add(0, "");

      this.rightFileContents = new ArrayList<String>(rightPanel.getFileContentList());
      this.rightFileContents.add(0, "");
      
      /* FileComparator�� �̿��Ͽ� compare �� difference�� ������ index �����ޱ� */
      FileComparator fc = new FileComparator(leftFileContents, rightFileContents);
      this.leftDiffIndex = fc.getDiffLeft();
      this.rightDiffIndex = fc.getDiffRight();
      this.blocks = fc.getBlocks();

      setDiffFirstIndex();
      setDiffLastIndex();
      flagPrevious = setFlagPrevious();
      flagNext = setFlagNext();
      
   }
/*
 * diffFirstIndex�� ������ش�.
 */
   private void setDiffFirstIndex(){
      for(int i = 1; i < leftDiffIndex.size(); i++){
         if(leftDiffIndex.get(i) <= 0 || rightDiffIndex.get(i) <= 0){
            diffFirstIndex = i;
            break;
         }
      }
   }
   
   /*
    * diffLastIndex�� ������ش�.
    */
   private void setDiffLastIndex(){
      for(int i = leftDiffIndex.size()-1; i > 0; i--){
         if(leftDiffIndex.get(i) <= 0 || rightDiffIndex.get(i) <= 0){
            diffLastIndex = i;
            break;
         }
      }
   }
   
   /*
    * flagPrevious ����. 
    */
   private boolean setFlagPrevious(){
      if(traverseCursor[0] != diffFirstIndex)
         return true;
      
      else
         return false;
   }
   
   /*
    * flagNext ����
    */
   private boolean setFlagNext(){
      if(traverseCursor[1] != diffLastIndex)
         return true;
      
      else
         return false;
   }
   
   /*
    * �� �޼ҵ带 �����ϸ� previous block �� ����Ų��.
    * MainView�� getTraverseCursor�� ���� ���� ����Ű�� [begin index, end index] �� ��´�.
    * �˰����� �ٷ� �Ʒ��� �޼ҵ带 ���� �ٶ�.
    */
   void traversePrevious() {
     int i = traverseCursor[0] - 1;
     
     while(leftDiffIndex.get(i) > 0 && rightDiffIndex.get(i) > 0)
        i--;
     
      traverseCursor[1] = i;

      if(leftDiffIndex.get(i) == 0 || rightDiffIndex.get(i) == 0){
         while(i >= diffFirstIndex && (leftDiffIndex.get(i) == 0 || rightDiffIndex.get(i) == 0))
            i--;
      }
      
      else{
         while(i >= diffFirstIndex && (leftDiffIndex.get(i) < 0 && rightDiffIndex.get(i) < 0))
            i--;
      }
      
      traverseCursor[0] = ++i;
   }   

   /*
    * �˰��� : 
    * [0,0]�� �� � traverse ��ư�� ������ ���� �ʱ� �����̴�.
    * �� ���¿��� next ��ư�� �����ٸ�
    * 1) leftDiffIndex�� 1�� index���� block�� ã������Ѵ�.
    * 2) block�� ã�Ҵٸ� �� block�� begin index�� ���� Ŀ���� begin index�� ����.
    * 3) block�� ���� ã�� ���� ���並 �Ѵ�. <�� ��, ������vs�ٸ������� block case�� �ٸ�����vs�ٸ������� block case�� ������.>
    * 4) block�� ���� ã�Ҵٸ� Ŀ���� end index�� ����.
    * 5) MainView�� getTraverseCursor�� ���� ���� ����Ű�� [begin index, end index] �� ��´�.
    * # ���� previous�� �̿� �ݴ�� �̷�����ִ�.
    */
   void traverseNext() {
     int i = traverseCursor[1] + 1;
     
     while(leftDiffIndex.get(i) > 0 && rightDiffIndex.get(i) > 0)
        i++;
     
     traverseCursor[0] = i;
   
     if(leftDiffIndex.get(i) == 0 || rightDiffIndex.get(i) == 0){
        while(i <= diffLastIndex && (leftDiffIndex.get(i) == 0 || rightDiffIndex.get(i) == 0))
           i++;
     }
     
     else{
        while(i <= diffLastIndex && (leftDiffIndex.get(i) < 0 && rightDiffIndex.get(i) < 0))
           i++;
     }
     
     traverseCursor[1] = --i;
   }

   ////////////////////////merge�� �Լ� CTL, CTR �̿ϼ� �ФФФФФФФФФФФФФФФФФФФФФФФФФФФФФФ�
   /*
    * 
    * 
    *  
    */
   void copyToLeft() {
      for(int i = traverseCursor[0]; i< traverseCursor[1]; i++){
         
      }
   }

   /**
    * clear all the string and copy from left to right
    */
   void copyToRight() {
      int i = traverseCursor[0];
        if(leftDiffIndex.get(i) == 0 || rightDiffIndex.get(i) == 0){
           while(i <=traverseCursor[1] && (leftDiffIndex.get(i) == 0 || rightDiffIndex.get(i) == 0))
              i++;
        }
        
        else{
           while(i <= traverseCursor[1] && (leftDiffIndex.get(i) < 0 && rightDiffIndex.get(i) < 0))
              i++;
        }
   }
   /////////////////////////////////////

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
   
   public int[] getTraverseCursor(){
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
            index = mc.getTraverseCursor();
            if (index[0] != -1 && index[1] != -1) {
               System.out.println("Traverse line " + index[0] + " to " + index[1]);
               for (int i = index[0]; i <= index[1]; i++)
                  System.out.println(mc.leftFileContents.get(i));
            }
            break;
         case 4:
            mc.traverseNext();
            index = mc.getTraverseCursor();
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