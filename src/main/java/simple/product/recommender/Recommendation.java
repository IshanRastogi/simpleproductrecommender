package simple.product.recommender;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


/**
 * A simple class that holds the relationship
 * between a customer and a game.
 * The relationship key is "rank" which indicates
 * the order in which a game will be recommended to
 * the customer
 */

@Entity
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private long customerId;
    private int gameId;
    private int rank;

    public Recommendation() {
    }

    public Recommendation(long customerId, int gameId, int rank) {
        this.customerId = customerId;
        this.gameId = gameId;
        this.rank = rank;
    }

    public int getId() {
        return id;
    }

    public long getCustomerId() {
        return customerId;
    }

    public int getGameId() {
        return gameId;
    }

    public int getRank() {
        return rank;
    }
}
