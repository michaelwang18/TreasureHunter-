/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all the things a Hunter can do in town.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class Town {
    // instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private boolean toughTown;

    private boolean searched;
    private String treasure;

    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     *
     * @param shop The town's shoppe.
     * @param toughness The surrounding terrain.
     */
    public Town(Shop shop, double toughness, String[] treasures) {
        this.shop = shop;
        this.terrain = getNewTerrain();
        boolean searched = false;
        treasure = treasures[(int) (Math.random() * treasures.length)];

        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;

        printMessage = "";

        // higher toughness = more likely to be a tough town
        toughTown = (Math.random() < toughness);
    }

    public String getLatestNews() {
        return printMessage;
    }

    /**
     * Assigns an object to the Hunter in town.
     *
     * @param hunter The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter) {
        this.hunter = hunter;
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";

        if (toughTown) {
            printMessage += "\nIt's pretty rough around here, so watch yourself.";
        } else {
            printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
        }
    }

    /**
     * Handles the action of the Hunter leaving the town.
     *
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown() {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown) {
            String item = terrain.getNeededItem();
            printMessage = "You used your " + Color.PURPLE_BOLD_BRIGHT + item + Color.RESET + " to cross the " +  Color.CYAN_BRIGHT + terrain.getTerrainName() + Color.RESET + ".";
            if (checkItemBreak()) {
                hunter.removeItemFromKit(item);
                printMessage += "\nUnfortunately, your " + Color.PURPLE_BOLD_BRIGHT + item + Color.RESET + " broke.";
            }

            return true;
        }

        printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + Color.PURPLE_BOLD_BRIGHT + terrain.getNeededItem() + Color.RESET + ".";
        return false;
    }

    /**
     * Handles calling the enter method on shop whenever the user wants to access the shop.
     *
     * @param choice If the user wants to buy or sell items at the shop.
     */
    public void enterShop(String choice) {
        shop.enter(hunter, choice);
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */

    public void huntForTreasure() {
        if (!searched) {
            System.out.println( Color.WHITE_BOLD_BRIGHT + "You found a... \n"  + Color.GREEN_BOLD_BRIGHT + treasure + Color.WHITE_BOLD_BRIGHT + "!" + Color.RESET);
            searched = true;
            hunter.addTreasure(treasure);
        } else {
            System.out.println(Color.WHITE_BOLD_BRIGHT + "You have already searched this town for treasure!" + Color.RESET);
        }
    }


    public void lookForTrouble() {
        double noTroubleChance;
        if (toughTown) {
            noTroubleChance = 0.66;
        } else {
            noTroubleChance = 0.33;
        }


        if (Math.random() > noTroubleChance) {
            printMessage = Color.RED_BRIGHT + Color.WHITE_BOLD_BRIGHT + "You couldn't find any trouble" + Color.RESET;
        } else {
            printMessage = Color.RED_BRIGHT + "You want trouble, stranger!  You got it!\nOof! Umph! Ow!\n" + Color.RESET;
            int goldDiff = (int) (Math.random() * 10) + 1;
            if (Math.random() > noTroubleChance) {
                printMessage += Color.RED_BRIGHT + "Okay, stranger! You proved yer mettle. Here, take my gold." + Color.RESET;
                printMessage += Color.RED_BRIGHT + "\nYou won the brawl and receive " + Color.YELLOW_BRIGHT + goldDiff + " gold." + Color.RESET;
                hunter.changeGold(goldDiff);
            } else {
                printMessage += Color.RED_BRIGHT + "That'll teach you to go lookin' fer trouble in MY town! Now pay up!";
                printMessage += Color.RED_BRIGHT + "\nYou lost the brawl and pay " + Color.YELLOW_BRIGHT + goldDiff + " gold." + Color.RESET;
                hunter.changeGold(-goldDiff);
            }
        }
    }

    public String toString() {
        return "This nice little town is surrounded by " + Color.CYAN_BOLD + terrain.getTerrainName() + Color.RESET + ".";
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain() {
        double rnd = Math.random();
        if (rnd < (1.0/6)) {
            return new Terrain("Mountains", "Rope");
        } else if (rnd < (1.0/3)) {
            return new Terrain("Ocean", "Boat");
        } else if (rnd < (1.0/2)) {
            return new Terrain("Plains", "Horse");
        } else if (rnd < (2.0/3)) {
            return new Terrain("Desert", "Water");
        } else if (rnd < (5.0/6)) {
            return new Terrain("Jungle", "Machete");
        } else {
            return new Terrain("Marsh", "Boots");
        }
    }

    /**
     * Determines whether a used item has broken.
     *
     * @return true if the item broke.
     */
    private boolean checkItemBreak() {
        double rand = Math.random();
        return (rand < 0.5);
    }
}