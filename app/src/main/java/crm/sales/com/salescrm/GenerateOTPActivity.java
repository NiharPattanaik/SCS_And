package crm.sales.com.salescrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by npattana on 31/03/17.
 */

public class GenerateOTPActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_otp);
        populateCustomerDropdown();
        populateOTPTypeDropdown();
    }

    private void populateCustomerDropdown(){
        Spinner spinner = (Spinner) findViewById(R.id.customers);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.customer_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
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
}
