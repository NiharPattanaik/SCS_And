package crm.sales.com.salescrm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity{

    Button generateOTP;
    Button registerOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        generateOTP = (Button)findViewById(R.id.generate_otp);
        registerOTP = (Button)findViewById(R.id.register_otp);
    }


    public void generateOTP(View view) {
        System.out.println("Genertae OTP is clicked");

        Intent intent = new Intent(this, GenerateOTPActivity.class);
        startActivity(intent);
    }

    public void registerOTP(View view) {
        System.out.println("Register OTP is clicked");
    }
}
