package jhshadi.com.taki.modules.takiLogic;


import jhshadi.com.taki.modules.takiLogic.exceptions.ChangeColorCardException;
import jhshadi.com.taki.modules.takiLogic.exceptions.InvalidCardPlayedException;
import jhshadi.com.taki.modules.takiLogic.exceptions.NoMoreCardsToDrawException;
import jhshadi.com.taki.modules.takiLogic.takiEnums.CardColors;
import jhshadi.com.taki.modules.takiLogic.takiEnums.CardTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComputerPlayer extends Player {
    
    private static final String COMPUTER_NAME = "COMPUTER";
    private static int computerCount = 0;
    private List<Card> cardsPlayed;
    
    ComputerPlayer() {
        super(false, COMPUTER_NAME + " " + computerCount++);
        cardsPlayed = new ArrayList<>();
    }
    
    ComputerPlayer(String name) {
        super(false, name);
        cardsPlayed = new ArrayList<>();
    }
    

    static void setComputerCount(int computerCount) {
        ComputerPlayer.computerCount = computerCount;
    }
    
    // Computer Player Ai Functions
    public void aiPlayComputerPlayer(TakiLogic logic) {
        while (logic.isTurnOver() == false) {
            aiPlayComputerPlayerTurn(logic);
        }
        
        if (cardsPlayed.isEmpty() == true) {
            logic.getEventQueue().push(Event.EventFactory.createCardsTakenFromStack());
        }
        else {
            logic.getEventQueue().push(Event.EventFactory.createCardsThrown(cardsPlayed));
        }
        cardsPlayed.clear();
    }   
    private void aiPlayComputerPlayerTurn(TakiLogic logic) {
        Card playedCard;
        boolean isValidTurn;
        boolean noCardsToPlay = false;
        int cardIndex = 0;
        
        do {
            isValidTurn = true;
            try {
                playedCard = this.getDeck().get(cardIndex);
                logic.playCardInTurn(playedCard);
                cardsPlayed.add(playedCard);
            }
            catch (ChangeColorCardException e) {
                aiChangeColor(logic, e);
                cardsPlayed.add(new Card(CardTypes.CHANGE_COLOR, logic.getCurrentColor()));
            }
            catch (InvalidCardPlayedException e) {
                isValidTurn = false;
            }
            catch (IndexOutOfBoundsException e) {
                noCardsToPlay = true;
            }
            
            cardIndex++;
        } while (isValidTurn == false && noCardsToPlay == false);
        
        if (noCardsToPlay == true) {
            aiPlayOtherActionInTurn(logic);
        }
    }
    private void aiPlayOtherActionInTurn(TakiLogic logic) {  
        try {
            logic.otherActionsInTurn();
        }
        catch (NoMoreCardsToDrawException e) {
            /* No action need to be done */
        }
    }
    private void aiChangeColor(TakiLogic logic, ChangeColorCardException e) {
        boolean isValid;
        
        do{
            isValid = true;
            try {
                logic.changeCurrentColor(aiSelectColorForChangeColor(e));
            }
            catch (ChangeColorCardException c) {
                isValid = false;
            }
        } while (isValid == false);

    }
    private CardColors aiSelectColorForChangeColor(ChangeColorCardException e) {
        Random rand = new Random();
        int  colorIndex = rand.nextInt(e.getValidColors().size());
        
        return e.getValidColors().get(colorIndex);
    }
}
