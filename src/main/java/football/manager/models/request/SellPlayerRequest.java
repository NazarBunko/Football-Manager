package football.manager.models.request;

public class SellPlayerRequest {
    private Long playerId;
    private Long newTeamId;
    private Long price;

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getNewTeamId() {
        return newTeamId;
    }

    public void setNewTeamId(Long newTeamId) {
        this.newTeamId = newTeamId;
    }
}
