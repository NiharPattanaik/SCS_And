package crm.sales.com.salescrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

/**
 * Created by npattana on 31/03/17.
 */

public class OrderCreationConfirmationActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_creation_confirmation);
        int orderID= getIntent().getIntExtra("orderID", -1);
        TextView textView = (TextView)findViewById(R.id.create_order_conf);
        textView.setText("Order "+ orderID+ " has been successfully created.");
    }

    public void navigateToHome(View view) {
        Intent intent = new Intent(this, PostLoginActivity.class);
        startActivity(intent);
    }
}
