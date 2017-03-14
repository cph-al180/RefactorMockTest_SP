package testex.jokefetching;

import java.util.List;

public interface IFetcherFactory {
  List<String> getAvailableTypes();
  List<IJokeFetcher> getJokeFetchers(String jokesToFetch);
}
