package crm.sales.com.salescrm;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

//import org.springframework.http.converter.StringHttpMessageConverter;
//import org.springframework.web.client.RestTemplate;

public class MainActivity extends AppCompatActivity {

    Button generateOTP;
    Button registerOTP;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        generateOTP = (Button) findViewById(R.id.generate_otp);
        registerOTP = (Button) findViewById(R.id.register_otp);
    }


    public void generateOTP(View view) {
        System.out.println("Genertae OTP is clicked");

        Intent intent = new Intent(this, GenerateOTPActivity.class);
        startActivity(intent);
    }

    public void registerOTP(View view) {
        System.out.println("Register OTP is clicked");
        Log.i(TAG, "Register OTP is clicked");
        new HttpRequestTask().execute();
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                final String url = "http://72.163.209.189:8080/crm/customer/trimmedCustomers/13";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                String greeting = restTemplate.getForObject(url, String.class);
                return greeting;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String greeting) {
            System.out.println(greeting);
        }

    }


}