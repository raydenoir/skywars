package thelonebarkeeper.mgame.data;

public class Data {

    private String playerName;
    private int kills;
    private boolean isWinner = false;

    public Data(String playerName) {
        this.playerName = playerName;
    }

    public int getKills() {
        return kills;
    }

    public void addKill() {
        this.kills++;
    }

    public void setWinner() {
        isWinner = true;
    }


    @Override
    public String toString() {
        return playerName+","+kills+","+isWinner;
    }

}
