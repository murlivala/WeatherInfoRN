package mock.weatherfeed;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.Calendar;

/**
 * Created by Murlivala on 25/08/2017.
 */

public class UIManagerModule extends ReactContextBaseJavaModule {

    @ReactMethod
    public void getWeatherUpdate(Callback callback) {
        callback.invoke(WeatherInfo.weather_state_name,
                WeatherInfo.visibility,
                WeatherInfo.humidity,
                WeatherInfo.max_temp,
                WeatherInfo.min_temp,
                WeatherInfo.created,
                WeatherInfo.applicable_date,
                WeatherInfo.the_temp,
                WeatherInfo.wind_speed,
                WeatherInfo.wind_direction,
                WeatherInfo.air_pressure,
                WeatherInfo.predictability);
    }

    public UIManagerModule(ReactApplicationContext reactApplicationContext){
        super(reactApplicationContext);
    }

    @Override
    public String getName() {
        return "UIManagerModule";
    }
}