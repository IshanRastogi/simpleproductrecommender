package simple.product.recommender;

/**
 * A simple class that holds the Id, recommendation choice
 * and the ordered recommended games for a customer
 */
public class CustomerRecommendationSet {

    private long customerId;
    private boolean recommendationsEnabled;
    private String[] recommendations;

    public CustomerRecommendationSet(long customerId, boolean recommendationsEnabled, String[] recommendations) {
        this.customerId = customerId;
        this.recommendationsEnabled = recommendationsEnabled;
        this.recommendations = recommendations;
    }

    public long getCustomerId() {
        return customerId;
    }

    public boolean isRecommendationsEnabled() {
        return recommendationsEnabled;
    }

    public String[] getRecommendations() {
        return recommendations;
    }
}
