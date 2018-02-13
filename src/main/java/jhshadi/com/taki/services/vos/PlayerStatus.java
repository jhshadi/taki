
package jhshadi.com.taki.services.vos;

public enum PlayerStatus {

    ACTIVE,
    RETIRED;

    public String value() {
        return name();
    }

    public static PlayerStatus fromValue(String v) {
        return valueOf(v);
    }

}
