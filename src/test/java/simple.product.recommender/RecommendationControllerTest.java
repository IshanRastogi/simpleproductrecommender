package simple.product.recommender;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
@AutoConfigureMockMvc
public class RecommendationControllerTest {

    @Autowired
    private MockMvc mockMVC;

    @InjectMocks
    private RecommendationController recommendationController;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private RecommendationRepository recommendationRepository;

    @Before
    public void setUp() {
        mockMVC  = standaloneSetup(recommendationController).build();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void recommendationDisabled() throws Exception {
        Customer customer = new Customer(222222, false);

        when(customerRepository.findCustomerById(222222)).thenReturn(customer);

        mockMVC.perform(get("/customers/222222/games/recommendations"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void userNotFound() throws Exception {
        when(customerRepository.findCustomerById(222222)).thenReturn(null);
        mockMVC.perform(get("/customers/222222/games/recommendations"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void recommendationFound() throws Exception {
        Customer customer   = new Customer(222222, true);
        when(customerRepository.findCustomerById(222222)).thenReturn(customer);

        Recommendation recommendation = new Recommendation(222222,1,1);
        List<Recommendation> recommendations = new ArrayList<Recommendation>(1);
        recommendations.add(recommendation);

        when(recommendationRepository.findByCustomerIdOrderByRank(222222)).thenReturn(recommendations);

        Game game = new Game("demoGame");
        when(gameRepository.findById(1)).thenReturn(game);

        mockMVC.perform(get("/customers/222222/games/recommendations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("demoGame"));
    }

    @Test
    public void recommendationUploadWrongHeader() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file",
                new FileInputStream(new File("test1.csv"))) ;

        mockMVC.perform(fileUpload("/recommendations/update").file(multipartFile))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void recommendationUploadWrongFields() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file",
                new FileInputStream(new File("test2.csv"))) ;

        mockMVC.perform(fileUpload("/recommendations/update").file(multipartFile))
                .andExpect(status().is4xxClientError());
    }

}