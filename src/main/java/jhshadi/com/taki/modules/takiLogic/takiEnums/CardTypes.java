package jhshadi.com.taki.modules.takiLogic.takiEnums;

public enum CardTypes {

    ONE, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, PLUS, PLUS_2, STOP, CHANGE_DIRECTION, TAKI, CHANGE_COLOR;
    private static final int NUMBER_CARD_ORDINAL_FIX1 = 1;
    private static final int NUMBER_CARD_ORDINAL_FIX2 = 2;

    public static boolean isNumber(CardTypes type) {
        return ONE.ordinal() <= type.ordinal() && type.ordinal() <= NINE.ordinal();
    }

    public static boolean isSpecial(CardTypes type) {
        return !isNumber(type);
    }

    public static boolean haveColor(CardTypes type) {
        return ONE.ordinal() <= type.ordinal() && type.ordinal() <= TAKI.ordinal();
    }

    public static int getCardNumber(CardTypes type) {
        int cardNumber = 0;

        if (type.ordinal() < THREE.ordinal()) {
            cardNumber = type.ordinal() + NUMBER_CARD_ORDINAL_FIX1;
        } else if (THREE.ordinal() <= type.ordinal() && type.ordinal() <= NINE.ordinal()) {
            cardNumber = type.ordinal() + NUMBER_CARD_ORDINAL_FIX2;
        }

        return cardNumber;
    }
}