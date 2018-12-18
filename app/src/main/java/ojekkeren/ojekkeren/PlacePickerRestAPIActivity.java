package ojekkeren.ojekkeren;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.TwoLineListItem;

import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class PlacePickerRestAPIActivity extends AppCompatActivity {

    // Hashmap for ListContent (ListView)
    List<TestingContentsDTO> contactList;

    /*Progress Dialog*/
    private ProgressDialog pDialog;

    /*List Adapter*/
//    private ArrayAdapter<TestingDTO> arrayAdapter;

    private static final String TAG_SEARCH = "search";
    private static final String TAG_NAMA = "nama";
    private static final String TAG_ALAMAT = "alamat";

    final private String url = "http://192.168.1.102/jobbers/api.php?search=dicari&username=uname&password=password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_picker_rest_api);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                new TestingRest().execute();
            }
        });
    }

    /*Connect To http://192.168.1.105/jobbers/api.php*/
    private class TestingRest extends AsyncTask<Void, Void, TestingDTO> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(PlacePickerRestAPIActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected TestingDTO doInBackground(Void... params) {


            return null;
        }

        @Override
        protected void onPostExecute(TestingDTO result) {
            ListAdapter listAdapter = new ArrayAdapter<TestingContentsDTO>(PlacePickerRestAPIActivity.this, android.R.layout.simple_list_item_1, result.getContents());
            ListView listView = (ListView) findViewById(R.id.listContents);
            listView.setAdapter(listAdapter);
            if(pDialog.isShowing())
            {
                pDialog.dismiss();
            }

        }



        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(TestingDTO testingDTO) {
            super.onCancelled(testingDTO);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

    }

    public class MyCustomAdapter extends ArrayAdapter<TestingContentsDTO> {

        private Context context;
        private List<TestingContentsDTO> contents;

        public MyCustomAdapter(Context context, List<TestingContentsDTO> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TwoLineListItem twoLineListItem;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                twoLineListItem = (TwoLineListItem) inflater.inflate(
                        android.R.layout.simple_list_item_2, null);
            } else {
                twoLineListItem = (TwoLineListItem) convertView;
            }

            TextView text1 = twoLineListItem.getText1();
            TextView text2 = twoLineListItem.getText2();

            text1.setText(contents.get(position).getNama());
            text2.setText(contents.get(position).getAlamat());

            return twoLineListItem;
        }
    }

    private class MyAdapter extends BaseAdapter {

        private Context context;
        private List<TestingContentsDTO> contents;

        public MyAdapter(Context context, List<TestingContentsDTO> contents) {
            this.context = context;
            this.contents = contents;
        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TwoLineListItem twoLineListItem;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                twoLineListItem = (TwoLineListItem) inflater.inflate(
                        android.R.layout.simple_list_item_2, null);
            } else {
                twoLineListItem = (TwoLineListItem) convertView;
            }

            TextView text1 = twoLineListItem.getText1();
            TextView text2 = twoLineListItem.getText2();

            text1.setText(contents.get(position).getNama());
            text2.setText(contents.get(position).getAlamat());

            return twoLineListItem;
        }
    }

}
