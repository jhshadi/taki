package jhshadi.com.taki.modules.serverLogic;


import jhshadi.com.taki.modules.serverLogic.enums.GameState;
import jhshadi.com.taki.modules.serverLogic.exceptions.GameAlreadyExist;
import jhshadi.com.taki.modules.serverLogic.exceptions.GameNotExistException;
import jhshadi.com.taki.modules.serverLogic.exceptions.InitializeLogicException;
import jhshadi.com.taki.modules.takiLogic.Player;
import jhshadi.com.taki.modules.takiLogic.exceptions.ExistPlayerNameException;
import jhshadi.com.taki.modules.takiLogic.exceptions.InvalidNumberOfPlayersInGame;

import java.util.*;


public class GamesManager implements OnFinishedGameInterface {

    // Data Members
    private final Map<String, GameInfo> waitingGames;
    private final Map<String, GameInfo> activeGames;
    private final Map<String, GameInfo> finishedGames;
    private final Map<Integer, GameInfo> allPlayers;

    // C'tor
    public GamesManager() {
        this.waitingGames = new HashMap();
        this.activeGames = new HashMap();
        this.finishedGames = new HashMap();
        this.allPlayers = new HashMap();
        
        GameInfo.setOnFinishGame(this);
    }

    // Public Functions
    public GameInfo addGame(String name, int humanPlayers, int computerizedPlayers) throws GameAlreadyExist, InvalidNumberOfPlayersInGame {
        GameInfo game;

        if (isGameExist(name)) {
            throw new GameAlreadyExist();
        } else {
            game = waitingGames.put(name, new GameInfoRegular(name, computerizedPlayers, humanPlayers));
        }

        return game;
    }

    public GameInfo getGame(String gameName) throws GameNotExistException {
        GameInfo game;

        if (waitingGames.containsKey(gameName)) {
            game = waitingGames.get(gameName);
        } else if (activeGames.containsKey(gameName)) {
            game = activeGames.get(gameName);
        } else if (finishedGames.containsKey(gameName)) {
            game = finishedGames.get(gameName);
        } else {
            throw new GameNotExistException();
        }

        return game;
    }

    public GameInfo removeGame(String gameName) throws GameNotExistException {
        GameInfo game;

        if (finishedGames.containsKey(gameName)) {
            game = finishedGames.remove(gameName);
        } else if (activeGames.containsKey(gameName)) {
            game = activeGames.remove(gameName);
        } else if (waitingGames.containsKey(gameName)) {
            game = waitingGames.remove(gameName);
        } else {
            throw new GameNotExistException();
        }

        for (Player player : game.getAllPlayers()) {
            allPlayers.remove(player.getId());
        }

        return game;
    }

    public List<String> getWaitingGames() {
        return new ArrayList(waitingGames.keySet());
    }

    public List<String> getActiveGames() {
        return new ArrayList(activeGames.keySet());
    }

    public int joinPlayerToGame(String gameName, String playerName) throws GameNotExistException, ExistPlayerNameException, InvalidNumberOfPlayersInGame, InitializeLogicException {
        GameInfo game;
        Player joinedPlayer;

        if (!waitingGames.containsKey(gameName)) {
            throw new GameNotExistException();
        }

        game = waitingGames.get(gameName);
        joinedPlayer = game.joinHumanPlayer(playerName);

        allPlayers.put(joinedPlayer.getId(), game);

        if (game.getGameState() != GameState.WAITING) {
            setGameToActive(game);
        }

        return joinedPlayer.getId();
    }

    public GameInfo getGameByPlayerId(int id) throws GameNotExistException {
        if (!allPlayers.containsKey(id)) {
            throw new GameNotExistException();
        }
        
        return allPlayers.get(id);
    }

    // Private Functions
    private boolean isGameExist(String gameName) {
        return waitingGames.containsKey(gameName) || activeGames.containsKey(gameName);
    }

    private void setGameToActive(GameInfo game) {
        activeGames.put(game.getName(), game);
        waitingGames.remove(game.getName());
    }

    private void setGameToFinish(GameInfo game) {
        finishedGames.put(game.getName(), game);
        activeGames.remove(game.getName());
    }

    private void startCountdownToRemoveGame(final String gameName) {
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    removeGame(gameName);
                } catch (GameNotExistException ex) {
                    // Nothing to be done
                }
            }
        }, 30 * 1000);
    }

    @Override
    public void moveGameToFinishList(String gameName) {
        try {
            setGameToFinish(getGame(gameName));
            startCountdownToRemoveGame(gameName);
        } catch (GameNotExistException ex) {
            // Nothing to be done
        }
    }
}
