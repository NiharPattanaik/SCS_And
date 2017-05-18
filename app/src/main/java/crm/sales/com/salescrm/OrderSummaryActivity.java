package crm.sales.com.salescrm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

/**
 * Created by npattana on 17/05/17.
 */

public class OrderSummaryActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        int noOfLineItems = getIntent().getIntExtra("noOfLineItems", 0);
        double orderValue = getIntent().getDoubleExtra("orderValue", 0.0);
        String remarks = getIntent().getStringExtra("remarks");

        ((TextView)findViewById(R.id.order_summ_lineitems)).setText(String.valueOf(noOfLineItems));
        ((TextView)findViewById(R.id.order_summ_orderval)).setText(String.valueOf(orderValue));
        ((TextView)findViewById(R.id.order_summ_remarks)).setText(remarks);

    }

}
