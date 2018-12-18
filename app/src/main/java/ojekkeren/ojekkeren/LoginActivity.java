package ojekkeren.ojekkeren;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class LoginActivity extends ActionBarActivity {
    private ProgressDialog pDialog;

    public String getServerAPIServer(){
        Context context = getApplicationContext();
        String ServerAPI;
        ApplicationInfo ai = null;
        try {
            ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ServerAPI = ai.metaData.get("ServerAPI").toString();
        return ServerAPI;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final TextView errorText = (TextView) findViewById(R.id.errorText);
        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        Intent getLogOutIntent = getIntent();
        if(getLogOutIntent.getStringExtra("bye") != null){
            if(getLogOutIntent.getStringExtra("bye").equals("1")){
                errorText.setText("Bye..");
            }
        }

        pDialog.cancel();
        TextView getCurrentLocation = (TextView) findViewById(R.id.registerbutton);
        getCurrentLocation.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(v.getContext(), RegisterActivity.class);
                startActivity(intent);
                return false;
            }
        });

        final EditText phoneNum = (EditText) findViewById(R.id.phonenum);
        final DBAccount dbAccount = new DBAccount(this);

        if(dbAccount.getCurrentMemberDetails().getIsLogged() != null){
            MemberAkun akunLoggedCheck = dbAccount.getCurrentMemberDetails();

            if(akunLoggedCheck.getIsLogged().equals("1"))
            {
                Intent prevIntent = getIntent();

                Intent intent = new Intent(LoginActivity.this, ConfirmationToOrderRide.class);
                intent.putExtra("orderFrom", prevIntent.getStringExtra("orderFrom"));

                intent.putExtra("orderFrom", prevIntent.getStringExtra("orderFrom"));
                intent.putExtra("orderFromAddress", prevIntent.getStringExtra("orderFromAddress"));
                intent.putExtra("orderDest", prevIntent.getStringExtra("orderDest"));
                intent.putExtra("orderDestAddress", prevIntent.getStringExtra("orderDestAddress"));
                if(prevIntent.getStringExtra("ItemToDeliver") != null)
                {
                    intent.putExtra("ItemToDeliver", prevIntent.getStringExtra("ItemToDeliver"));
                }

                intent.putExtra("Distance", prevIntent.getStringExtra("Distance"));

                startActivity(intent);
            }
        }

        ImageButton nextOrder = (ImageButton) findViewById(R.id.nextOrder);
        nextOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(LoginActivity.this);
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(true);
                pDialog.show();

                String base_url = getServerAPIServer();
                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(base_url)
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .build();

                LoginAPI loginap = restAdapter.create(LoginAPI.class);


                EditText pass = (EditText) findViewById(R.id.password);
                loginap.getJson("loginFront", phoneNum.getText().toString(), pass.getText().toString(), new Callback<LoginPojo>() {
                    @Override
                    public void success(LoginPojo loginPojo, Response response) {

                        Log.w("RESPONSE ", loginPojo.getResponse());
                        if (!loginPojo.getResponse().equals("WELCOME")) {

                            errorText.setText("Wrong Phone Number and /or Password");
                            errorText.setVisibility(View.VISIBLE);
                        } else {
                            Log.w("Result Kalang ", "Masuk Kalang Iii");
                            Intent prevIntent = getIntent();



                            /*Set logged in member*/
                            MemberAkun memberAkun = new MemberAkun();
                            memberAkun.setPhoneNumber(phoneNum.getText().toString());
                            memberAkun.setId(0);
                            dbAccount.setLoginFlag(memberAkun);

                            DBOrder dbOrder = new DBOrder(LoginActivity.this);
                            String latestOrder = dbOrder.getTheLatestOrderStatus();
                            if(latestOrder != null){
                                if(latestOrder.equals("3"))
                                {
                                /*Choose the intent , if has order =3 in the last order then go to confirmation ride page*/
                                    Intent intent = new Intent(LoginActivity.this, ConfirmationToOrderRide.class);
                                    Log.w("Order Latest", dbOrder.getTheLatestOrderStatus());
                                    startActivity(intent);

                                    finish();

                                }else{
                                /*Choose the intent , if has order =3 in the last order then go to confirmation ride page*/
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }else{
                                Toast.makeText(LoginActivity.this, "Welcome back!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }



                        }
                        pDialog.cancel();
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        DBAccount dbAccount = new DBAccount(this);

        if(dbAccount.getCurrentMemberDetails().getIsLogged() != null){
            MemberAkun akunLoggedCheck = dbAccount.getCurrentMemberDetails();

            Log.w("MENU STAT",akunLoggedCheck.getIsLogged());
            if(akunLoggedCheck.getIsLogged().equals("0"))
            {
                if (menu != null) {
                    menu.findItem(R.id.actionLogout).setVisible(false);
                    menu.findItem(R.id.actionLogin).setVisible(true);
                }
            }else if(akunLoggedCheck.getIsLogged().equals("1")) {
                if (menu != null) {
                    menu.findItem(R.id.actionLogout).setVisible(true);
                    menu.findItem(R.id.actionLogin).setVisible(false);
                    Log.w("Logged ",akunLoggedCheck.getIsLogged());
                }
            }else{
                if (menu != null) {
                    menu.findItem(R.id.actionLogout).setVisible(false);
                    menu.findItem(R.id.actionLogin).setVisible(true);
                }
            }
        }else{
            if (menu != null) {
                menu.findItem(R.id.actionLogout).setVisible(false);
                menu.findItem(R.id.actionLogin).setVisible(true);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingAtivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.actionLogout){
            DBAccount dbAccount = new DBAccount(this);
            dbAccount.setLogout();
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        if(id == R.id.actionLogin){
            DBAccount dbAccount = new DBAccount(this);
            dbAccount.setLogout();
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
