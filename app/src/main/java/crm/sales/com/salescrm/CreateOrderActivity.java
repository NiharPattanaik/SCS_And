package crm.sales.com.salescrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by npattana on 17/05/17.
 */

public class CreateOrderActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);
    }

    public void reviewOrder(View view){
        TextView errorView = (TextView) findViewById(R.id.create_order_error);
        EditText noOfLineItems = (EditText) findViewById(R.id.create_order_nolines);
        EditText orderValue = (EditText) findViewById(R.id.create_order_val);
        EditText remarks = (EditText)findViewById(R.id.create_order_remark);
        if(noOfLineItems.getText() == null || noOfLineItems.getText().toString().trim().isEmpty()){
            errorView.setText("Number Of Line Items is required.");
        }else if(orderValue.getText() == null || orderValue.getText().toString().trim().isEmpty()){
            errorView.setText("Aproximate order value is required.");
        }else if(remarks.getText() == null || remarks.getText().toString().trim().isEmpty()){
            errorView.setText("Remarks is required.");
        }else {
            int orderBookingID = getIntent().getIntExtra("orderBookingID", 0);
            Intent intent = new Intent(CreateOrderActivity.this, OrderSummaryActivity.class);
            intent.putExtra("noOfLineItems", Integer.valueOf(noOfLineItems.getText().toString()));
            intent.putExtra("orderValue", Double.valueOf(orderValue.getText().toString()));
            intent.putExtra("remarks", remarks.getText().toString());
            intent.putExtra("orderBookingID", orderBookingID);
            startActivity(intent);
        }

    }
}
