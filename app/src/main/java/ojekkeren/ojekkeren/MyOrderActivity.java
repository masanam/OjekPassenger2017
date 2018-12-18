package ojekkeren.ojekkeren;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MyOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        final DBAccount dbAccount = new DBAccount(this);

        if(dbAccount.getCurrentMemberDetails().getIsLogged() != null) {
            MemberAkun akunLoggedCheck = dbAccount.getCurrentMemberDetails();

            if (akunLoggedCheck.getIsLogged().equals("1")) {
                
            }
        }
    }
}
