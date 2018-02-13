package jhshadi.com.taki.services;

import jhshadi.com.taki.modules.serverLogic.GameInfo;
import jhshadi.com.taki.modules.serverLogic.GamesManager;
import jhshadi.com.taki.modules.serverLogic.exceptions.GameAlreadyExist;
import jhshadi.com.taki.modules.serverLogic.exceptions.GameNotExistException;
import jhshadi.com.taki.modules.serverLogic.exceptions.InitializeLogicException;
import jhshadi.com.taki.modules.takiLogic.Player;
import jhshadi.com.taki.modules.takiLogic.exceptions.ExistPlayerNameException;
import jhshadi.com.taki.modules.takiLogic.exceptions.InvalidCardPlayedException;
import jhshadi.com.taki.modules.takiLogic.exceptions.InvalidNumberOfPlayersInGame;
import jhshadi.com.taki.services.exceptions.*;
import jhshadi.com.taki.services.vos.Card;
import jhshadi.com.taki.services.vos.GameDetails;
import jhshadi.com.taki.services.vos.PlayerDetails;
import jhshadi.com.taki.services.vos.convertors.TakiLogicConvertor;
import jhshadi.com.taki.services.vos.convertors.WsConvertor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("singleton")
public class TakiService {

    private final GamesManager gamesManager;

    public TakiService() {
        this.gamesManager = new GamesManager();
    }

    public List<jhshadi.com.taki.services.vos.Event> getEvents(int playerId, int eventId) throws InvalidPlayerException, InvalidEventException {
        GameInfo game;

        try {
            game = gamesManager.getGameByPlayerId(playerId);
        } catch (GameNotExistException ex) {
            throw new InvalidPlayerException();
        }

        int currentEventId = game.getLogic().getEventQueue().getCurrentEventId();
        if (eventId < 0 || currentEventId < eventId) {
            throw new InvalidEventException();
        }

        return WsConvertor.convertToEventsListWs(game.getLogic().getEventQueue().getEvents(eventId));
    }

    public void createGame(String name, int humanPlayers, int computerizedPlayers) throws DuplicateGameNameException, InvalidPlayerParameterException {
        try {
            gamesManager.addGame(name, humanPlayers, computerizedPlayers);
        } catch (GameAlreadyExist ex) {
            throw new DuplicateGameNameException();
        } catch (InvalidNumberOfPlayersInGame ex) {
            throw new InvalidPlayerParameterException();
        }
    }

    public int joinGame(String gameName, String playerName) throws GameDoesNotExistsException, InvalidPlayerParameterException {
        int playerId = 0;

        try {
            playerId = gamesManager.joinPlayerToGame(gameName, playerName);
        } catch (GameNotExistException ex) {
            throw new GameDoesNotExistsException();
        } catch (ExistPlayerNameException | InvalidNumberOfPlayersInGame | InitializeLogicException ex) {
            throw new InvalidPlayerParameterException();
        }

        return playerId;
    }

    public void throwCards(int playerId, int eventId, List<Card> cards) throws InvalidCardException, InvalidPlayerException {
        GameInfo game;

        try {
            game = gamesManager.getGameByPlayerId(playerId);
        } catch (GameNotExistException ex) {
            throw new InvalidPlayerException();
        }

        if (playerId == game.getLogic().getCurrentPlayer().getId()) {
            try {
                List<jhshadi.com.taki.modules.takiLogic.Card> logicalCards = TakiLogicConvertor.convertToPlayerCardList(cards);
                game.throwCards(playerId, logicalCards);
            } catch (InvalidCardPlayedException ex) {
                throw new InvalidCardException();
            }
        } else {
            throw new InvalidPlayerException();
        }
    }

    public void resign(int playerId) throws InvalidPlayerException {
        GameInfo game;
        Player player;

        try {
            game = gamesManager.getGameByPlayerId(playerId);
        } catch (GameNotExistException ex) {
            throw new InvalidPlayerException();
        }

        player = game.getPlayerById(playerId);
        if (player == null || player.isResigned()) {
            throw new InvalidPlayerException();
        } else {
            game.resignPlayer(player);
        }
    }



    public List<PlayerDetails> getPlayersDetails(java.lang.String gameName, int playerId) throws GameDoesNotExistsException {
        GameInfo game = null;
        List<PlayerDetails> result = new ArrayList();

        try {
            game = gamesManager.getGame(gameName);
        } catch (GameNotExistException ex) {
            throw new GameDoesNotExistsException();
        }

        for (Player player : game.getLogic().getPlayers()) {
            PlayerDetails wsPlayer = new PlayerDetails();
            wsPlayer.setName(player.getName());
            wsPlayer.setType(WsConvertor.convertToPlayerTypeWs(player.isIsHuman()));
            wsPlayer.setStatus(WsConvertor.convertToPlayerStatusWs(player.isResigned()));
            if (playerId == player.getId()) {
                wsPlayer.getCards().addAll(WsConvertor.convertToPlayerCardListWs(player.getDeck()));
            }

            result.add(wsPlayer);
        }

        return result;
    }

    public List<String> getWaitingGames() {
        return gamesManager.getWaitingGames();
    }

    public List<String> getActiveGames() {
        return gamesManager.getActiveGames();
    }

    public GameDetails getGameDetails(java.lang.String gameName) throws GameDoesNotExistsException {
        GameInfo game = null;
        GameDetails result = new GameDetails();

        try {
            game = gamesManager.getGame(gameName);
        } catch (GameNotExistException ex) {
            throw new GameDoesNotExistsException();
        }

        result.setName(game.getName());
        result.setComputerizedPlayers(game.getComputerizedPlayers());
        result.setHumanPlayers(game.getHumanPlayers());
        result.setJoinedHumanPlayers(game.getJoinedHumanPlayers());
        result.setStatus(WsConvertor.convertToGameStatusWs(game.getGameState()));

        return result;
    }
}
