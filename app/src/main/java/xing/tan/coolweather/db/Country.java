package xing.tan.coolweather.db;

import org.litepal.crud.LitePalSupport;

/**
 * Created by Tx.Loooper on 2018/11/5.
 */

public class Country extends LitePalSupport{

    private int id;
    private String countyName;
    private String weatherId;
    private int cityId;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
