
package jhshadi.com.taki.services.vos;

public enum Type {

    NUMBER,
    PLUS,
    STOP,
    CHANGE_DIRECTION,
    CHANGE_COLOR,
    TAKI;

    public String value() {
        return name();
    }

    public static Type fromValue(String v) {
        return valueOf(v);
    }

}
