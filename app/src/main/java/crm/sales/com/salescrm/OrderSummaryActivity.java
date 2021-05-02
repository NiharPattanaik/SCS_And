package crm.sales.com.salescrm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import crm.sales.com.salescrm.model.Order;

/**
 * Created by npattana on 17/05/17.
 */

public class OrderSummaryActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        //int orderBookingID = getIntent().getIntExtra("orderBookingID", 0);
        //int customerID = getIntent().getIntExtra("customerID", 0);
        int noOfLineItems = getIntent().getIntExtra("noOfLineItems", 0);
        double orderValue = getIntent().getDoubleExtra("orderValue", 0.0);
        String remarks = getIntent().getStringExtra("remarks");

        ((TextView)findViewById(R.id.order_summ_lineitems)).setText(String.valueOf(noOfLineItems));
        ((TextView)findViewById(R.id.order_summ_orderval)).setText(String.valueOf(orderValue));
        ((TextView)findViewById(R.id.order_summ_remarks)).setText(remarks);

        //SharedPreferences sharedpreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedpreferences.edit();
        //editor.putInt("orderBookingID", orderBookingID);
        //editor.putInt("customerID", customerID);
        //editor.commit();
    }

    public void confirmOrder(View view){
        verifyOTP();
    }

    public void submitOrder(){
        //SharedPreferences sharedpreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        int noOfLineItems = Integer.parseInt(((TextView)findViewById(R.id.order_summ_lineitems)).getText().toString());
        double orderValue = Double.parseDouble(((TextView)findViewById(R.id.order_summ_orderval)).getText().toString());
        String remarks = ((TextView)findViewById(R.id.order_summ_remarks)).getText().toString();
        //int orderBookingID = sharedpreferences.getInt("orderBookingID", -1);
        //int customerID = sharedpreferences.getInt("customerID", -1);
        int orderBookingID = getIntent().getIntExtra("orderBookingID", 0);
        int customerID = getIntent().getIntExtra("customerID", 0);
        EditText otpField = (EditText)findViewById(R.id.otpTextBox);
        String otp = otpField.getText().toString();
        new CreateOrderTask().execute(orderBookingID, noOfLineItems, orderValue, remarks, customerID, otp);

    }

    public void editOrder(View view){
        //SharedPreferences sharedpreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        int noOfLineItems = Integer.parseInt(((TextView)findViewById(R.id.order_summ_lineitems)).getText().toString());
        double orderValue = Double.parseDouble(((TextView)findViewById(R.id.order_summ_orderval)).getText().toString());
        String remarks = ((TextView)findViewById(R.id.order_summ_remarks)).getText().toString();
        //int orderBookingID = sharedpreferences.getInt("orderBookingID", -1);
        //int customerID = sharedpreferences.getInt("customerID", -1);
        int orderBookingID = getIntent().getIntExtra("orderBookingID", 0);
        int customerID = getIntent().getIntExtra("customerID", 0);
        Intent intent = new Intent(OrderSummaryActivity.this, EditOrderActivity.class);
        intent.putExtra("noOfLineItems", noOfLineItems);
        intent.putExtra("orderValue", orderValue);
        intent.putExtra("remarks", remarks);
        intent.putExtra("orderBookingID", orderBookingID);
        intent.putExtra("customerID", customerID);
        startActivity(intent);
    }

    private class CreateOrderTask extends AsyncTask<Object, Integer, String> {
        SharedPreferences sharedpreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);

        @Override
        protected String doInBackground(Object... params) {
            String result = "";
            try {
                int orderBookingID = Integer.valueOf(String.valueOf(params[0]));
                int noOfLineItems = Integer.valueOf(String.valueOf(params[1]));
                double orderValue = Double.valueOf(String.valueOf(params[2]));
                String remarks = String.valueOf(params[3]);
                int customerID = Integer.valueOf(String.valueOf(params[4]));
                String otp = String.valueOf(params[5]);
                String userName = sharedpreferences.getString("userName", "");
                String password = sharedpreferences.getString("password", "");
                String plainCreds = userName + ":" + password;
                String base64encoded = Base64.encodeToString(plainCreds.getBytes(), Base64.DEFAULT);
                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "Basic " + base64encoded);
                final String url = BaseActivity.ipaddress+"/crm/rest/orderReST/createWithOTP/"+otp;
                //HttpEntity<String> request = new HttpEntity<String>(headers);
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                //restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                Order order = new Order(orderBookingID, noOfLineItems, orderValue, remarks, customerID);
                HttpEntity<Order> httpEntity = new HttpEntity<Order>(order, headers);
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
                result = response.getBody();
            }catch(Exception exception){
                Log.e("OrderSummaryActivity", exception.getMessage(), exception);
                publishProgress(0);
            }
            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            TextView errView = (TextView) findViewById(R.id.register_OTP_error);
            errView.setText("Oops! something is not right. Please retry after sometime and if error persists contact System Administrator");
        }

        @Override
        protected void onPostExecute(String result) {
            TextView view = (TextView) findViewById(R.id.order_summ_error);
            try {
                JSONObject resultJson = new JSONObject(result);
                if(resultJson.getInt("status") == 1){
                    int orderID = resultJson.getInt("businessEntityID");
                    Intent intent = new Intent(OrderSummaryActivity.this, OrderCreationConfirmationActivity.class);
                    intent.putExtra("orderID", orderID);
                    startActivity(intent);
                }else{
                    view.setText("Order could not be created successfully. Please retry again, if error persists contat system administrator.");
                }

            }catch(Exception e){
                Log.e("OrderSummaryActivity", e.getMessage(), e);
                view.setText("Order could not be created successfully. Please retry again, if error persists contat system administrator.");
            }
        }
    }


    public void verifyOTP() {
        EditText otpField = (EditText)findViewById(R.id.otpTextBox);
        TextView errorView = (TextView) findViewById(R.id.order_summ_error);
        //SharedPreferences sharedpreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        //int customerID = sharedpreferences.getInt("customerID", -1);
        int customerID = getIntent().getIntExtra("customerID", 0);
        if(otpField.getText().toString().trim().isEmpty()){
            errorView.setText("Please enter OTP.");
        }else if(customerID != 0) {
            String otp = otpField.getText().toString();
            new RegisterOTPTask().execute(customerID, 1, otp);
        }
    }


    private class RegisterOTPTask extends AsyncTask<Object, Integer, String> {
        SharedPreferences sharedpreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        int customerID = -1;
        int otpType = -1;
        String otp = "";
        @Override
        protected String doInBackground(Object... params) {
            String result = "";
            try {
                customerID = Integer.valueOf(String.valueOf(params[0]));
                otpType = Integer.valueOf(String.valueOf(params[1]));
                otp = String.valueOf(params[2]);
                String userName = sharedpreferences.getString("userName", "");
                String password = sharedpreferences.getString("password", "");
                int tenantID = sharedpreferences.getInt("tenantID", 0);
                String plainCreds = userName + ":" + password;
                String base64encoded = Base64.encodeToString(plainCreds.getBytes(), Base64.DEFAULT);
                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "Basic " + base64encoded);
                final String url = BaseActivity.ipaddress+"/crm/rest/otpReST/verify/" + customerID + "/" + otpType + "/" + otp + "/"+ tenantID;
                HttpEntity<String> request = new HttpEntity<String>(headers);
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
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
            TextView errView = (TextView) findViewById(R.id.order_summ_error);
            errView.setText("Oops! something is not right. Please retry after sometime and if error persists contact System Administrator");
        }

        @Override
        protected void onPostExecute(String result) {
            TextView view = (TextView) findViewById(R.id.order_summ_error);
            try {
                JSONObject resultJson = new JSONObject(result);
                if(resultJson.getInt("status") == 1){
                    submitOrder();
                }else{
                    view.setText(resultJson.getString("errorMsg"));
                }

            }catch(Exception e){
                Log.e("MainActivity", e.getMessage(), e);
                TextView errView = (TextView) findViewById(R.id.order_summ_error);
                errView.setText("Oops! something is not right. Please retry after sometime and if error persists contact System Administrator");

            }
        }
    }
}
