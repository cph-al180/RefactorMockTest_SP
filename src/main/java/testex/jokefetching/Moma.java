package testex.jokefetching;

import static com.jayway.restassured.RestAssured.given;
import testex.Joke;

public class Moma implements IJokeFetcher {

    @Override
    public Joke getJoke() {
        try {
            //API does not set response type to JSON, so we have to force the response to read as so
            String joke = given().get("http://api.yomomma.info/").andReturn().jsonPath().getString("joke");
            return new Joke(joke, "http://api.yomomma.info/");
        } catch (Exception e) {
            return null;
        }
    }
    
}
