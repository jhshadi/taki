
package jhshadi.com.taki.services.vos;

public enum PlayerType {

    HUMAN,
    COMPUTER;

    public String value() {
        return name();
    }

    public static PlayerType fromValue(String v) {
        return valueOf(v);
    }

}
