import java.awt.*;
import java.util.Scanner;

/**
 * This class is responsible for controlling the Treasure Hunter game.<p>
 * It handles the display of the menu and the processing of the player's choices.<p>
 * It handles all the display based on the messages it receives from the Town object. <p>
 *
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class TreasureHunter {
    // static variables
    private static final Scanner SCANNER = new Scanner(System.in);

    // instance variables
    private Town currentTown;
    private Hunter hunter;
    private boolean hardMode;
    private String mode;
    private String[] treasures = {"Crown","Trophy","Gem","Dust"};
    private OutputWindow display;

    /**
     * Constructs the Treasure Hunter game.
     */
    public TreasureHunter() {
        // these will be initialized in the play method
        currentTown = null;
        hunter = null;
    }

    /**
     * Starts the game; this is the only public method
     */
    public void play(OutputWindow display) {
        this.display = display;
        welcomePlayer();
        enterTown();
        showMenu();

    }

    /**
     * Creates a hunter object at the beginning of the game and populates the class member variable with it.
     */
    private void welcomePlayer() {
        display.addTextToWindow("Welcome to TREASURE HUNTER!", Color.yellow);
        display.addTextToWindow("\ngoing hunting for the big treasure, eh?");
        display.addTextToWindow("\nWhat's ye name, Hunter? ");
        String name = SCANNER.nextLine().toLowerCase();

        // set hunter instance variable
        hunter = new Hunter(name, 10);

        display.addTextToWindow("\n\n\nHard/Normal/Easy Mode? (h/n/e): ",Color.red);
        String difficulty = SCANNER.nextLine().toLowerCase();
        if (difficulty.equals("h")) {
            mode = "h";
        }
        if (difficulty.equals("e")) {
            hunter = new Hunter(name, 20);
            mode = "e";
        }
        if (difficulty.equals("s")){
            hunter = new Hunter("Samurai " + name, 20);
            mode = "s";
        }
        if (difficulty.equals("test")) {
            hunter = new Hunter(name, 100);
            hunter.addItem("water");
            hunter.addItem("rope");
            hunter.addItem("machete");
            hunter.addItem("horse");
            hunter.addItem("boat");
            hunter.addItem("boots");
            hunter.addItem("shovel");
        }
    }

    /**
     * Creates a new town and adds the Hunter to it.
     */
    private void enterTown() {
        double markdown = 0.5;
        double toughness = 0.4;
        if (mode.equals("h")) {
            // in hard mode, you get less money back when you sell items
            markdown = 0.25;

            // and the town is "tougher"
            toughness = 0.75;
        }
        if (mode.equals("e")) {
            // in hard mode, you get less money back when you sell items
            markdown = 1;

            // and the town is "tougher"
            toughness = 0.25;
        }
        if (mode.equals("s")) {
            // in hard mode, you get less money back when you sell items
            markdown = 1;

            // and the town is "tougher"
            toughness = 0;
        }
        // note that we don't need to access the Shop object
        // outside of this method, so it isn't necessary to store it as an instance
        // variable; we can leave it as a local variable
        Shop shop = new Shop(markdown,mode);

        // creating the new Town -- which we need to store as an instance
        // variable in this class, since we need to access the Town
        // object in other methods of this class
        currentTown = new Town(shop, toughness, treasures, mode);

        // calling the hunterArrives method, which takes the Hunter
        // as a parameter; note this also could have been done in the
        // constructor for Town, but this illustrates another way to associate
        // an object with an object of a different class
        currentTown.hunterArrives(hunter);
    }

    /**
     * Displays the menu and receives the choice from the user.<p>
     * The choice is sent to the processChoice() method for parsing.<p>
     * This method will loop until the user chooses to exit.
     */
    private void showMenu() {
        String choice = "";

        while (!choice.equals("x") && hunter.getGold() >= 0 && !hunter.hasAllTreasure()) {
            display.clear();
            display.addTextToWindow(currentTown.getLatestNews());
            display.addTextToWindow("***");
            display.addTextToWindow("\n" + hunter);
            display.addTextToWindow("\n" + currentTown);
            display.addTextToWindow("\n(B)uy something at the shop.");
            display.addTextToWindow("\n(S)ell something at the shop.");
            display.addTextToWindow("\n(M)ove on to a different town.");
            display.addTextToWindow("\n(H)unt for treasure!");
            display.addTextToWindow("\n(D)ig for gold!");
            display.addTextToWindow("\n(L)ook for trouble!");
            display.addTextToWindow("\nGive up the hunt and e(X)it.");
            System.out.print("\n\nWhat's your next move? ");
            choice = SCANNER.nextLine().toLowerCase();
            processChoice(choice);
        }
        if (hunter.hasAllTreasure()){
            System.out.println(textColor.YELLOW_BOLD_BRIGHT + "Congrats you collected all the treasures of the land" + textColor.RESET);
        } else if (hunter.getGold() < 0){
            System.out.println(textColor.RED_BACKGROUND_BRIGHT + "You ran out of money and is forced to stop your hunt and dreams." + textColor.RESET);
        }
        System.out.println("\nUntil next time... bye bye");
    }

    /**
     * Takes the choice received from the menu and calls the appropriate method to carry out the instructions.
     * @param choice The action to process.
     */
    private void processChoice(String choice) {
        if (choice.equals("b") || choice.equals("s")) {
            currentTown.enterShop(choice);
        } else if (choice.equals("m")) {
            if (currentTown.leaveTown()) {
                // This town is going away so print its news ahead of time.
                System.out.println(currentTown.getLatestNews());
                enterTown();
            }
        } else if (choice.equals("l")) {
            currentTown.lookForTrouble();
        } else if (choice.equals("h")) {
            currentTown.huntForTreasure();
        } else if (choice.equals("d")) {
            currentTown.digForGold();
        } else if (choice.equals("x")) {
            System.out.println("Fare thee well, " + hunter.getHunterName() + "!");
        } else {
            System.out.println(textColor.RED_BOLD_BRIGHT + "Yikes! That's an invalid option! Try again." + textColor.RESET);
        }
    }
}