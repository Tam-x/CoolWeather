package xing.tan.coolweather.util;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import xing.tan.coolweather.db.City;
import xing.tan.coolweather.db.Country;
import xing.tan.coolweather.db.Province;

/**
 * Created by Tx.Loooper on 2018/11/5.
 */

public class Utility {
    /**
     * handle the result of server callback province data.
     *
     */
    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray provinces = new JSONArray(response);
                for(int i=0; i<provinces.length(); i++){
                    JSONObject obj = provinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName((String)obj.get("name"));
                    province.setId((int)obj.get("id"));
                    province.setProvinceCode((int)obj.get("id"));
                    province.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * handle the result of server callback city data.
     *
     */
    public static boolean handleCityResponse(String response, int provinceId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray provinces = new JSONArray(response);
                for(int i=0; i<provinces.length(); i++){
                    JSONObject obj = provinces.getJSONObject(i);
                    City city = new City();
                    city.setCityName((String)obj.get("name"));
                    city.setId((int)obj.get("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * handle the result of server callback city data.
     *
     */
    public static boolean handleCountryResponse(String response, int cityId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray provinces = new JSONArray(response);
                for(int i=0; i<provinces.length(); i++){
                    JSONObject obj = provinces.getJSONObject(i);
                    Country country = new Country();
                    country.setCountyName((String)obj.get("name"));
                    country.setId((int)obj.get("id"));
                    country.setCityId(cityId);
                    country.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

}
