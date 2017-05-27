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

    public static final String ipaddress = "http://192.168.0.4:8080";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.exit_menu){
            SharedPreferences sharedpreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    public void generateOTP(View view) {
        Intent intent = new Intent(this, GenerateOTPActivity.class);
        startActivity(intent);
    }

    public void registerOTP(View view) {
        Intent intent = new Intent(this, RegisterOTPActivity.class);
        startActivity(intent);
    }

    public void goHome(View view){
        Intent intent = new Intent(this, PostLoginActivity.class);
        startActivity(intent);
    }
}
