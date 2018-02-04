package simple.product.recommender;

import org.springframework.data.repository.CrudRepository;


public interface CustomerRepository extends CrudRepository<Customer, Long> {

    Customer findCustomerById(long id);
}
