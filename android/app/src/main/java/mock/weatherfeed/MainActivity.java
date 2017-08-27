package mock.weatherfeed;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.shell.MainReactPackage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.facebook.react.common.LifecycleState.RESUMED;

public class MainActivity extends AppCompatActivity implements ResponseCallback, DefaultHardwareBackBtnHandler {

    private final String TAG = MainActivity.class.getSimpleName();
    private ServiceDataClass serviceDataClass;
    private JsonUtils jsonUtils;

    //React native
    private ReactRootView mReactRootView;
    private ReactInstanceManager mReactInstanceManager;
    UIManagerModule uiManagerModule;
    private ProgressDialog mProgressDialog;

    Callback rnCallback;
    private final String REACT_APP = "WeatherInfoRN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
    }

    private void checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1234);
            }else{
                configureReactNative();
                setContentView(R.layout.activity_weather_feed);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1234) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    // SYSTEM_ALERT_WINDOW permission not granted...
                    Log.d(TAG,"SYSTEM_ALERT_WINDOW permission not granted...");
                }else{
                    configureReactNative();
                    setContentView(R.layout.activity_weather_feed);
                }
            }
        }
    }

    private void configureReactNative(){
        mReactRootView = new ReactRootView(this);
        mReactInstanceManager = ReactInstanceManager.builder()
                .setApplication(getApplication())
                .setBundleAssetName("index.android.bundle")
                .setJSMainModuleName("index.android")
                .addPackage(new MainReactPackage())
                .addPackage(new AnExampleReactPackage())
                .setUseDeveloperSupport(BuildConfig.DEBUG)
                .setInitialLifecycleState(RESUMED)
                .build();
        mReactRootView.startReactApplication(mReactInstanceManager, REACT_APP, null);
    }

    private void showReactNativeView(){
        setContentView(mReactRootView);
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        super.onBackPressed();
    }

    @Override protected void onPause() {
        super.onPause();
        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostPause(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostResume(this,this);
        }
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        dismiss();
        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostDestroy();
        }
    }

    @Override
    public void onBackPressed() {
        dismiss();
        if (mReactInstanceManager != null) {
            mReactInstanceManager.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    private void getLocation(){
        showProgressDialog("Getting weather info...");
        serviceDataClass = new ServiceDataClass(MainActivity.this,Constants.GET_WOEID,
                Constants.SERVICE_URL);
        serviceDataClass.setResponseCallback(this);
        serviceDataClass.execute();
    }

    @Override
    public void onSuccess(String result,int what) {
        Log.d(TAG,"onSuccess - "+result);
        switch(what){
            case Constants.GET_WOEID:
                jsonUtils = new JsonUtils(MainActivity.this,Constants.PARSE_WOEID,
                        result);
                jsonUtils.setResponseCallback(this);
                jsonUtils.execute();
                break;
            case Constants.GET_WEATHER:
                jsonUtils = new JsonUtils(MainActivity.this,Constants.PARSE_WEATHER,
                        result);
                jsonUtils.setResponseCallback(this);
                jsonUtils.execute();
                break;
            case Constants.PARSE_WOEID:
                showProgressDialog("Evaluating weather data...");
                serviceDataClass = new ServiceDataClass(MainActivity.this,Constants.GET_WEATHER,
                        Constants.BASE_URL+result+"/"+getDateString(-1));
                serviceDataClass.setResponseCallback(this);
                serviceDataClass.execute();
                break;
            case Constants.PARSE_WEATHER:
                Log.d(TAG,"onSuccess - "+WeatherInfo.weather_state_name);
                Log.d(TAG,"onSuccess - "+WeatherInfo.max_temp);
                Log.d(TAG,"onSuccess - "+WeatherInfo.min_temp);
                Log.d(TAG,"onSuccess - "+WeatherInfo.humidity);
                Log.d(TAG,"onSuccess - "+WeatherInfo.predictability);
                Log.d(TAG,"onSuccess - "+WeatherInfo.air_pressure);
                Log.d(TAG,"onSuccess - "+WeatherInfo.visibility);
                dismiss();
                showReactNativeView();
                break;
            default:
        }
    }

    @Override
    public void onFailure(String errorMessage) {
        /*Toast.makeText(getApplicationContext(),"Error:"+errorMessage,Toast.LENGTH_SHORT).show();
        dismiss();*/
        dismiss();
        Log.d(TAG,"onSuccess - "+errorMessage);
    }

    private Date getDate(int when) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, when);
        return cal.getTime();
    }

    /**
     *
     * @param when -1 for yesterday, 0 for today
     * @return date in yyyy/mm/dd format as String
     */
    private String getDateString(int when) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(getDate(when));
    }

    public void showProgressDialog(){
        showProgressDialog("");
    }

    public void showProgressDialog(String message){
        if(null == message || "".equals(message)){
            message = "Updating"+"...";
        }
        if(null == mProgressDialog){
            mProgressDialog = ProgressDialog.show(this, "", message, true, false);
            mProgressDialog.setCancelable(true);
        }else{
            mProgressDialog.setMessage(message);
        }
    }

    public void dismiss(){
        try {
            if (null != mProgressDialog && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        }catch(IllegalArgumentException e){
            Log.e(TAG, "dismiss() - IllegalArgumentException: " + e.getMessage());
        }catch (Exception e){
            Log.e(TAG, "dismiss() - Exception: " + e.getMessage());
        }
    }

    public void onPress(View v){
        getLocation();
    }
}
