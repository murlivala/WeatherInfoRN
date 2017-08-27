package mock.weatherfeed;

public class Constants {
    public static final int PARSE_WOEID = 0;
    public static final int PARSE_WEATHER = 1;
    public static final int GET_WOEID = 2;
    public static final int GET_WEATHER = 3;
    public static final String BASE_URL = "https://www.metaweather.com/api/location/";
    public static final String BASE_URL_SUFFIX = "search/?query=";
    public static String LOCATION = "brisbane";
    public static final String SERVICE_URL = BASE_URL + BASE_URL_SUFFIX + LOCATION;
}
