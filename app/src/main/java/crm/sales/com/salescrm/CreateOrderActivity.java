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
 * Created by npattana on 17/05/17.
 */

public class CreateOrderActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);
        new FetchCustomersTask().execute(1);
    }

    private void populateCustomerDropdown(List<Customer> customers){
        Spinner spinner = (Spinner) findViewById(R.id.customer_dropdn);
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, customers.toArray(new Customer[customers.size()]));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

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
                int tenantID = sharedpreferences.getInt("tenantID", 0);
                String plainCreds = userName+":"+password;
                String base64encoded = Base64.encodeToString(plainCreds.getBytes(), Base64.DEFAULT);
                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "Basic " + base64encoded);
                final String url = BaseActivity.ipaddress+"/crm/rest/customer/customersForOTPVerification/"+userID +"/"+otpType + "/"+ tenantID;
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
            TextView errView = (TextView) findViewById(R.id.create_order_error);
            errView.setText("Oops! something is not right. Please retry after sometime and if error persists contact System Administrator");
        }

        @Override
        protected void onPostExecute(String response) {
            TextView view = (TextView) findViewById(R.id.create_order_error);
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
                TextView errView = (TextView) findViewById(R.id.create_order_error);
                errView.setText("Oops! something is not right. Please retry after sometime and if error persists contact System Administrator");
            }

        }

    }


    public void reviewOrder(View view){
        TextView errorView = (TextView) findViewById(R.id.create_order_error);
        EditText noOfLineItems = (EditText) findViewById(R.id.create_order_nolines);
        EditText orderValue = (EditText) findViewById(R.id.create_order_val);
        EditText remarks = (EditText)findViewById(R.id.create_order_remark);
        Spinner customerDropDn = (Spinner) findViewById(R.id.customer_dropdn);
        Customer customer = (Customer) customerDropDn.getSelectedItem();
        if(customerDropDn.getSelectedItemPosition() == 0){
            errorView.setText("Please select Customer.");
        }else if(noOfLineItems.getText() == null || noOfLineItems.getText().toString().trim().isEmpty()){
            errorView.setText("Number Of Line Items is required.");
        }else if(orderValue.getText() == null || orderValue.getText().toString().trim().isEmpty()){
            errorView.setText("Aproximate order value is required.");
        }else if(remarks.getText() == null || remarks.getText().toString().trim().isEmpty()){
            errorView.setText("Remarks is required.");
        }else {
            Intent intent = new Intent(CreateOrderActivity.this, OrderSummaryActivity.class);
            intent.putExtra("noOfLineItems", Integer.valueOf(noOfLineItems.getText().toString()));
            intent.putExtra("orderValue", Double.valueOf(orderValue.getText().toString()));
            intent.putExtra("remarks", remarks.getText().toString());
            intent.putExtra("orderBookingID", customer.getOrderBookingID());
            intent.putExtra("customerID", customer.getCustomerID());
            startActivity(intent);
        }

    }
}
