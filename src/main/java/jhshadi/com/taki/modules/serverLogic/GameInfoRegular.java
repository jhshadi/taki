package jhshadi.com.taki.modules.serverLogic;

import jhshadi.com.taki.modules.serverLogic.exceptions.InitializeLogicException;
import jhshadi.com.taki.modules.takiLogic.Player;
import jhshadi.com.taki.modules.takiLogic.TakiLogic;
import jhshadi.com.taki.modules.takiLogic.exceptions.ExistPlayerNameException;
import jhshadi.com.taki.modules.takiLogic.exceptions.InvalidNumberOfPlayersInGame;
import jhshadi.com.taki.modules.takiLogic.exceptions.NoMoreCardsToDrawException;

public class GameInfoRegular extends GameInfo {

    GameInfoRegular(String name, int computerizedPlayers, int humanPlayers) throws InvalidNumberOfPlayersInGame {
        super();

        this.name = name;
        this.computerizedPlayers = computerizedPlayers;
        this.humanPlayers = humanPlayers;
        this.joinedHumanPlayers = 0;
        this.logic = TakiLogic.LogicFactory.create();

        if (!super.validatePlayersNumbers()) {
            throw new InvalidNumberOfPlayersInGame();
        }
    }

    @Override
    Player joinHumanPlayerHelper(String playerName) throws ExistPlayerNameException, InvalidNumberOfPlayersInGame {
        Player joinPlayer = Player.PlayerFactory.create(true, playerName);

        joinPlayer.setJoined(true);
        logic.addPlayer(joinPlayer);

        return joinPlayer;
    }

    @Override
    void initializeGameLogic() throws InitializeLogicException {
        try {
            for (int i = 0; i < computerizedPlayers; i++) {
                logic.addPlayer(Player.PlayerFactory.create(false, null));
            }
            logic.initializeGame();
        } catch (InvalidNumberOfPlayersInGame | NoMoreCardsToDrawException | ExistPlayerNameException ex) {
            setGameToFinishList();
            throw new InitializeLogicException();
        }
    }

}
