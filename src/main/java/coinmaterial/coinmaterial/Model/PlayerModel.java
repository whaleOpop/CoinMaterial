package coinmaterial.coinmaterial.Model;

public class PlayerModel {
    private String nikcname;
    private Integer coin;

    public PlayerModel(String nikcname, Integer coin) {
        this.nikcname = nikcname;
        this.coin = coin;
    }
    public PlayerModel(){

    }

    public String getNikcname() {
        return nikcname;
    }

    public void setNikcname(String nikcname) {
        this.nikcname = nikcname;
    }

    public Integer getCoin() {
        return coin;
    }

    public void setCoin(Integer coin) {
        this.coin = coin;
    }
}