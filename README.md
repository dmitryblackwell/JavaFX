# JavaFX 
![alt text](https://raw.githubusercontent.com/dmitryblackwell/JavaFX/master/screenshots/rocket.png) Simple examples of JavaFx possibilities

## Calculator
Simple calculator, just to make awesome gui.
#### Project Tree:
1. Main
   - void start(Stage primaryStage);
   - void add(String s);
   - String getResult();
   - static void main(String[] args);
2. Material
   - public static final String BUTTON;
   - public static final String CTRL;
   - public static final String ZERRO;
#### GamePlay: 
![alt text](https://raw.githubusercontent.com/dmitryblackwell/JavaFX/master/screenshots/calculator.gif)

## 2048

Popular game remake on Java.
#### Game Tree (public only):

1. Bg - enum with color for text and background.
   - ![#FFFFFF](https://placehold.it/15/FFFFFF/000000?text=+) SLIGHTLY
   - ![#FF7E04](https://placehold.it/15/FF7E04/000000?text=+) STANDART
   - ![#CC1500](https://placehold.it/15/CC1500/000000?text=+) RED_FULL
   - ![#C704FF](https://placehold.it/15/C704FF/000000?text=+) VIOLET_LIGHT
   - ![#5C76CC](https://placehold.it/15/5C76CC/000000?text=+) BLUE_LIGHT
2. GUI - main class, that draw all javafx object.
   - static void main(String[] args);
   - void start(Stage primaryStage);
   - void setUpdateOfMap();
   - void setButtonStyle(int i,int j, int FontSize, Bg color);
3. MapImpl - implementation of interface below. 
4. Map - interface, to interact with map.
   - int FIELD_SIZE = 4;
   - String SAVES_FILE = "saves.txt";
   - int getCellValue(int x, int y);
   - int getScore(); 
   - void moveLeft();
   - void moveRight();
   - void moveUp();
   - void moveDown();
   - void moveRandom();
   - void saveGame(String bestScore);
   - String loadGame();
   
#### GamePlay
![alt text](https://raw.githubusercontent.com/dmitryblackwell/JavaFX/master/screenshots/2048.gif)