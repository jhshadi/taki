
package jhshadi.com.taki.services.vos;

public enum Value {

    ONE,
    TWO_PLUS,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE;

    public String value() {
        return name();
    }

    public static Value fromValue(String v) {
        return valueOf(v);
    }

}
