package mock.weatherfeed;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;


public class JsonUtils extends AsyncTask<Void, Integer, String> {
    private static final String TAG = JsonUtils.class.getSimpleName();

    private Activity activity;
    private String jsonString;
    private ResponseCallback responseCallback;
    private boolean isNetworkFailure;
    private int parseWhat;

    public JsonUtils(Activity activity, int what, String jsonData) {
        this.activity = activity;
        parseWhat = what;
        jsonString = jsonData;
    }

    public JsonUtils(String jsonData) {
        jsonString = jsonData;
    }

    public JsonUtils(){

    }

    JsonUtils getJsonUtils(){
        return new JsonUtils(jsonString);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            if (InternetUtil.isInternetOn(activity)) {
                jsonString = parseJson(jsonString);
            }else{
                isNetworkFailure = true;
            }

        } catch (Exception e) {
            return e.getMessage();
        }
        return jsonString;
    }

    protected void onProgressUpdate(Integer... index) {
        if(null != activity &&
                !activity.isFinishing()){
            if(index[0] == -1){
                //responseCallback.onUpdate(Constants.UPDATE_TITLE,index[0]);
            }else{
                //responseCallback.onUpdate(Constants.JSON_PARSE_PARTIAL,index[0]);
            }
        }else{
            cancel(true);
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(null != activity &&
                !activity.isFinishing()){
            if(isNetworkFailure){
                new ShowErrorDialogAndCloseApp(activity).getAlert(activity.getString(R.string.network_error)).show();
            }
            //responseCallback.onUpdate(Constants.JSON_PARSE_COMPLETED,0);
            responseCallback.onSuccess(result,parseWhat);
        }
    }

    public String parseJson(final String jsonData){

        /****************** Start Parse Response JSON Data *************/

        Log.d(TAG,"JsonUtils - parseJson ---- IN --"+jsonData);
        String result = "";
        String tmp = "";
        String datetime = null;
        String datetime1 = "";
        int lengthJsonArr = -1;
        try {

            /****************** End Parse Response JSON Data *************/
            JSONArray jsonMainNode = new JSONArray(jsonData);

            /*********** Process each JSON Node ************/
            lengthJsonArr = jsonMainNode.length();
            Log.d(TAG,"WeatherFeedActivity - parseJson -------- length:"+lengthJsonArr);
            for (int i = 0; i < lengthJsonArr; i++) {
                /****** Get Object for each JSON node.***********/
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                /******* Fetch node values **********/
                /**
                 * below parsing is as per given JSON
                 * could be improved to be more generic
                 */

                if(parseWhat == Constants.PARSE_WOEID) {
                    result = jsonChildNode.optString("woeid").toString();
                    break;
                }else if(parseWhat == Constants.PARSE_WEATHER){
                    result = jsonChildNode.optString("created").toString();
                    Log.d(TAG,"WeatherFeedActivity - parseJson -------- created:"+result);
                  /*  if(null == datetime){
                        datetime = result.substring(result.indexOf('T')+1,result.length()-1);
                        datetime = datetime.replace(":","");
                        datetime = datetime.replace(".","");
                        tmp = result;
                    }else{
                        datetime1 = result.substring(result.indexOf('T')+1,result.length()-1);
                        datetime1 = datetime1.replace(":","");
                        datetime1 = datetime1.replace(".","");
                        if(Long.parseLong(datetime) < Long.parseLong(datetime1)){
                            tmp = result;
                            datetime = datetime1;
                            Log.d(TAG,"WeatherFeedActivity - parseJson -------- latest datetime:"+tmp);
                        }
                    }*/
                    WeatherInfo.weather_state_name = jsonChildNode.optString("weather_state_name").toString();
                    WeatherInfo.max_temp = jsonChildNode.optString("max_temp").toString();
                    WeatherInfo.min_temp = jsonChildNode.optString("min_temp").toString();
                    WeatherInfo.applicable_date = jsonChildNode.optString("applicable_date").toString();
                    WeatherInfo.created = jsonChildNode.optString("created").toString();
                    WeatherInfo.humidity = jsonChildNode.optString("humidity").toString();
                    WeatherInfo.the_temp = jsonChildNode.optString("the_temp").toString();
                    WeatherInfo.humidity = jsonChildNode.optString("humidity").toString();
                    WeatherInfo.predictability = jsonChildNode.optString("predictability").toString();
                    WeatherInfo.air_pressure = jsonChildNode.optString("air_pressure").toString();
                    WeatherInfo.visibility = jsonChildNode.optString("visibility").toString();
                    WeatherInfo.wind_speed = jsonChildNode.optString("wind_speed").toString();
                    WeatherInfo.wind_direction = jsonChildNode.optString("wind_direction").toString();
                    WeatherInfo.isWeatherInfoAvailable = true;
                    break;

                }
            }

        } catch (Exception e) {
            Log.d(TAG,"WeatherFeedActivity - parseJson -------- Error parsing jSon:"+e.getMessage());
            e.printStackTrace();
			//responseCallback.onUpdate(Constants.DIALOG_DISMISS,0);
        }
        return result;
    }

    public void setResponseCallback(ResponseCallback responseCallback) {
        this.responseCallback = responseCallback;
    }

}
