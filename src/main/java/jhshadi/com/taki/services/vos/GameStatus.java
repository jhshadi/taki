
package jhshadi.com.taki.services.vos;

public enum GameStatus {

    WAITING,
    ACTIVE,
    FINISHED;

    public String value() {
        return name();
    }

    public static GameStatus fromValue(String v) {
        return valueOf(v);
    }

}
