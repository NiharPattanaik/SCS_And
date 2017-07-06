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

    public static final String ipaddress = "http://35.189.180.185:8080";

    public static final String LOGIN_STATUS = "loginStatus";

    @Override
    public void onBackPressed() {
        SharedPreferences sharedpreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        if(!sharedpreferences.getBoolean(LOGIN_STATUS, false)){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else{
            super.onBackPressed();
        }
    }

    public void logOut(View view){
        SharedPreferences sharedpreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.putBoolean(LOGIN_STATUS, false);
        editor.commit();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void generateOTP(View view) {
        SharedPreferences sharedpreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        if(!sharedpreferences.getBoolean(LOGIN_STATUS, false)){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(this, GenerateOTPActivity.class);
            startActivity(intent);
        }
    }

    public void createOrder(View view) {
        SharedPreferences sharedpreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        if(!sharedpreferences.getBoolean(LOGIN_STATUS, false)){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(this, CreateOrderActivity.class);
            startActivity(intent);
        }
    }

    public void goHome(View view){
        SharedPreferences sharedpreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        if(!sharedpreferences.getBoolean(LOGIN_STATUS, false)){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(this, PostLoginActivity.class);
            startActivity(intent);
        }
    }
}
