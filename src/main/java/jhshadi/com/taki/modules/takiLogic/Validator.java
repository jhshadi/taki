package jhshadi.com.taki.modules.takiLogic;


import jhshadi.com.taki.modules.takiLogic.takiEnums.*;

abstract class Validator {

    public static boolean validateCard(Card currentCard, CardColors currentColor, int plus2Modifier, boolean isTakiStrikeOn, Card newCard) {
        boolean isValid = false;

        if (isTakiStrikeOn == true) {
            isValid = validateOpenTakiMode(currentCard, newCard, currentColor);
        } else if (plus2Modifier != TakiLogic.PLUS_2_DRAW_CARDS_NOT_ACTIVE) {
            isValid = validatePlus2Mode(newCard);
        } else if (newCard.getType() == CardTypes.CHANGE_COLOR) {
            isValid = validateChangeColor(currentCard);
        } else if (validateCardType(currentCard, newCard) == true) {
            isValid = true;
        } else if (validateCardColor(currentColor, newCard) == true) {
            isValid = true;
        }

        return isValid;
    }

    private static boolean validateOpenTakiMode(Card currentCard, Card newCard, CardColors currentColor) {
        return validateCardColor(currentColor, newCard) || validateCardType(currentCard, newCard);
    }

    private static boolean validatePlus2Mode(Card newCard) {
        return newCard.getType() == CardTypes.PLUS_2;
    }

    private static boolean validateChangeColor(Card currentCard) {
        return currentCard.getType() != CardTypes.CHANGE_COLOR;
    }

    private static boolean validateCardColor(CardColors currentColor, Card newCard) {
        return newCard.getColor() == currentColor;
    }

    private static boolean validateCardType(Card currentCard, Card newCard) {
        return newCard.getType() == currentCard.getType();
    }
}