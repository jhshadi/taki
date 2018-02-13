package jhshadi.com.taki.modules.takiLogic;

public abstract class Player {

    private static int playersIds = 0;
    private int id;
    private boolean isHuman;
    private String name;
    private Deck deck;
    private boolean resigned;
    private boolean joined;
    
    public Player(boolean isHuman, String name) {
        this.id = ++playersIds;
        this.isHuman = isHuman;
        this.name = name;
        this.resigned = false;
        this.joined = false;
        deck = new Deck();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isIsHuman() {
        return isHuman;
    }

    public Deck getDeck() {
        return deck;
    }

    public boolean isResigned() {
        return resigned;
    }

    public boolean isJoined() {
        return joined;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setResigned(boolean resigned) {
        this.resigned = resigned;
    }

    public void setJoined(boolean joined) {
        this.joined = joined;
    }

    boolean hasPlayerWon() {
        return deck.isDeckEmpty();
    }

    public static abstract class PlayerFactory {

        public static Player create(boolean isHuman, String playerName) {
            Player newPlayer;

            if (isHuman == true) {
                newPlayer = new HumanPlayer(playerName);
            } else {
                if (playerName != null) {
                    newPlayer = new ComputerPlayer(playerName);
                }
                else {
                    newPlayer = new ComputerPlayer();
                }
            }

            return newPlayer;
        }
    }
}