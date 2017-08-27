package mock.weatherfeed;

public class Constants {
    public static final int DIALOG_DISMISS = 0;
    public static final int SHOW_DIALOG = 1;
    public static final int LIST_UPDATE = 2;
    public static final int JSON_PARSE_PARTIAL = 3;
    public static final int JSON_PARSE_COMPLETED = 4;
    public static final int IMAGE_DOWNLOAD_COMPLETED = 5;
    public static final int UPDATE_TITLE = 6;
    public static final int PARSE_WOEID = 7;
    public static final int PARSE_WEATHER = 8;
    public static final int GET_WOEID = 9;
    public static final int GET_WEATHER = 10;
    public static final String BASE_URL = "https://www.metaweather.com/api/location/";
    public static final String BASE_URL_SUFFIX = "search/?query=";
    public static String LOCATION = "brisbane";
    public static final String SERVICE_URL = BASE_URL + BASE_URL_SUFFIX + LOCATION;
}
