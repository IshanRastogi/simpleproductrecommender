package simple.product.recommender;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * A simple class to represent basic details of
 * a Customer consisting of an ID
 * and preference for recommendations
 */
@Entity
public class Customer {

    @Id
    private long id;
    private boolean recommendationActive;

    protected Customer() {
    }

    public Customer(long id, boolean recommendationActive) {
        this.id = id;
        this.recommendationActive = recommendationActive;
    }

    public long getId() {
        return id;
    }

    public boolean isRecommendationActive() {
        return recommendationActive;
    }
}
