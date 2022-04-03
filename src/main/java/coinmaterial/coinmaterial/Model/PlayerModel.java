package coinmaterial.coinmaterial.Model;


/**
 * Implements PlayerModel wallet to serialize via Hashmapper
 * Usage:        getNickname, getCoin, setCoin
 * Requirements: none
*/
public class PlayerModel {
    private String nickname;
    private Integer coin;

    public PlayerModel(String nickname, Integer coin) {
        this.nickname = nickname;
        this.coin = coin;
    }
    public PlayerModel(){}

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getCoin() {
        return coin;
    }

    public void setCoin(Integer coin) {
        this.coin = coin;
    }
}