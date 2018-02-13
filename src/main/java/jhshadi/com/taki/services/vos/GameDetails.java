
package jhshadi.com.taki.services.vos;

public class GameDetails {

    protected int computerizedPlayers;
    protected int humanPlayers;
    protected int joinedHumanPlayers;
    protected String name;
    protected GameStatus status;

    /**
     * Gets the value of the computerizedPlayers property.
     * 
     */
    public int getComputerizedPlayers() {
        return computerizedPlayers;
    }

    /**
     * Sets the value of the computerizedPlayers property.
     * 
     */
    public void setComputerizedPlayers(int value) {
        this.computerizedPlayers = value;
    }

    /**
     * Gets the value of the humanPlayers property.
     * 
     */
    public int getHumanPlayers() {
        return humanPlayers;
    }

    /**
     * Sets the value of the humanPlayers property.
     * 
     */
    public void setHumanPlayers(int value) {
        this.humanPlayers = value;
    }

    /**
     * Gets the value of the joinedHumanPlayers property.
     * 
     */
    public int getJoinedHumanPlayers() {
        return joinedHumanPlayers;
    }

    /**
     * Sets the value of the joinedHumanPlayers property.
     * 
     */
    public void setJoinedHumanPlayers(int value) {
        this.joinedHumanPlayers = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link GameStatus }
     *     
     */
    public GameStatus getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link GameStatus }
     *     
     */
    public void setStatus(GameStatus value) {
        this.status = value;
    }

}
