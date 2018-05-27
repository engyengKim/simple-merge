import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/*�гΰ��� �޼ҵ带 �����ϴ� ��Ʈ�ѷ�(interpanel controller),
 * compare, traverse, merge �޼ҵ� ���� */
public class MergeControllerSP {
	
	private String leftPanelContent;
	private String rightPanelContent;
	private FileComparatorSP fc;
	private ArrayList<Integer> diffLeft;
	private ArrayList<Integer> diffRight;
	private ArrayList<int[]> leftTraversePoint;
	private ArrayList<int[]> rightTraversePoint;
	
	MergeControllerSP(){
		System.out.println("No input panels");
	}
	
	MergeControllerSP(PanelController leftPanel, PanelController rightPanel) {
		
		leftPanelContent = leftPanel.getFileContent();
		rightPanelContent = rightPanel.getFileContent();
		
		fc = new FileComparatorSP(leftPanelContent, rightPanelContent);
		
		diffLeft = fc.getDiffLeft();
		diffRight = fc.getDiffRight();
		
		leftTraversePoint = new ArrayList<int[]>();
		rightTraversePoint = new ArrayList<int[]>();
		leftTraversePoint = arrangeTraverseSamePoint(leftTraversePoint, diffLeft);
		rightTraversePoint = arrangeTraverseSamePoint(rightTraversePoint, diffRight);
		arrangeTraverseAdditionalPoint();
	}
	
	/*
	  �� ���� �ؽ�Ʈ ���� ����� �κ��� ���ӵǾ��ִ� index���� ArrayList���� �����Ѵ�. 
	  
	  example:
	  
	  "AGGTAB" "GXXTAYYB" -> {1,3,4,5}, {0,3,4,7}
	  ��� ���Ҵ� char index.
	  
	  3�� 4 �κ��� ���ӵ� �κ��̴�. 
	  �� �ؽ�Ʈ�� Ʋ���κ��� begin index�� end index �̿��� �迭ȭ ��Ŵ. {begin, end}
	  ���� AL�� { {1,3} , {4,5} }, { {0,3}, {4,7} }
	 */
	ArrayList<int[]> arrangeTraverseSamePoint(ArrayList<int[]> traversePoints, ArrayList<Integer> diffAL){			
		
		for(int i = 1; i<diffAL.size(); i++){
			
			int begin = diffAL.get(i-1);
			int end = diffAL.get(i);
			
			if(end - begin != 1)
				traversePoints.add(new int[] {begin, end});
		}
		
		return traversePoints;
	}
	
	/*
	 �� ���� �ؽ�Ʈ���� �� �ʿ��� ���� �� �Ǵ� �ڿ� ������ �߰��Ǿ� �ִ°�츦 �����Ѵ�.  
	 
	 
	 */
	void arrangeTraverseAdditionalPoint(){
		
		if(diffLeft.get(0) != 0 || diffRight.get(0) != 0){
			
			leftTraversePoint.add(0, new int[]{0, diffLeft.get(0)});
			rightTraversePoint.add(0, new int[]{0, diffRight.get(0)});
		}
		
		int leftLastIndex = diffLeft.size()-1;
		int rightLastIndex = diffRight.size()-1;
		
		if(diffLeft.get(leftLastIndex) != leftPanelContent.length()-1 || diffRight.get(rightLastIndex) != rightPanelContent.length()-1 ){
			
			leftTraversePoint.add(new int[]{ diffLeft.get(leftLastIndex), leftPanelContent.length()-1});
			rightTraversePoint.add(new int[]{ diffRight.get(rightLastIndex) , rightPanelContent.length()-1 });
		}

	}
	
	
	ArrayList<int[]> getLeftTraversePoint() {
		
		return this.leftTraversePoint;
	}
	
	ArrayList<int[]> getRightTraversePoint() {
		
		return this.rightTraversePoint;
	}
	
	public void copyToLeft() {
		// TODO
	}
	
	public void copyToRight() {
		// TODO
	}	
	
	public static void main(String[] args) {
		/* This main is a test for this class.
		 * You can delete this if you don't need.
		 * */


	}
}
