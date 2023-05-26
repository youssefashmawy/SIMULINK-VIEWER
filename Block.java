
package javafxapplication3;

import java.util.ArrayList;
import javafx.geometry.Point2D;
class Block {

    private String name;
    private String sidS;
    private int sidI;
    private int positionLeft;
    private int positionTop;
    private static int numberOfBlocks = 0;
    private int inputPort;
    private int outputPort;
    private ArrayList<Point2D> inBlock = new ArrayList<>();
    private ArrayList<Point2D> outBlock = new ArrayList<>();
    private boolean isReverse;

    public Block(String name, int sidI, int positionLeft, int positionTop, int inputPort, int outputPort, boolean isReverse) {
        this.name = name;
        this.sidI = sidI;
        this.positionLeft = positionLeft;
        this.positionTop = positionTop;
        this.inputPort = inputPort;
        this.outputPort = outputPort;
        this.inBlock = new ArrayList<>();
        this.outBlock = new ArrayList<>();
        this.isReverse = isReverse;
        pointsOfBlocks(outBlock, inBlock);

        if (isReverse) {
            ArrayList<Point2D> temp = inBlock;
            inBlock = outBlock;
            outBlock = temp;

        }
        numberOfBlocks++;
    }

    public static void setNumberOfBlocks(int numberOfBlocks) {
        Block.numberOfBlocks = numberOfBlocks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSidS() {
        return sidS;
    }

    public void setSidS(String sidS) {
        this.sidS = sidS;
    }

    public int getSidI() {
        return sidI;
    }

    public void setSidI(int sidI) {
        this.sidI = sidI;
    }

    public int getPositionLeft() {
        return positionLeft;
    }

    public void setPositionLeft(int positionLeft) {
        this.positionLeft = positionLeft;
    }

    public int getPositionTop() {
        return positionTop;
    }

    public void setPositionTop(int positionTop) {
        this.positionTop = positionTop;
    }

    public static int getNumberOfBlocks() {
        return numberOfBlocks;
    }

    public int getInputPort() {
        return inputPort;
    }

    public boolean isIsReverse() {
        return isReverse;
    }

    public int getOutputPort() {
        return outputPort;
    }

    public ArrayList<Point2D> getInBlock() {
        return inBlock;
    }

    public ArrayList<Point2D> getOutBlock() {
        return outBlock;
    }

    @Override
    public String toString() {
        return "Block{"
                + "name='" + name + '\''
                + ", sidI=" + sidI
                + ", positionLeft=" + positionLeft
                + ", positionTop=" + positionTop
                + ", inputPort=" + inputPort
                + ", outputPort=" + outputPort
                + '}';
    }

    void print() {
        System.out.println(this.toString());
    }

    public void pointsOfBlocks(ArrayList<Point2D> outBlock, ArrayList<Point2D> inBlock) {
        double yOffset = 40 / (outputPort + 1);
        int xOffset = 40;
        for (int i = 0; i < outputPort; i++) {

            outBlock.add(new Point2D(this.positionLeft + xOffset, this.positionTop + yOffset));
            yOffset += 40 / (outputPort + 1);
        }
        yOffset = 40 / (inputPort + 1);
        for (int i = 0; i < inputPort; i++) {

            inBlock.add(new Point2D(this.positionLeft, this.positionTop + yOffset));
            yOffset += 40 / (inputPort + 1);
        }
    }
}



