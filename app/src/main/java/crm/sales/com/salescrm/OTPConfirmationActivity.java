package crm.sales.com.salescrm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by npattana on 31/03/17.
 */

public class OTPConfirmationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_confirmation);
        String customerName= getIntent().getStringExtra("customer_name");
        TextView textView = (TextView)findViewById(R.id.otp_conf);
        textView.setText("OTP for customer "+ customerName +" has been successfully generated and sent.");
    }
}
