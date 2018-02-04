package simple.product.recommender;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RecommendationRepository extends CrudRepository<Recommendation,Integer> {

    List<Recommendation> findByCustomerIdOrderByRank(long customerId);
}
