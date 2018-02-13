package jhshadi.com.taki.services.vos.convertors;

import jhshadi.com.taki.modules.takiLogic.takiEnums.CardColors;
import jhshadi.com.taki.modules.takiLogic.takiEnums.CardTypes;
import jhshadi.com.taki.services.vos.Card;
import jhshadi.com.taki.services.vos.Color;
import jhshadi.com.taki.services.vos.Type;
import jhshadi.com.taki.services.vos.Value;

import java.util.ArrayList;
import java.util.List;

public class TakiLogicConvertor {

    // Cards
    public static List<jhshadi.com.taki.modules.takiLogic.Card> convertToPlayerCardList(List<Card> cards) {
        List<jhshadi.com.taki.modules.takiLogic.Card> res = new ArrayList<>();

        for (Card card : cards) {
            res.add(convertToCard(card));
        }

        return res;
    }

    public static jhshadi.com.taki.modules.takiLogic.Card convertToCard(Card card) {
        jhshadi.com.taki.modules.takiLogic.takiEnums.CardColors color = convertToColor(card.getColor());
        jhshadi.com.taki.modules.takiLogic.takiEnums.CardTypes type = convertToType(card);

        return new jhshadi.com.taki.modules.takiLogic.Card(type, color);
    }

    public static CardColors convertToColor(Color color) {
        CardColors res = null;

        if (color != null) {
            switch (color) {
                case BLUE:
                    res = CardColors.BLUE;
                    break;
                case GREEN:
                    res = CardColors.GREEN;
                    break;
                case RED:
                    res = CardColors.RED;
                    break;
                case YELLOW:
                    res = CardColors.YELLOW;
                    break;
            }
        } else {
            res = CardColors.NONE;
        }

        return res;
    }

    public static CardTypes convertToType(Card card) {
        CardTypes res = null;

        if (card.getType() == Type.NUMBER) {
            res = convertToValue(card.getValue());
        } else {
            switch (card.getType()) {
                case CHANGE_COLOR:
                    res = CardTypes.CHANGE_COLOR;
                    break;
                case CHANGE_DIRECTION:
                    res = CardTypes.CHANGE_DIRECTION;
                    break;
                case PLUS:
                    res = CardTypes.PLUS;
                    break;
                case STOP:
                    res = CardTypes.STOP;
                    break;
                case TAKI:
                    res = CardTypes.TAKI;
                    break;
            }
        }

        return res;
    }

    public static CardTypes convertToValue(Value value) {
        CardTypes res = null;

        switch (value) {
            case ONE:
                res = CardTypes.ONE;
                break;
            case TWO_PLUS:
                res = CardTypes.PLUS_2;
                break;
            case THREE:
                res = CardTypes.THREE;
                break;
            case FOUR:
                res = CardTypes.FOUR;
                break;
            case FIVE:
                res = CardTypes.FIVE;
                break;
            case SIX:
                res = CardTypes.SIX;
                break;
            case SEVEN:
                res = CardTypes.SEVEN;
                break;
            case EIGHT:
                res = CardTypes.EIGHT;
                break;
            case NINE:
                res = CardTypes.NINE;
                break;
        }

        return res;
    }
}
