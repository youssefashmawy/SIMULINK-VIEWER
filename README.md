# SIMULINK VIEWER

The project aims to implement a graphical user interface (GUI) application using JavaFX. The application involves displaying blocks based on the Provided MDL file.

## Features

- Displaying blocks with the given properties such as name, position, input/output ports, and reverse mode which are provided in the MDL file.
- Automatic generation of connection points within each block based on the number of input/output ports.
- Ability to connect blocks together based on the provided MDL file.
- User-friendly interface for visualizing the blocks.

## Getting Started

To run the Simulink viewer, make sure you have the following requirements installed on your system:
- An IDE of your choice
- Java Development Kit (JDK) 8 or above
- JavaFX framework
- Run the `Main` class as the main entry point of the application.

## Usage

Upon running the application, you will be presented with a GUI window. Here, you can perform the following actions:

- Browse: Which makes you select the MDL file you want to view 
- View: After selecting the file you press view to show the MDL file
- Exit: After viewing your required MDL files press exit to terminate the program

## Classes

The BlockGUI Application consists of the following Java classes:

1. `Block`: Represents a block in the GUI. It contains properties such as name, position, input/output ports, and reverse mode. It also provides methods for managing connections and generating connection points within the block.

2. `Main`: The main class of the application. It initializes the GUI window and handles user interactions.

3. `Path`: Is a Java class that represents a path in between the blocks. It is designed to store information about the source, branches, destination numbers, and coordinate points of a path.

## UML diagrams
- [Main](https://github.com/youssefashmawy/SIMULINK-VIEWER/blob/main/Main.png)
- [Block](https://github.com/youssefashmawy/SIMULINK-VIEWER/blob/main/Block.png)
- [Path](https://github.com/youssefashmawy/SIMULINK-VIEWER/blob/main/Path.png)

## Authors 
- Leader: [Youssef Wael](https://github.com/youssefashmawy) 2001430
- [Yousef Shawky](https://github.com/thedarkevil987)  2001500
- [Omar Saleh](https://github.com/MrMariodude)  2001993
- [Fathy Abdlhady](https://github.com/fathy-abdlhady-f)  2001152
