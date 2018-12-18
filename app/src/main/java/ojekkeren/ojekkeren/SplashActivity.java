package ojekkeren.ojekkeren;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.util.logging.Handler;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class SplashActivity extends Activity {

    private ProgressDialog pDialog;
    private boolean retVal = true;

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

    public boolean getCheckConnection(){

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getServerAPIServer())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        CheckConnectionAPI checkConnectionAPI = restAdapter.create(CheckConnectionAPI.class);

        checkConnectionAPI.checkConn("checkConnection", new Callback<CheckConnectionPojo>() {

            @Override
            public void success(CheckConnectionPojo checkConnectionPojo, Response response) {
                retVal = true;
            }

            @Override
            public void failure(RetrofitError error) {
                retVal = false;
            }
        });
        return retVal;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /*pDialog = new ProgressDialog(SplashActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();*/


        final TextView loadingText = (TextView) findViewById(R.id.loadingText);
        /*Startup*/
        int secondsDelayed = 1;
        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                loadingText.setText("Check Connection");
                getCheckConnection();
            }
        }, secondsDelayed * 10);

        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {

                loadingText.setText("Rebuild Account Database");
                setAndResetAccount();
            }
        }, secondsDelayed * 20);

        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                loadingText.setText("Rebuild Order Database");
                rebuildOrderDatabase();
            }
        }, secondsDelayed * 30);

        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                loadingText.setText("All Ready");
                rebuildOrderDatabase();
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                overridePendingTransition(android.R.anim.bounce_interpolator,android.R.anim.anticipate_interpolator);
                startActivity(intent);
                finish();
            }
        }, secondsDelayed * 40);


    }

    public void rebuildOrderDatabase(){
        DBOrder dbOrder = new DBOrder(this);
        dbOrder.reCreateDB();
    }

    public void setAndResetAccount(){
        DBAccount dbAccount = new DBAccount(this);

        MemberAkun memberAkun = new MemberAkun();
        memberAkun.setPhoneNumber("0");
        memberAkun.setIsLogged("0");
        memberAkun.setId(0);

        if(dbAccount.getCurrentMemberDetails().getIsLogged() == null)
        {
            dbAccount.startUpMemberTable(memberAkun);
        }else{
            if(!dbAccount.getCurrentMemberDetails().getIsLogged().equals("1")){
                dbAccount.reCreateDB();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
