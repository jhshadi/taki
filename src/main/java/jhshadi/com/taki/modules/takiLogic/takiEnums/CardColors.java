package jhshadi.com.taki.modules.takiLogic.takiEnums;

import java.util.ArrayList;
import java.util.List;

public enum CardColors {

    RED, YELLOW, GREEN, BLUE, NONE;

    public static boolean isValidColor(CardColors type) {
        return RED.ordinal() <= type.ordinal() && type.ordinal() <= BLUE.ordinal();
    }

    public static List<CardColors> getColorsOnly() {
        List<CardColors> validColors = new ArrayList<>();

        for (CardColors color : CardColors.values()) {
            if (isValidColor(color)) {
                validColors.add(color);
            }
        }

        return validColors;
    }
}