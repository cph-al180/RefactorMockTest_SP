package testex;

import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItemInArray;
import static org.mockito.BDDMockito.given;
import org.mockito.Mock;
import testex.jokefetching.FetcherFactory;
import testex.jokefetching.IFetcherFactory;
import testex.jokefetching.IJokeFetcher;

@RunWith(MockitoJUnitRunner.class)
public class JokeFetcherTest {

    @Mock
    IDateFormatter dateFormatMock;
    @Mock
    IFetcherFactory factory;
    @Mock
    IJokeFetcher edu, chuck, moma, tambal;

    private Joke newJoke;
    private JokeFetcher jf;

    @Before
    public void setUp() {
        newJoke = new Joke("Mock Joke","Somewhere in hell");
        given(edu.getJoke()).willReturn(newJoke);
        given(chuck.getJoke()).willReturn(newJoke);
        given(moma.getJoke()).willReturn(newJoke);
        given(tambal.getJoke()).willReturn(newJoke);
        List<IJokeFetcher> fetchers = Arrays.asList(edu, chuck, moma, tambal);
        when(factory.getJokeFetchers("EduJoke,ChuckNorris,Moma,Tambal")).thenReturn(fetchers);
        List<String> types = Arrays.asList("EduJoke", "ChuckNorris", "Moma", "Tambal");
        when(factory.getAvailableTypes()).thenReturn(types);
        jf = new JokeFetcher(dateFormatMock, factory);
    }

    @Test
    public void testGetAvailableTypes() {
        List<String> types = jf.getAvailableTypes();
        assertThat(types, hasItems());
        assertThat(types.size(), greaterThan(0));
    }

    @Test
    public void testCheckIfValidToken() {
        boolean valid = jf.isStringValid("EduJoke,ChuckNorris,Moma,Tambal");
        boolean invalid = jf.isStringValid("cooljokez,9gag,morejokez,moomin,eclipse");
        assertThat(valid, equalTo(true));
        assertThat(invalid, equalTo(false));

    }

    @Test
    public void testGetJokes() throws JokeException {
        given(dateFormatMock.getFormattedDate(eq("Europe/Copenhagen"), anyObject())).willReturn("17 feb. 2017 10:56 AM");
        Jokes jokes = jf.getJokes("EduJoke,ChuckNorris,Moma,Tambal", "Europe/Copenhagen");

        assertThat(jokes.getJokes().size(), greaterThan(3));
        assertThat(jokes.getTimeZoneString(), containsString("17 feb. 2017 10:56 AM"));
        verify(dateFormatMock, times(1)).getFormattedDate(anyObject(), anyObject());

        for (Joke jo : jokes.getJokes()) {
            assertThat(jo, equalTo(newJoke));
        }

        verify(edu, times(1)).getJoke();
        verify(chuck, times(1)).getJoke();
        verify(moma, times(1)).getJoke();
        verify(tambal, times(1)).getJoke();
    }

    @Test
    public void testFetchers() {
        IFetcherFactory factory = new FetcherFactory();
        List<IJokeFetcher> result = factory.getJokeFetchers("EduJoke,ChuckNorris,Moma,Tambal");
        assertThat(result, hasItems());
        result.forEach((fetch) -> {
            assertThat(fetch, instanceOf(IJokeFetcher.class));
        });

    }

}
