package javafxapplication3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {
    
    public static ArrayList<Block> blocks;
    public static ArrayList<Path> paths;
    public static String directoryPath = "";
    File selectedFile = null;
    
    @Override
    public void start(Stage mainStage) {
        // Creating label for path
        Text path = new Text("Path");
        path.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        path.setFill(Color.WHITE);

        // Creating TextField for directory path
        TextField directoryTextField = new TextField();
        directoryTextField.setEditable(false);
        directoryTextField.setStyle("-fx-pref-width: 200px; -fx-background-color: #333333; -fx-text-fill: white;");

        // Creating Buttons
        Button browse = new Button("Browse");
        Button view = new Button("View");
        Button exit = new Button("Exit");

        // Setting styles for buttons
        browse.setStyle("-fx-pref-width: 100px; -fx-font-size: 16px; -fx-background-color: #FF6F00; -fx-text-fill: white; -fx-font-family: 'Arial'");
        view.setStyle("-fx-pref-width: 100px; -fx-font-size: 16px; -fx-background-color: #F44336; -fx-text-fill: white; -fx-font-family: 'Arial'");
        exit.setStyle("-fx-pref-width: 100px; -fx-font-size: 16px; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-family: 'Arial'");

        // Event handler for the browse button
        browse.setOnAction((ActionEvent event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select a File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("MDL Files", "*.mdl"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
            selectedFile = fileChooser.showOpenDialog(mainStage);
            
            if (selectedFile != null) {
                Main.directoryPath = selectedFile.getAbsolutePath();
                directoryTextField.setText(directoryPath);
            }
            selectedFile = new File(directoryPath);
        });

        // Event handler for the view button
        view.setOnAction((ActionEvent event) -> {
            // Check if The Extension is Right
            if (!(directoryPath.matches(".*.mdl"))) {
                System.out.println(directoryPath);
                Stage primaryStage = new Stage();
                Label messageLabel = new Label("Invalid File Type Selected");

                // Create a layout to hold the content
                VBox root = new VBox(messageLabel);
                root.setSpacing(10);
                root.setStyle("-fx-padding: 20px");

                // Create the pop-up window
                Stage popupStage = new Stage();
                popupStage.initModality(Modality.APPLICATION_MODAL); // Prevent interaction with other windows
                popupStage.initOwner(primaryStage);
                popupStage.setTitle("Error");
                popupStage.setResizable(false);
                popupStage.setScene(new Scene(root, 250, 100));

                // Show the pop-up window
                popupStage.show();
            }
            
            // Extracting Information From MDL File
            String[] x = null;
            try {
                x = extractFromFileEnd(selectedFile.getAbsolutePath(), "__MWOPC_PART_BEGIN__ /simulink/systems/system_root.xml", "__MWOPC_PART_BEGIN__ /simulink/windowsInfo.xml");
            } catch (Exception ex) {
                System.out.println("Invalid File Selected");
            }
            int count = 0;
            blocks = new ArrayList<>();
            ArrayList<ArrayList<Integer>> dist;
            dist = new ArrayList<>();
            paths = new ArrayList<>();
            String name = "";
            int sidI = 0;
            int positionLeft = 0;
            int positionTop = 0;
            int inputPort = 0;
            int outputPort = 0;
            int branchsCount = 0;
            ArrayList<Point2D> branchPoints = new ArrayList<>();//fake branch points
            ArrayList<Integer> realbranchPoints = new ArrayList<>();//real branching points becoz we were stupid and named the prev variable branch points but in reality branch points are the real points
            int[] srcPoints = null;
            String[] arr;
            boolean flag = false;
            boolean flagPort = false;
            boolean flagline = false;
            boolean isReverse = false;
            for (String x1 : x) {
                if (x1.matches(".*<Block.*")) {
                    flag = true;
                    name = getStringBetweenVarQuotes(x1, 3);
                    sidI = Integer.parseInt(getStringBetweenVarQuotes(x1, 5));
                } else if (x1.matches(".*</Block>")) {
                    count++;
                    flag = false;
                    if (flagPort == true) {
                        blocks.add(new Block(name, sidI, positionLeft, positionTop, inputPort, outputPort, isReverse));
                        flagPort = false;
                        isReverse = false;
                    } else {
                        blocks.add(new Block(name, sidI, positionLeft, positionTop, 1, 1, isReverse));
                        isReverse = false;
                    }
                } else if (x1.matches(".*<P Name=\"Position\">.*") && flag) {
                    arr = getIntBetweenSquareBrackets(x1);
                    positionLeft = Integer.parseInt(arr[0]);
                    positionTop = Integer.parseInt(arr[1]);
                    Integer.parseInt(arr[2]);
                    Integer.parseInt(arr[3]);
                } else if (x1.matches(".*<P Name=\"Ports\">.*") && flag) {
                    arr = getIntBetweenSquareBrackets(x1);
                    flagPort = true;
                    if (arr.length == 2) {
                        inputPort = Integer.parseInt(arr[0]);
                        outputPort = Integer.parseInt(arr[1]);
                    } else {
                        inputPort = Integer.parseInt(arr[0]);
                        outputPort = 0;
                    }
                } else if (x1.matches(".*<P Name=\"BlockMirror\".*") && flag) {
                    isReverse = true;
                } else if (x1.matches(".*<Line>")) {
                    flagline = true;
                } else if (x1.matches(".*</Line>")) {
                    paths.add(new Path((ArrayList<ArrayList<Integer>>) dist.clone(), srcPoints[0], branchsCount, (ArrayList<Point2D>) branchPoints.clone(), (ArrayList<Integer>) realbranchPoints.clone()));
                    branchPoints.clear();
                    dist.clear();
                    
                    flagline = false;
                    branchsCount = 0;
                } else if (flagline && x1.matches(".*<P Name=\"Src\">.*")) {
                    srcPoints = extractNumbers(x1);
                } else if (flagline && x1.matches(".*<P Name=\"Points\">.*")) {
                    for (int c = 0; c < extractPoints(x1).size(); c++) {
                        branchPoints.add(extractPoints(x1).get(c));
                    }
                } else if (flagline && x1.matches(".*<P Name=\"Dst\">.*")) {
                    dist.add(extractNumbersdist(x1));
                } else if (flagline && x1.matches(".*<Branch>")) {
                    branchsCount++;
                    if (branchsCount == 1) {
                        realbranchPoints.add(branchPoints.size() - 1);
                    }
                }
            }

            // Drawing Blocks and Lines 
            Stage stage1 = new Stage();
            Pane pane = new Pane();
            Rectangle[] rectangles = new Rectangle[Block.getNumberOfBlocks()];
            Text[] texts = new Text[Block.getNumberOfBlocks()];
            for (int i = 0; i < Block.getNumberOfBlocks(); i++) {
                rectangles[i] = new Rectangle(blocks.get(i).getPositionLeft(), blocks.get(i).getPositionTop(), 40, 40);
                pane.getChildren().add(rectangles[i]);
                texts[i] = new Text(blocks.get(i).getPositionLeft(), blocks.get(i).getPositionTop() + 50, blocks.get(i).getName());
                pane.getChildren().add(texts[i]);
            }
            int srcSid = 0;
            int dstSid = 0;
            boolean flagsrc = false;
            boolean flagdst = false;
            Point2D src = null;
            Point2D dst = null;
            ArrayList<ArrayList<Point2D>> inputsForEveryBlockInCaseOfBranching = new ArrayList<>();
            for (int i = 0; i < paths.size(); i++) {
                if (paths.get(i).getBranches() == 0) {
                    for (int j = 0; j < Block.getNumberOfBlocks(); j++) {
                        //This gets the sid of the block and match it with the Src sid of the path(line)
                        if (blocks.get(j).getSidI() == paths.get(i).getSrcSidI()) {
                            flagsrc = true;
                            
                            src = blocks.get(j).getOutBlock().get(0);
                            
                        }
                        //This gets the sid of the block and match it with the Dst sid of the path(line)
                        if (blocks.get(j).getSidI() == paths.get(i).getDstNumbers().get(0).get(0)) {
                            
                            flagdst = true;
                            if (blocks.get(j).getInputPort() == 1) {
                                dst = blocks.get(j).getInBlock().get(0);
                            } else {
                                flagdst = true;
                                dst = blocks.get(j).getInBlock().get(paths.get(i).getDstNumbers().get(0).get(0) - 1); //5azo2
                            }
                        }
                        
                        if (flagdst == true && flagsrc == true) {
                            if (paths.get(i).getPoint2Ds().isEmpty()) {//no curves in line or branches it means that it is a normal straight line
                                Line line = new Line(src.getX(), src.getY(), dst.getX(), dst.getY()); // 5aazo2
                                pane.getChildren().add(line);
                                flagdst = false;
                                flagsrc = false;
                            } else {
                                Point2D temp = null;
                                for (int q = 0; q < paths.get(i).getPoint2Ds().size() + 1; q++) {
                                    
                                    if (q == 0) {
                                        Line line = new Line(src.getX(), src.getY(), paths.get(i).getPoint2Ds().get(q).getX() + src.getX(), paths.get(i).getPoint2Ds().get(q).getY() + src.getY());
                                        pane.getChildren().add(line);
                                        temp = new Point2D(paths.get(i).getPoint2Ds().get(q).getX() + src.getX(), paths.get(i).getPoint2Ds().get(q).getY() + src.getY());
                                    } else if (q > 0 && q < paths.get(i).getPoint2Ds().size()) {
                                        Line line = new Line(temp.getX(), temp.getY(), temp.getX() + paths.get(i).getPoint2Ds().get(q).getX(), temp.getY() + paths.get(i).getPoint2Ds().get(q).getY());
                                        pane.getChildren().add(line);
                                        temp = new Point2D(temp.getX() + paths.get(i).getPoint2Ds().get(q).getX(), temp.getY() + paths.get(i).getPoint2Ds().get(q).getY());
                                    } else {
                                        Line line = new Line(temp.getX(), temp.getY(), dst.getX(), temp.getY());
                                        pane.getChildren().add(line);
                                    }
                                }
                                flagdst = false;
                                flagsrc = false;
                            }
                            
                        }
                    }
                } else {
                    // this part if getBranches>0
                    for (int z = 0; z < Block.getNumberOfBlocks(); z++) {
                        // compare each pathSidI with every block in the model
                        if (blocks.get(z).getSidI() == paths.get(i).getSrcSidI()) {
                            // flagsrc = true;
                            src = blocks.get(z).getOutBlock().get(0);
                            
                        }
                        for (int v = 0; v < paths.get(i).getDstNumbers().size(); v++) {
                            // store No. of points2D that represent the inputs for each block 
                            if (blocks.get(z).getSidI() == paths.get(i).getDstNumbers().get(v).get(0)) {
                                inputsForEveryBlockInCaseOfBranching.add(blocks.get(z).getInBlock());
                            }
                        }
                        
                    }
                    Point2D temp = null;
                    Point2D temp1 = null;
                    for (int b = 0; b < paths.get(i).getIndexOfBranchingPoints().get(0) + 1; b++) { // get(0) refer to that we only have 1 point before branching we need it dynamic 
                        temp = new Point2D(paths.get(i).getPoint2Ds().get(paths.get(i).getIndexOfBranchingPoints().get(0)).getX() + src.getX(), paths.get(i).getPoint2Ds().get(paths.get(i).getIndexOfBranchingPoints().get(0)).getY() + src.getY());
                        Line line = new Line(src.getX(), src.getY(), temp.getX(), temp.getY());
                        pane.getChildren().add(line);
                        temp1 = new Point2D(temp.getX(), temp.getY());
                        for (int a = 0; a < (paths.get(i).getPoint2Ds().size()) - (paths.get(i).getIndexOfBranchingPoints().get(0) + 1) + paths.get(i).getBranches(); a++) {
                            if (a == 2) {
                                line = new Line(temp1.getX(), temp1.getY(), inputsForEveryBlockInCaseOfBranching.get(0).get(0).getX(), temp1.getY());
                                pane.getChildren().add(line);
                            } else if (a == 0) {
                                
                                line = new Line(temp.getX(), temp.getY(), paths.get(i).getPoint2Ds().get(1).getX() + temp.getX(), paths.get(i).getPoint2Ds().get(1).getY() + temp.getY());
                                
                                pane.getChildren().add(line);
                                temp = new Point2D(paths.get(i).getPoint2Ds().get(1).getX() + temp.getX(), paths.get(i).getPoint2Ds().get(1).getY() + temp.getY());
                                
                            } else {
                                if (i == 1) {
                                    line = new Line(temp.getX(), temp.getY(), inputsForEveryBlockInCaseOfBranching.get(0).get(0).getX(), temp.getY());
                                    pane.getChildren().add(line);
                                } else {
                                    line = new Line(temp.getX(), temp.getY(), blocks.get(4).getPositionLeft() + 40, temp.getY());
                                    pane.getChildren().add(line);
                                    
                                }
                            }
                        }
                    }
                    
                    inputsForEveryBlockInCaseOfBranching.clear();
                }
            }
            
            Scene scene = new Scene(pane);
            
            stage1.setScene(scene);
            
            stage1.show();
            
            Block.setNumberOfBlocks(0);
        });
        
        // Event handler for the exit button
        exit.setOnAction((ActionEvent event) -> {
            System.exit(0);
        });

        // Creating a GridPane
        GridPane gridPane = new GridPane();

        // Setting size for the pane
        gridPane.setMinSize(600, 400);

        // Setting the padding
        gridPane.setPadding(new Insets(5));

        // Setting the vertical and horizontal gaps between the columns
        gridPane.setVgap(50);
        gridPane.setHgap(10);

        // Setting the Grid alignment
        gridPane.setAlignment(Pos.CENTER);
        GridPane.setHalignment(path, HPos.CENTER);
        GridPane.setValignment(path, VPos.CENTER);
        GridPane.setHalignment(exit, HPos.CENTER);
        GridPane.setValignment(exit, VPos.CENTER);
        GridPane.setHalignment(view, HPos.CENTER);
        GridPane.setValignment(view, VPos.CENTER);
        GridPane.setHalignment(browse, HPos.CENTER);
        GridPane.setValignment(browse, VPos.CENTER);

        // Adding column constraints
        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints();
        ColumnConstraints column3 = new ColumnConstraints();
        column1.setPrefWidth(150); // Set preferred width for column 1
        column2.setPrefWidth(200); // Set preferred width for column 2
        column3.setPrefWidth(150); // Set preferred width for column 3
        gridPane.getColumnConstraints().addAll(column1, column2, column3);

        // Arranging all the nodes in the grid
        gridPane.add(path, 0, 0);
        gridPane.add(directoryTextField, 1, 0, 2, 1);
        gridPane.add(browse, 0, 1);
        gridPane.add(view, 1, 1);
        gridPane.add(exit, 2, 1);

        // Set dark mode background color
        gridPane.setStyle("-fx-background-color: #1f1f1f;");

        // Creating a scene object
        Scene scene = new Scene(gridPane);

        // Setting title to the Stage
        mainStage.setTitle("SimuLink Viewer");

        // Adding scene to the stage
        mainStage.setScene(scene);

        // Displaying the contents of the stage
        mainStage.show();
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        
        launch(args);
        
    }
    
    public static String[] extractFromFileEnd(String fileName, String startingString, String endingString) throws IOException {
        File file = new File(fileName);
        StringBuilder contentBuilder;
        int startingLine;
        try ( BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean startExtracting = false;
            contentBuilder = new StringBuilder();
            startingLine = 0;
            // Get the line number where the startingString is located
            while ((line = br.readLine()) != null) {
                startingLine++;
                if (line.contains(startingString)) {
                    startExtracting = true;
                    contentBuilder.append(line).append("\n");
                    break;
                }
            }   // Extract the content from the starting line to the end of the file
            if (startExtracting) {
                while ((line = br.readLine()) != null) {
                    contentBuilder.append(line).append("\n");
                    if (line.contains(endingString)) {
                        break;
                    }
                }
            }
        }

        // Get the substring from the starting line to the end of the extracted content
        String content = contentBuilder.toString();
        return content.split("\n");
    }
    
    public static String getStringBetweenVarQuotes(String input, int posQuote) {
        int quoteCount = 0;
        int startIndex = -1;
        int endIndex = -1;

        // Iterate over each character in the input string
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '"') {
                quoteCount++;
                
                if (quoteCount == posQuote) {
                    startIndex = i + 1; // Start index of the substring

                } else if (quoteCount == posQuote + 1) {
                    endIndex = i; // End index of the substring
                    break;
                }
            }
        }

        // Return the substring between the third and fourth quotes
        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            return input.substring(startIndex, endIndex);
        } else {
            return "NOT FOUND"; // Return an empty string if the quotes are not found or in the wrong order
        }
    }
    
    public static String[] getIntBetweenSquareBrackets(String x) {
        String dummy;
        int begin = 0;
        int end = 0;
        String[] arr;
        
        for (int j = 0; j < x.length(); j++) {
            if (x.charAt(j) == '[') {
                
                begin = j;
            } else if (x.charAt(j) == ']') {
                end = j;
                break;
            }
        }
        dummy = x.substring(begin + 1, end);
        arr = dummy.split(", ");
        return arr;
    }
    
    public static int[] extractNumbers(String input) {
        String numberString = input.replaceAll("\\D+", "");
        int[] numbers = new int[2];
        for (int i = 0; i < numberString.length(); i++) {
            numbers[i] = Character.getNumericValue(numberString.charAt(i));
        }
        return numbers;
    }
    
    public static ArrayList<Integer> extractNumbersdist(String input) {
        String numberString = input.replaceAll("\\D+", "");
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < numberString.length(); i++) {
            numbers.add(Character.getNumericValue(numberString.charAt(i)));
        }
        return numbers;
    }
    
    public static ArrayList<Point2D> extractPoints(String input) {
        String pointsString = input.substring(input.indexOf("[") + 1, input.indexOf("]"));
        String[] pointsArray = pointsString.split(";");
        
        ArrayList<Point2D> points = new ArrayList<>();
        for (String pointsArray1 : pointsArray) {
            String[] pointCoordinates = pointsArray1.trim().split(",");
            double x = Double.parseDouble(pointCoordinates[0].trim());
            double y = Double.parseDouble(pointCoordinates[1].trim());
            points.add(new Point2D(x, y));
        }
        
        return points;
    }
    
}
