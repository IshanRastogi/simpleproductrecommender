package simple.product.recommender;

import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game,Integer> {

    Game findById(int id);

    Game findByName(String name);
}
