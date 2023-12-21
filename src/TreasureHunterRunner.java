public class TreasureHunterRunner {
    public static void main(String[] args) {
        OutputWindow display = new OutputWindow();
        TreasureHunter game = new TreasureHunter();
        game.play(display);
    }
}