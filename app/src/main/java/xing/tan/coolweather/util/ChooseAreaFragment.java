package xing.tan.coolweather.util;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.LitePalDB;
import org.litepal.crud.LitePalSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import xing.tan.coolweather.R;
import xing.tan.coolweather.db.City;
import xing.tan.coolweather.db.Country;
import xing.tan.coolweather.db.Province;

/**
 * Created by Tx.Loooper on 2018/11/5.
 */

public class ChooseAreaFragment extends Fragment {

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_CONTY = 2;
    private ProgressDialog progressDialog;
    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();
    private List<Province> provinces;
    private List<City> cities;
    private List<Country> countries;
    private Province selectedProvinec;
    private City selectedCity;
    private Country selectedConutry;
    private int currentLevel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        LitePal.deleteAll(Province.class);
        LitePal.deleteAll(City.class);
        titleText = view.findViewById(R.id.title_text);
        backButton = view.findViewById(R.id.back_button);
        listView = view.findViewById(R.id.listview);
        adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentLevel == LEVEL_PROVINCE){
                    selectedProvinec = provinces.get(position);
                    Log.v("TAG","click;...."+selectedProvinec.getProvinceName()+"position:"+position);
                    queryCities();
                }else if(currentLevel == LEVEL_CITY){
                    selectedCity = cities.get(position);
                    //TODO
//                    queryCounties();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentLevel == LEVEL_CONTY){
                    queryCities();
                }else if(currentLevel == LEVEL_CITY){
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }

    private void queryProvinces(){
        titleText.setText("China");
        backButton.setVisibility(View.GONE);
        provinces = LitePal.findAll(Province.class);
        if(provinces.size() > 0){
            dataList.clear();
            for(Province province: provinces){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();;
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        }else{
            queryFromServer("http://guolin.tech/api/china", "province");
        }
    }

    private void queryCities(){
        titleText.setText(selectedProvinec.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cities = LitePal.where("provinceid=?",String.valueOf(selectedProvinec.getId())).find(City.class);
        if(cities.size()>0){
            dataList.clear();
            for(City city: cities){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        }else{
            int provinceCode = selectedProvinec.getProvinceCode();
            Log.v("TAGxx", "code....."+selectedProvinec.getId());
            queryFromServer("http://guolin.tech/api/china/"+(provinceCode), "city");
        }
    }

    /**
     * query data by http request.
     */
    private void queryFromServer(String address, final String type){
        showProgessDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(), "failllll", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                boolean result = false;
                if("province".equals(type)){
                    result = Utility.handleProvinceResponse(res);
                }else if("city".equals(type)){
                    result = Utility.handleCityResponse(res, selectedProvinec.getId());
                }else if("county".equals(type)){
                    result = Utility.handleCountryResponse(res, selectedCity.getId());
                }
                if(result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if("province".equals(type)){
                                queryProvinces();
                            }else if("city".equals(type)){
                                queryCities();;
                            }
                        }
                    });
                }
            }
        });
    }

    private void showProgessDialog(){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载。。。");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

}
