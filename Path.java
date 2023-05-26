
package javafxapplication3;

import java.util.ArrayList;
import javafx.geometry.Point2D;

class Path {

    private int srcSidI;
    private int branches;
    private ArrayList<ArrayList<Integer>> dstNumbers;
    private ArrayList<Point2D> Point2Ds;
    private ArrayList<Integer> indexOfBranchingPoints;

    public Path(ArrayList<ArrayList<Integer>> dstNumber, int srcSidI, int branches, ArrayList<Point2D> Point2Ds, ArrayList<Integer> indexOfBranchingPoints) {
        this.dstNumbers = dstNumber;
        this.srcSidI = srcSidI;
        this.branches = branches;
        this.Point2Ds = Point2Ds;
        this.indexOfBranchingPoints = indexOfBranchingPoints;
    }

    public ArrayList<Point2D> getPoint2Ds() {
        return Point2Ds;
    }

    public int getSrcSidI() {
        return srcSidI;
    }

    public void setSrcSidI(int srcSidI) {
        this.srcSidI = srcSidI;
    }

    public int getBranches() {
        return branches;
    }

    public void setBranches(int branches) {
        this.branches = branches;
    }

    public ArrayList<ArrayList<Integer>> getDstNumbers() {
        return dstNumbers;
    }

    public void setDstNumbers(ArrayList<ArrayList<Integer>> dstNumbers) {
        this.dstNumbers = dstNumbers;
    }

    public ArrayList<Integer> getIndexOfBranchingPoints() {
        return indexOfBranchingPoints;
    }

    public void printsidI() {
        for (int i = 0; i < dstNumbers.size(); i++) {
            System.out.print(dstNumbers.get(i));
        }
        System.out.println("");
        System.out.print("source" + srcSidI + " ");
        System.out.print("branch" + branches);

    }

}
