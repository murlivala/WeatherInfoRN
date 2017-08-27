package mock.weatherfeed;

import android.widget.Toast;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.IllegalViewOperationException;

import java.util.Calendar;

/**
 * Created by Murlivala on 25/08/2017.
 */

public class UIManagerModule extends ReactContextBaseJavaModule {

    /*@ReactMethod
    public void measureLayout(
            int tag,
            int ancestorTag,
            Callback errorCallback,
            Callback successCallback) {
        try {
            measureLayout(tag, ancestorTag,null,null);
            successCallback.invoke(1, 2, 3, 4);
        } catch (IllegalViewOperationException e) {
            errorCallback.invoke(e.getMessage());
        }
    }*/

    @ReactMethod
    public void measureLayout( int tag, int ancestorTag, Promise promise) {
        try {
            measureLayout(tag, ancestorTag,promise);
            WritableMap map = Arguments.createMap();
            map.putDouble("relativeX", 1);
            map.putDouble("relativeY", 2);
            map.putDouble("width", 3);
            map.putDouble("height", 4);
            promise.resolve(map);
        } catch (IllegalViewOperationException e) {
            promise.reject("E_LAYOUT_ERROR", e);
        }
    }

    @ReactMethod
    public void update(Callback callback) {
        AppUtils.sCallback = callback;
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

    @ReactMethod
    public void show(String message) {
        Toast.makeText(getReactApplicationContext(), message+WeatherInfo.created, Toast.LENGTH_SHORT).show();
    }

    public UIManagerModule(ReactApplicationContext reactApplicationContext){
        super(reactApplicationContext);
    }

    @Override
    public String getName() {
        return "UIManagerModule";
    }
}