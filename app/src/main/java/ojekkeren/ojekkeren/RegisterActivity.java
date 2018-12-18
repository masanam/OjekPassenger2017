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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import okhttp3.OkHttpClient;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Client;
import retrofit.client.OkClient;
import retrofit.client.Response;


public class RegisterActivity extends ActionBarActivity {

    private ProgressDialog pDialog;
    private boolean successRegister = false;

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
        setContentView(R.layout.activity_register);

        ImageButton nextconfirmationphone = (ImageButton) findViewById(R.id.nextconfirmationphone);
        nextconfirmationphone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(RegisterActivity.this);
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(false);
                pDialog.show();
                EditText phoneNum = (EditText) findViewById(R.id.phoneNum);
                EditText confirmPhoneNum = (EditText) findViewById(R.id.confirmPhoneNum);

                EditText email = (EditText) findViewById(R.id.email);
                EditText password = (EditText) findViewById(R.id.password);

                TextView errorRegId = (TextView) findViewById(R.id.errorRegId);
                if(phoneNum.getText().toString().equals( confirmPhoneNum.getText().toString()))
                {
                    if(android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches())
                    {
                        if(!(password.getText().toString().length() <= 6)){
                            doRegistration(phoneNum.getText().toString(),
                                    email.getText().toString(),
                                    password.getText().toString());
                            /*Intent intent = new Intent(v.getContext(), LoginActivity.class);
                            startActivity(intent);*/
                        }else{
                            errorRegId.setText("Password must not less than 6 charachter");
                        }
                    }else{
                        errorRegId.setText("Wrong Email Format");
                    }
                }else {
                    errorRegId.setText("Phone not same..");
                }
                pDialog.cancel();

            }
        });
    }

    public boolean doRegistration(final String phoneNum, String email, String password){
        String base_url = getServerAPIServer();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(base_url)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(new com.squareup.okhttp.OkHttpClient()))
                .build();

        RegisterAPI registerAPI = restAdapter.create(RegisterAPI.class);

        registerAPI.doReg("userRegister", phoneNum, email, password, new Callback<RegisterResponsePojo>() {

            @Override
            public void success(RegisterResponsePojo registerResponsePojo, Response response) {
                /*if(response.getReason().toString() == "2"){
                    successRegister=true;
                }*/
                TextView errorRegId = (TextView) findViewById(R.id.errorRegId);
                if (registerResponsePojo.getResponse().equals("USER EXISTS")) {
                    errorRegId.setText("Phone Number Already Registered");
                } else {
                    /*SET lOGIN*/
                    DBAccount dbAccount = new DBAccount(RegisterActivity.this);
                    MemberAkun memberAkun = new MemberAkun();
                    memberAkun.setPhoneNumber(phoneNum);
                    memberAkun.setId(0);
                    dbAccount.setLoginFlag(memberAkun);

                    DBOrder dbOrder = new DBOrder(RegisterActivity.this);
                    String latestOrder = dbOrder.getTheLatestOrderStatus();
                    if (latestOrder.equals("3")) {
                                /*Choose the intent , if has order =3 in the last order then go to confirmation ride page*/
                        Intent intent = new Intent(RegisterActivity.this, ConfirmationToOrderRide.class);
                        startActivity(intent);
                        Log.w("Order Latest", dbOrder.getTheLatestOrderStatus());
                        finish();

                    } else {
                                /*Choose the intent , if has order =3 in the last order then go to confirmation ride page*/
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }


                Log.w("Status Register", response.getBody().toString());
            }

            @Override
            public void failure(RetrofitError cause) {
                Log.w("Retrofit Error ", cause.getResponse().getReason());
            }
        });
        return successRegister;
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
