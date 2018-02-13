package jhshadi.com.taki.modules.takiLogic.exceptions;


import jhshadi.com.taki.modules.takiLogic.takiEnums.CardColors;

import java.util.List;

public class ChangeColorCardException extends Exception {

    private List<CardColors> validColors;

    public ChangeColorCardException(List<CardColors> validColors) {
        this.validColors = validColors;
    }

    public List<CardColors> getValidColors() {
        return validColors;
    }
}
