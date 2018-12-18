package ojekkeren.ojekkeren;

import android.app.Application;

/**
 * Created by andi on 4/17/2016.
 */
public class DataBetweenTabs extends Application {

    private String txtValue;

    public String getTxtValue(){
        return txtValue;
    }
    public void setTxtValue(String aString){
        txtValue= aString;
    }
}
