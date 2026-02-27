package app.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import app.exception.BadRequestException;
import app.model.database.TagEntity;
import app.model.database.TokenEntity;
import app.model.database.UserEntity;
import app.model.database.VisualNovelEntity;
import app.model.in.VisualNovelBody;
import app.model.out.VisualNovelResponse;
import app.repository.TagRepository;
import app.repository.TokenRepository;
import app.repository.UserRepository;
import app.repository.VisualNovelRepository;
import helper.Helper;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class VisualNovelControllerTest {
    
    static final String DATA_FILE = "src/test/resources/visual_novel.json";

    static final String TEST_TOKEN = "1171660c4f41406d";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VisualNovelRepository visualNovelRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    TokenRepository tokenRepository;


    /** A method to initialize the database before each test. */
	@BeforeEach
	public void init() throws NoSuchAlgorithmException, IOException, BadRequestException{

        this.tokenRepository.deleteAll();
        this.userRepository.deleteAll();
        this.visualNovelRepository.deleteAll();

        String password = "green-mechanic";
        String verification_link = "https://mynrista-api/check-mail/24de8968a01e4e39";
        String hash = Helper.hash(password);
        
        String content = Helper.readAll(VisualNovelControllerTest.DATA_FILE);
        ObjectMapper objectMapper = new ObjectMapper();
        List<VisualNovelBody> visualNovelBodies = objectMapper.readValue(content, new TypeReference<List<VisualNovelBody>>(){});
        
        for(VisualNovelBody body : visualNovelBodies){
            Map<String, TagEntity> mapTags = this.mapTags(body.getTags());
            VisualNovelEntity entity = new VisualNovelEntity(body, mapTags);
            this.visualNovelRepository.save(entity);
        }
		
        UserEntity user = new UserEntity("Itsuki", "itsuki@gmail.com", hash, verification_link, true);
		this.userRepository.save(user);
        TokenEntity token = new TokenEntity(VisualNovelControllerTest.TEST_TOKEN, user, OffsetDateTime.now().plusSeconds(1000));
        this.tokenRepository.save(token);
		
	}




    @SuppressWarnings("null")
    @Test
    public void getVisualNovel_returns_15_titles() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders
                .get("/visual-novel"))
                    .andExpect(status().is(200))
				    .andExpect(jsonPath("$.length()", is(15)));
    }



    @Test
    public void getVisualNovel_returns_correct_Informations_About_Lotus_Reverie() throws Exception{

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/visual-novel"))
                    .andExpect(status().is(200))
                    .andReturn();

        String content = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        List<VisualNovelResponse> returned = objectMapper.readValue(content, new TypeReference<List<VisualNovelResponse>>(){});
        boolean lotusReverieFound = false;

        for(VisualNovelResponse visualNovel : returned){
            if("Lotus Reverie: First Nexus".equals(visualNovel.getTitle())){
                lotusReverieFound = true;
                assertNotNull(visualNovel.getId());
                assertEquals("14/01/2021", visualNovel.getReleaseDate());
                assertEquals("14 jan. 2021", visualNovel.getTextReleaseDate());
                assertTrue(visualNovel.getSummary().contains("A visual novel at the end of the world"));
                assertEquals(85, visualNovel.getPercentPositiveEvaluationOnSteam());
                assertEquals(57, visualNovel.getNumberEvaluationOnSteam());
                assertTrue(visualNovel.getDescription().contains("Cinque will start her own particular daily life, trying to find out"));
                assertEquals("Casual", visualNovel.getTags().get(0));
                assertEquals("Fantasy", visualNovel.getTags().get(1));
                assertEquals("Aventure", visualNovel.getTags().get(2));
                assertTrue(visualNovel.getImage_base64().contains("iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAA"));
            }
        }

        assertTrue(lotusReverieFound);
    }



    @Test
    public void postVisualNovel_allow_to_find_visual_novel_by_id_returned() throws Exception{
        
        String content = Helper.readAll("src/test/resources/body.json");
        String body = content == null ? "" : content;

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                                            .post("/visual-novel")
                                            .contentType("application/json")
                                            .header("Authorization", "Bearer " + TEST_TOKEN)
                                            .content(body))
                                                .andExpect(status().is(201))
                                                .andReturn();

        String link = result.getResponse().getHeader("LINK");
        assertNotNull(link);
        String[] parts = link.split("/");
        String id = parts[parts.length - 1];
        String expectedSummary = "The Good People (Na Daoine Maithe) is a lore-rich, choice-driven visual novel inspired by Irish myths";
        String expectedDescription = "The Good People (Na Daoine Maithe) is a lore-rich and choice-driven historical fantasy" +  
                " visual novel inspired by Irish mythology and Celtic folklore. Play as a tenant farmer from mid-19th century Ireland.";

        mockMvc.perform(MockMvcRequestBuilders
                .get("/visual-novel/" + id))
                    .andExpect(status().is(200))
				    .andExpect(jsonPath("$.id", is(Integer.valueOf(id))))
                    .andExpect(jsonPath("$.title", is("The Good People (Na Daoine Maithe)")))
                    .andExpect(jsonPath("$.releaseDate", is("01/11/2024")))
                    .andExpect(jsonPath("$.textReleaseDate", is("01 nov. 2024")))
                    .andExpect(jsonPath("$.summary", is(expectedSummary)))
                    .andExpect(jsonPath("$.percentPositiveEvaluationOnSteam", is(100)))
                    .andExpect(jsonPath("$.numberEvaluationOnSteam", is(33)))
                    .andExpect(jsonPath("$.description", is(expectedDescription)))
                    .andExpect(jsonPath("$.tags.length()", is(6)))
                    .andExpect(jsonPath("$.tags[0]", is("Casual")))
                    .andExpect(jsonPath("$.tags[1]", is("Fantasy")))
                    .andExpect(jsonPath("$.tags[2]", is("Aventure")))
                    .andExpect(jsonPath("$.image_base64", notNullValue()));
    }


    @Test
    public void putVisualNovel_modify_visual_novel() throws Exception{
        
        String content = Helper.readAll("src/test/resources/body.json");
        String body = content == null ? "" : content;

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                                            .get("/visual-novel"))
                                                .andExpect(jsonPath("$[0].title", not(is("The Good People (Na Daoine Maithe)"))))
                                                .andReturn();

        String contentResult = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        List<VisualNovelResponse> returned = objectMapper.readValue(contentResult, new TypeReference<List<VisualNovelResponse>>(){});
        Long id = returned.get(0).getId();


        mockMvc.perform(MockMvcRequestBuilders
                                .put("/visual-novel/" + id)
                                .contentType("application/json")
                                .header("Authorization", "Bearer " + TEST_TOKEN)
                                .content(body))
                                    .andExpect(status().is(204));

        String expectedSummary = "The Good People (Na Daoine Maithe) is a lore-rich, choice-driven visual novel inspired by Irish myths";
        String expectedDescription = "The Good People (Na Daoine Maithe) is a lore-rich and choice-driven historical fantasy" +  
                " visual novel inspired by Irish mythology and Celtic folklore. Play as a tenant farmer from mid-19th century Ireland.";

        mockMvc.perform(MockMvcRequestBuilders
                .get("/visual-novel/" + id))
                    .andExpect(status().is(200))
				    .andExpect(jsonPath("$.id", is(Integer.valueOf("" + id))))
                    .andExpect(jsonPath("$.title", is("The Good People (Na Daoine Maithe)")))
                    .andExpect(jsonPath("$.releaseDate", is("01/11/2024")))
                    .andExpect(jsonPath("$.textReleaseDate", is("01 nov. 2024")))
                    .andExpect(jsonPath("$.summary", is(expectedSummary)))
                    .andExpect(jsonPath("$.percentPositiveEvaluationOnSteam", is(100)))
                    .andExpect(jsonPath("$.numberEvaluationOnSteam", is(33)))
                    .andExpect(jsonPath("$.description", is(expectedDescription)))
                    .andExpect(jsonPath("$.tags.length()", is(6)))
                    .andExpect(jsonPath("$.tags[0]", is("Casual")))
                    .andExpect(jsonPath("$.tags[1]", is("Fantasy")))
                    .andExpect(jsonPath("$.tags[2]", is("Aventure")))
                    .andExpect(jsonPath("$.image_base64", notNullValue()));
    }


    @Test
    public void postVisualNovel_throws_401_if_no_header_authorization() throws Exception{
        
        String content = Helper.readAll("src/test/resources/body.json");
        String body = content == null ? "" : content;

        mockMvc.perform(MockMvcRequestBuilders
                                            .post("/visual-novel")
                                            .contentType("application/json")
                                            .content(body))
                                                .andExpect(status().is(401));
    }


    @Test
    public void postVisualNovel_throws_401_if_header_authorization_is_incorrect() throws Exception{
        
        String content = Helper.readAll("src/test/resources/body.json");
        String body = content == null ? "" : content;

        mockMvc.perform(MockMvcRequestBuilders
                                            .post("/visual-novel")
                                            .contentType("application/json")
                                            .header("Authorization", "Bearer ab6075c4f41406d")
                                            .content(body))
                                                .andExpect(status().is(401));
    }


    @Test
    public void postVisualNovel_throws_400_if_title_is_null() throws Exception{
        
        String content = Helper.readAll("src/test/resources/body2.json");
        String body = content == null ? "" : content;

        mockMvc.perform(MockMvcRequestBuilders
                                            .post("/visual-novel")
                                            .contentType("application/json")
                                            .header("Authorization", "Bearer ab6075c4f41406d")
                                            .content(body))
                                                .andExpect(status().is(401));
    }

    @Test
    public void postVisualNovel_throws_400_if_image_base64_is_null() throws Exception{
        
        String content = Helper.readAll("src/test/resources/body3.json");
        String body = content == null ? "" : content;

        mockMvc.perform(MockMvcRequestBuilders
                                            .post("/visual-novel")
                                            .contentType("application/json")
                                            .header("Authorization", "Bearer ab6075c4f41406d")
                                            .content(body))
                                                .andExpect(status().is(401));
    }



    /** Given a list of tag, this function return a map containing the TagEntity matching each tag.
    * If a tag wasn't already existing, the tag will be created in the database. */
    private Map<String, TagEntity> mapTags(List<String> tags){

        if(tags == null)
            return new HashMap<String, TagEntity>();

        Map<String, TagEntity> result = new HashMap<String, TagEntity>();
        List<TagEntity> allTags = this.tagRepository.list();

        for(String tagName: tags){
            TagEntity matching = null;

            for(TagEntity entity : allTags){
                if(entity.getTag().equals(tagName))
                    matching = entity;
            }

            if(matching == null){
                TagEntity t = new TagEntity(tagName);
                this.tagRepository.save(t);
                matching = t;
            }

            result.put(tagName, matching);
        }

        return result;
        
    }

}
