package crm.sales.com.salescrm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.List;

/**
 * Created by npattana on 21/04/17.
 */

public class RegisterOTPActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_registration);
        populateOTPTypeDropdown();
        populateEmptyCustomerDropDown();
    }

    private void populateEmptyCustomerDropDown(){
        List<Customer> customers = new ArrayList<Customer>();
        Customer cust = new Customer();
        cust.setCustomerID(-1);
        cust.setCustomerName("-- Select a customer --");
        customers.add(0, cust);
        populateCustomerDropdown(customers);
    }


    public void validateAndregisterOTP(View view) {
        Spinner customerDropDn = (Spinner) findViewById(R.id.register_OTP_customers);
        Spinner otpTypeDropDn = (Spinner)findViewById(R.id.register_OTP_otp_types);
        EditText otpField = (EditText)findViewById(R.id.register_otp);
        TextView errorView = (TextView) findViewById(R.id.register_OTP_error);
        //Show error if dropdown is not selected
        if(customerDropDn.getSelectedItemPosition() == 0){
            errorView.setText("Please select Customer.");
        } else if(otpTypeDropDn.getSelectedItemPosition() == 0){
            errorView.setText("Please select OTP Type.");
        } else if(otpField.getText().toString().trim().isEmpty()){
            errorView.setText("Please enter OTP.");
        }else {
            Customer customer = (Customer) customerDropDn.getSelectedItem();
            if (customer != null && otpTypeDropDn.getSelectedItemPosition() != 0 && !otpField.getText().toString().trim().isEmpty()) {
                int customerID = customer.getCustomerID();
                int otpType = otpTypeDropDn.getSelectedItemPosition();
                String otp = otpField.getText().toString();
                String customerName = customer.getCustomerName();
                int orderBookingID = customer.getOrderBookingID();
                new registerOTPTask().execute(customerID, otpType, otp, customerName, orderBookingID);
            }
        }
      }

    private class registerOTPTask extends AsyncTask<Object, Integer, String> {
        SharedPreferences sharedpreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        int customerID = -1;
        int otpType = -1;
        String otp = "";
        String customerName = "";
        int orderBookingID = -1;
        @Override
        protected String doInBackground(Object... params) {
            String result = "";
            try {
                customerID = Integer.valueOf(String.valueOf(params[0]));
                otpType = Integer.valueOf(String.valueOf(params[1]));
                otp = String.valueOf(params[2]);
                customerName = String.valueOf(params[3]);
                orderBookingID = Integer.valueOf(String.valueOf(params[4]));
                String userName = sharedpreferences.getString("userName", "");
                String password = sharedpreferences.getString("password", "");
                String plainCreds = userName + ":" + password;
                String base64encoded = Base64.encodeToString(plainCreds.getBytes(), Base64.DEFAULT);
                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "Basic " + base64encoded);
                final String url = BaseActivity.ipaddress+"/crm/rest/otpReST/verify/" + customerID + "/" + otpType + "/" + otp;
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
            TextView errView = (TextView) findViewById(R.id.register_OTP_error);
            errView.setText("Oops! something is not right. Please retry after sometime and if error persists contact System Administrator");
        }

        @Override
        protected void onPostExecute(String result) {
            TextView view = (TextView) findViewById(R.id.register_OTP_error);
            try {
                JSONObject resultJson = new JSONObject(result);
                if(resultJson.getInt("status") == 1){
                    Intent intent = new Intent(RegisterOTPActivity.this, CreateOrderActivity.class);
                    intent.putExtra("orderBookingID", orderBookingID);
                    intent.putExtra("customerID", customerID);
                    startActivity(intent);
                }else{
                    view.setText(resultJson.getString("errorMsg"));
                }

            }catch(Exception e){
                Log.e("MainActivity", e.getMessage(), e);
                TextView errView = (TextView) findViewById(R.id.register_OTP_error);
                errView.setText("Oops! something is not right. Please retry after sometime and if error persists contact System Administrator");

            }
        }
    }

    private void populateCustomerDropdown(List<Customer> customers){
        Spinner spinner = (Spinner) findViewById(R.id.register_OTP_customers);
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, customers.toArray(new Customer[customers.size()]));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }


    private void populateOTPTypeDropdown(){
        Spinner spinner = (Spinner) findViewById(R.id.register_OTP_otp_types);
        // Create an ArrayAdapter using the string array and a default spinner layout
        SharedPreferences sharedpreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        String commaSeparatedRoleIDs = sharedpreferences.getString("roleIDs", "");
        ArrayList<String> otpTypeList = new ArrayList<String>();
        otpTypeList.add("-- Select OTP Type --");
        for(String roleID : commaSeparatedRoleIDs.split(",")){
            if(roleID.equals("2")){
                otpTypeList.add("Order Booking");
            }else if(roleID.equals("3")){
                otpTypeList.add("Delivery Confirmation");
            }else if(roleID.equals("4")){
                otpTypeList.add("Payment Confirmation");
            }
        }
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        //        R.array.otp_types_array, android.R.layout.simple_spinner_item);
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, otpTypeList.toArray(new String[otpTypeList.size()]));
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //on selection
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String otpType = (String)parent.getItemAtPosition(position);
                switch(otpType){
                    case "Order Booking":
                        new RegisterOTPActivity.FetchCustomersTask().execute(1);
                        break;
                    case "Delivery Confirmation":
                        new RegisterOTPActivity.FetchCustomersTask().execute(2);
                        break;
                    case "Payment Confirmation":
                        new RegisterOTPActivity.FetchCustomersTask().execute(3);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private class FetchCustomersTask extends AsyncTask<Integer, Integer, String> {
        SharedPreferences sharedpreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        @Override
        protected String doInBackground(Integer... params) {
            String result = "";
            try {
                int otpType = params[0];
                int userID = sharedpreferences.getInt("userID", -1);
                String userName = sharedpreferences.getString("userName", "");
                String password = sharedpreferences.getString("password", "");
                String plainCreds = userName+":"+password;
                String base64encoded = Base64.encodeToString(plainCreds.getBytes(), Base64.DEFAULT);
                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "Basic " + base64encoded);
                final String url = BaseActivity.ipaddress+"/crm/rest/customer/customersForOTPVerification/"+userID +"/"+otpType;
                HttpEntity<String> request = new HttpEntity<String>(headers);
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                result = response.getBody();
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
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
        protected void onPostExecute(String response) {
            TextView view = (TextView) findViewById(R.id.register_OTP_error);
            try {
                JSONObject userJson = new JSONObject(response);
                List<Customer> customers = new ArrayList<Customer>();
                if(userJson.getInt("status") == 1){
                    JSONArray custArray = userJson.getJSONArray("businessEntities");
                    if(custArray != null && custArray.length() > 0){
                        for(int i=0; i<custArray.length(); i++) {
                            JSONObject custObject = new JSONObject(custArray.get(i).toString());
                            Customer customer = new Customer();
                            customer.setCustomerID(custObject.getInt("customerID"));
                            customer.setCustomerName(custObject.getString("customerName"));
                            customer.setOrderBookingID(custObject.getInt("orderBookingID"));
                            customers.add(customer);
                        }
                    }
                    Customer cust = new Customer();
                    cust.setCustomerID(-1);
                    cust.setCustomerName("-- Select a customer --");
                    customers.add(0, cust);
                    populateCustomerDropdown(customers);
                    //If no customers retirned show error message
                    if(customers.size() == 1){
                        view.setText("No customer visit is scheduled for today.");
                    }
                }else{
                    view.setText(userJson.getString("errorMsg"));
                }
            }catch(Exception e){
                Log.e("MainActivity", e.getMessage(), e);
                TextView errView = (TextView) findViewById(R.id.register_OTP_error);
                errView.setText("Oops! something is not right. Please retry after sometime and if error persists contact System Administrator");
            }

        }

    }
}
