package crm.sales.com.salescrm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.sax.TextElementListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

//import org.springframework.http.converter.StringHttpMessageConverter;
//import org.springframework.web.client.RestTemplate;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View view) {
        EditText user = (EditText) findViewById(R.id.main_user);
        EditText pass = (EditText) findViewById(R.id.main_pass);
        Log.i(TAG, "User Name "+ user.getText().toString());
        Log.i(TAG, "Password "+ pass.getText().toString());
        String userName = user.getText().toString();
        String password = pass.getText().toString();

        if(userName.trim().isEmpty() || password.trim().isEmpty() ){
            TextView textView = (TextView) findViewById(R.id.main_error_msg);
            textView.setText("User Name or Password is empty.");
        }else {
            new HttpRequestTask().execute(userName, password);
        }

        SharedPreferences sharedpreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        System.out.println("##########################################################");
        System.out.println(sharedpreferences.getString("userName", ""));
        System.out.println(sharedpreferences.getString("password", ""));
        System.out.println(sharedpreferences.getInt("userID", -1));
        System.out.println(sharedpreferences.getInt("resellerID", -1));
        System.out.println(sharedpreferences.getString("roleIDs", ""));
        System.out.println("##########################################################");


    }

    private class HttpRequestTask extends AsyncTask<String, Void, String> {

        String userName;
        String password;
        @Override
        protected String doInBackground(String... params) {
            String response = "";
            try {
                userName = params[0];
                password = params[1];
                final String url = "http://192.168.0.4:8080/crm/rest/userReST/validateUser/"+userName+"/"+password;
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                response =  restTemplate.getForObject(url, String.class);
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            int userID = -1;
            int resellerID = -1;
            String commaSeparatedRoleIDs = "";
            try {
                JSONObject jsonObject = new JSONObject(result);
                System.out.println(jsonObject.get("status"));
                System.out.println(jsonObject.get("errorMsg"));
                System.out.println(jsonObject.get("errorCode"));

                if(jsonObject.getInt("status") == 0){
                    TextView view = (TextView) findViewById(R.id.main_error_msg);
                    view.setText(jsonObject.getString("errorMsg"));
                }else{
                    JSONObject userJson = new JSONObject(result);
                    if(userJson.getInt("status") == 1){
                        JSONArray userArray = userJson.getJSONArray("businessEntities");
                        if(userArray != null && userArray.length() > 0){
                            JSONObject userObject = new JSONObject(userArray.get(0).toString());
                            //Retrive User ID
                            userID = userObject.getInt("userID");
                            //Retrieve Reseller ID
                            resellerID = userObject.getInt("resellerID");
                            JSONArray rolesArray = userObject.getJSONArray("roles");
                            if(rolesArray != null){
                                String[] roleIDArray = new String[rolesArray.length()];
                                for(int i=0; i<rolesArray.length(); i++){
                                    JSONObject role = new JSONObject(rolesArray.get(i).toString());
                                    //Retrieve roleIds
                                    roleIDArray[i] = String.valueOf(role.getInt("roleID"));
                                }
                                commaSeparatedRoleIDs = StringUtils.arrayToCommaDelimitedString(roleIDArray);
                            }

                        }
                    }

                    //Set in preferece
                    SharedPreferences sharedpreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("userName", userName);
                    editor.putString("password", password);
                    editor.putInt("userID", userID);
                    editor.putInt("resellerID", resellerID);
                    editor.putString("roleIDs", commaSeparatedRoleIDs);
                    editor.commit();

                    Intent intent = new Intent(MainActivity.this, PostLoginActivity.class);
                    startActivity(intent);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}