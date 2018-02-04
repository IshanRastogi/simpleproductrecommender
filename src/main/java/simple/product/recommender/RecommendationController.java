package simple.product.recommender;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@EnableAutoConfiguration
@ControllerAdvice
public class RecommendationController {

    private static final int MAX_RECOMMENDATIONS_POSSIBLE = 10;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private RecommendationRepository recommendationRepository;

    /**
     * Default Route
     *
     * @return HTTP Status 200
     */
    @GetMapping("/")
    public ResponseEntity defaultRoute() {
        return new ResponseEntity("OK", HttpStatus.OK);
    }

    /**
     * Get recommendation method, serving 2 routes.
     * /customers/{customerId}/games/recommendations
     * and
     * /recommendations/games/customers/{customerId}
     *
     * @param count
     * @param id
     * @return json
     */
    @GetMapping(value = {"/customers/{id:[\\d]+}/games/recommendations", "/recommendations/games/customers/{id:[\\d]+}"})
    public ResponseEntity recommendation(@RequestParam(value = "count", defaultValue = "5") int count, @PathVariable("id") long id) {

        Customer customer = customerRepository.findCustomerById(id);

        if (null == customer) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if (!customer.isRecommendationActive()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        List<Recommendation> recommendations = recommendationRepository.findByCustomerIdOrderByRank(customer.getId());

        count = Math.min(
                Math.min(count, RecommendationController.MAX_RECOMMENDATIONS_POSSIBLE),
                recommendations.size()
        );

        String[] games = new String[count];

        for (int i = 0; i < count; i++) {
            games[i] = gameRepository.findById(recommendations.get(i).getGameId()).getName();
            System.out.println("Game Identified :" + games[i] + "\n");
        }

        return new ResponseEntity(games, HttpStatus.OK);
    }

    /**
     * Post Endpoint to upload CSV file
     *
     * @param file
     * @return json
     * @throws java.io.IOException
     * @throws InvalidCSVException
     */
    @PostMapping(value = "/recommendations/update")
    @ResponseBody
    public ResponseEntity recommendationUpload(@RequestParam("file") MultipartFile file) throws java.io.IOException, InvalidCSVException {
        if (file.isEmpty()) {
            return new ResponseEntity("No file uploaded", HttpStatus.OK);
        }

        String content = new String(file.getBytes());

        List<CustomerRecommendationSet> customerRecommendationSet = CustomerRecommendationSets.fromCSV(content);

        /**
         * Deleting the data currently being held in the database, to avoid duplicates
         */
        recommendationRepository.deleteAll();
        gameRepository.deleteAll();
        customerRepository.deleteAll();

        for (CustomerRecommendationSet recommendationSet : customerRecommendationSet) {

            customerRepository.save(new Customer(recommendationSet.getCustomerId(), recommendationSet.isRecommendationsEnabled()));
            recommendationRepository.deleteRecommendationByCustomerId(recommendationSet.getCustomerId());

            int ranking = 0;

            String[] games = recommendationSet.getRecommendations();
            for (String game : games) {

                int gameId = 0;
                Game gameEntity = gameRepository.findByName(game);
                if (null == gameEntity) {
                    gameEntity = gameRepository.save(new Game(game));
                }

                gameId = gameEntity.getId();

                recommendationRepository.save(
                        new Recommendation(
                                recommendationSet.getCustomerId(),
                                gameId,
                                ranking
                        )
                );
                ++ranking;
            }
        }

        return new ResponseEntity("File Uploaded", HttpStatus.OK);
    }

    @ExceptionHandler(InvalidCSVException.class)
    protected ResponseEntity handleCSVError(InvalidCSVException ex) {
        return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
