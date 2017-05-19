package crm.sales.com.salescrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by npattana on 18/05/17.
 */

public class EditOrderActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_edit);

        int orderBookingID = getIntent().getIntExtra("orderBookingID", 0);
        int noOfLineItems = getIntent().getIntExtra("noOfLineItems", 0);
        double orderValue = getIntent().getDoubleExtra("orderValue", 0.0);
        String remarks = getIntent().getStringExtra("remarks");

        ((TextView)findViewById(R.id.edit_order_nolines)).setText(String.valueOf(noOfLineItems));
        ((TextView)findViewById(R.id.edit_order_val)).setText(String.valueOf(orderValue));
        ((TextView)findViewById(R.id.edit_order_remark)).setText(remarks);
    }

    public void reviewOrder(View view){
        EditText noOfLineItems = (EditText) findViewById(R.id.edit_order_nolines);
        EditText orderValue = (EditText) findViewById(R.id.edit_order_val);
        EditText remarks = (EditText)findViewById(R.id.edit_order_remark);
        int orderBookingID = getIntent().getIntExtra("orderBookingID", 0);
        Intent intent = new Intent(EditOrderActivity.this, OrderSummaryActivity.class);
        intent.putExtra("noOfLineItems", Integer.valueOf(noOfLineItems.getText().toString()));
        intent.putExtra("orderValue", Double.valueOf(orderValue.getText().toString()));
        intent.putExtra("remarks", remarks.getText().toString());
        intent.putExtra("orderBookingID", orderBookingID);
        startActivity(intent);

    }
}
