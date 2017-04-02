package crm.sales.com.salescrm;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by npattana on 31/03/17.
 */

public class GenerateOTPActivity extends AppCompatActivity {

    List<Customer> customers = new ArrayList<Customer>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_otp);
        new HttpRequestTask().execute();
        populateOTPTypeDropdown();
    }

    /*@Override
    protected void onStart() {
        super.onStart();
        new HttpRequestTask().execute();
    }*/

    private void populateCustomerDropdown(){
        Spinner spinner = (Spinner) findViewById(R.id.customers);
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, customers.toArray(new Customer[customers.size()]));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }


    private void populateOTPTypeDropdown(){
        Spinner spinner = (Spinner) findViewById(R.id.otp_types);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.otp_types_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

    }

    public void generateOTP(View view) {
        Intent intent = new Intent(this, OTPConfirmationActivity.class);
        startActivity(intent);
    }


    private class HttpRequestTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                final String url = "http://192.168.0.4:8080/crm/customer/trimmedCustomers/13";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                String response = restTemplate.getForObject(url, String.class);
                return response;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return "";
        }

        @Override
        protected void onPostExecute(String response) {
            try {
                JSONArray array = new JSONArray(response);
                for(int i=0; i<array.length(); i++){
                    JSONObject object = new JSONObject(String.valueOf(array.get(i)));
                    Customer customer = new Customer();
                    customer.setCustomerID(object.getLong("customerID"));
                    customer.setCustomerName(object.getString("customerName"));
                    customers.add(customer);
                }
                populateCustomerDropdown();
            }catch(Exception e){
                Log.e("MainActivity", e.getMessage(), e);
            }

        }

    }
}
