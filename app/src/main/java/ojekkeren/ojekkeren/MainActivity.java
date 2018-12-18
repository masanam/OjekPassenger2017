package ojekkeren.ojekkeren;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = getApplicationContext();
        CharSequence text = "Pick Our Service..";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

    }

    /*ride Menu Button*/
    public void rideMenuButtonMethod(View v){
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        Intent intent = new Intent(v.getContext(), GoRideMenu.class);
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        pDialog.cancel();
        startActivity(intent);
    }

    /*send doc Menu Button*/
    public void sendMenuButtonMethod(View v){
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        Intent intent = new Intent(v.getContext(), GoSendActivity.class);
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        pDialog.cancel();
        startActivity(intent);
    }

    /*delivery Menu Button*/
    public void deliMenuButtonMethod(View v){
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        Intent intent = new Intent(v.getContext(), DeliveryMenu.class);
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        pDialog.cancel();
        startActivity(intent);
        /*Context context = getApplicationContext();
        CharSequence text = "Under Construction (Bug Research)";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();*/
    }

    public void maintenanceButtonMethod(View v){
        Context context = getApplicationContext();
        CharSequence text = "Menu sedang dikembangkan";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
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
                    menu.findItem(R.id.actionOrders).setVisible(false);
                }
            }else if(akunLoggedCheck.getIsLogged().equals("1")) {
                if (menu != null) {
                    menu.findItem(R.id.actionLogout).setVisible(true);
                    menu.findItem(R.id.actionLogin).setVisible(false);
                    menu.findItem(R.id.actionOrders).setVisible(true);
                    Log.w("Logged ",akunLoggedCheck.getIsLogged());
                }
            }else{
                if (menu != null) {
                    menu.findItem(R.id.actionLogout).setVisible(false);
                    menu.findItem(R.id.actionLogin).setVisible(true);
                    menu.findItem(R.id.actionOrders).setVisible(false);
                }
            }
        }else{
            if (menu != null) {
                menu.findItem(R.id.actionLogout).setVisible(false);
                menu.findItem(R.id.actionLogin).setVisible(true);
                menu.findItem(R.id.actionOrders).setVisible(false);
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

        if(id == R.id.actionOrders){
            DBAccount dbAccount = new DBAccount(this);
            Intent intent = new Intent(getBaseContext(), Orders.class);
            startActivity(intent);
        }

        if(id == R.id.actionExit){
            DBAccount dbAccount = new DBAccount(this);
            dbAccount.setLogout();
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
            finish();
            System.exit(0);
        }

        return super.onOptionsItemSelected(item);
    }
}
