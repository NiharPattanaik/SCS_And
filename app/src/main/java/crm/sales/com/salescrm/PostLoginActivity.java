package crm.sales.com.salescrm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by npattana on 18/04/17.
 */

public class PostLoginActivity extends AppCompatActivity {
    Button generateOTP;
    Button registerOTP;

    private static final String TAG = PostLoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_login);
        generateOTP = (Button) findViewById(R.id.post_login_generate_otp);
        registerOTP = (Button) findViewById(R.id.post_login_register_otp);
    }

    public void generateOTP(View view) {
        System.out.println("Genertae OTP is clicked");

        Intent intent = new Intent(this, GenerateOTPActivity.class);
        startActivity(intent);
    }

    public void registerOTP(View view) {
        System.out.println("Register OTP is clicked");
        Log.i(TAG, "Register OTP is clicked");
        Intent intent = new Intent(this, RegisterOTPActivity.class);
        startActivity(intent);
    }


}
