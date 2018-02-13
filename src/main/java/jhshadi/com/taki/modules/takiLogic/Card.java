package jhshadi.com.taki.modules.takiLogic;


import jhshadi.com.taki.modules.takiLogic.takiEnums.CardColors;
import jhshadi.com.taki.modules.takiLogic.takiEnums.CardTypes;

public class Card {

    private final CardTypes type;
    private final CardColors color;

    public Card(CardTypes type, CardColors color) {
        this.color = color;
        this.type = type;
    }

    public CardColors getColor() {
        return color;
    }

    public CardTypes getType() {
        return type;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Card other = (Card) obj;
        if (this.type != other.type) {
            return false;
        }
        if (this.color != other.color) {
            return false;
        }
        return true;
    }
}