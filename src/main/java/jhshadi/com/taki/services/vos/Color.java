
package jhshadi.com.taki.services.vos;

public enum Color {

    RED,
    GREEN,
    BLUE,
    YELLOW;

    public String value() {
        return name();
    }

    public static Color fromValue(String v) {
        return valueOf(v);
    }

}
