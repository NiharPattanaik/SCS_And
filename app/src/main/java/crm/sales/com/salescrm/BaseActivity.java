package crm.sales.com.salescrm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by npattana on 21/04/17.
 */

public class BaseActivity extends AppCompatActivity {

    public static final String ipaddress = "http://192.168.0.7:8080";

    public void generateOTP(View view) {
        Intent intent = new Intent(this, GenerateOTPActivity.class);
        startActivity(intent);
    }

    public void registerOTP(View view) {
        Intent intent = new Intent(this, RegisterOTPActivity.class);
        startActivity(intent);
    }

    public void logOut(View view){
        SharedPreferences sharedpreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
