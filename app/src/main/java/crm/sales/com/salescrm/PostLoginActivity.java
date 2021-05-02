package crm.sales.com.salescrm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

/**
 * Created by npattana on 18/04/17.
 */

public class PostLoginActivity extends BaseActivity {

    private static final String TAG = PostLoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_login);

        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        RelativeLayout ordersLyout = (RelativeLayout) findViewById(R.id.Orders);
        ordersLyout.setVisibility(View.GONE);

        RelativeLayout deliveryLayout = (RelativeLayout) findViewById(R.id.Delivery);
        deliveryLayout.setVisibility(View.GONE);

        RelativeLayout paymentLayout = (RelativeLayout) findViewById(R.id.Payment);
        paymentLayout.setVisibility(View.GONE);


        SharedPreferences sharedpreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        String commaSeparatedRoleIDs = sharedpreferences.getString("roleIDs", "");
        ArrayList<Integer> otpTypeList = new ArrayList<Integer>();
        TabHost.TabSpec spec;
        for(String roleID : commaSeparatedRoleIDs.split(",")){
            if(roleID.equals("2")){
                //Tab 1
                spec = host.newTabSpec("Tab One");
                spec.setContent(R.id.Orders);
                spec.setIndicator("Orders");
                host.addTab(spec);
                ordersLyout.setVisibility(View.VISIBLE);
            }else if(roleID.equals("3")){
                //Tab 2
                spec = host.newTabSpec("Tab Two");
                spec.setContent(R.id.Delivery);
                spec.setIndicator("Deliveries");
                host.addTab(spec);
                deliveryLayout.setVisibility(View.VISIBLE);
            }else if(roleID.equals("4")){
                //Tab 3
                spec = host.newTabSpec("Tab Three");
                spec.setContent(R.id.Payment);
                spec.setIndicator("Payments");
                host.addTab(spec);
                paymentLayout.setVisibility(View.VISIBLE);
            }
        }

        new fetchOrderBookingStats().execute();
    }


    private class fetchOrderBookingStats extends AsyncTask<Void, Integer, String> {
        SharedPreferences sharedpreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        int userID = sharedpreferences.getInt("userID", -1);
        String result = "";
        @Override
        protected String doInBackground(Void ... objects) {
            try {
                String userName = sharedpreferences.getString("userName", "");
                String password = sharedpreferences.getString("password", "");
                int tenantID = sharedpreferences.getInt("tenantID", 0);
                String plainCreds = userName + ":" + password;
                String base64encoded = Base64.encodeToString(plainCreds.getBytes(), Base64.DEFAULT);
                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "Basic " + base64encoded);
                final String url = BaseActivity.ipaddress+"/crm/rest/orderReST/orderBookingStatsToday/" + userID +"/"+ tenantID;
                System.out.println(url);
                HttpEntity<String> request = new HttpEntity<String>(headers);
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                result = response.getBody();
            }catch(Exception exception){
                Log.e("MainActivity", exception.getMessage(), exception);
                publishProgress(0);
            }
            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            TextView errView = (TextView) findViewById(R.id.generate_OTP_error_msg);
            errView.setText("Oops! something is not right. Please retry after sometime and if error persists contact System Administrator");
        }

        @Override
        protected void onPostExecute(String result) {
            TextView view = (TextView) findViewById(R.id.order_booking_stats_error);
            try {
                JSONObject resultJson = new JSONObject(result);
                if(resultJson.getInt("status") == 1){
                    JSONArray custArray = resultJson.getJSONArray("businessEntities");
                    if(custArray != null && custArray.length() > 0){
                            JSONObject custObject = new JSONObject(custArray.get(0).toString());
                            //all customer visits
                            TextView allOrdersBooked = (TextView) findViewById(R.id.order_booking_stats_all_visits);
                            allOrdersBooked.setText(String.valueOf(custObject.getInt("totalNoOfVisits")));
                            //Completed Visits
                            TextView completedVisits = (TextView)findViewById(R.id.order_booking_stats_visits_completed);
                            completedVisits.setText(String.valueOf(custObject.getInt("noOfVisitsCompleted")));
                            //Pending Visits
                            TextView pendingVisits = (TextView)findViewById(R.id.order_booking_stats_visits_pending);
                            pendingVisits.setText(String.valueOf(custObject.getInt("noOfVisitsPending")));
                    }
                }else{
                    view.setText(resultJson.getString("errorMsg"));
                }

            }catch(Exception e){
                Log.e("MainActivity", e.getMessage(), e);
                TextView errView = (TextView) findViewById(R.id.order_booking_stats_error);
                errView.setText("Oops! something is not right. Please retry after sometime and if error persists contact System Administrator");
            }
        }
    }

}
