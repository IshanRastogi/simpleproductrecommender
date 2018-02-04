package simple.product.recommender;

public interface StorageInterface {
    public CustomerRecommendationSet getItem(int customerId);

    public boolean setItem(int customerId, CustomerRecommendationSet recommendationSet);
}
