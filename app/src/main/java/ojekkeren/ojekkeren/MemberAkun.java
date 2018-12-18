package ojekkeren.ojekkeren;

/**
 * Created by andi on 3/30/2016.
 */
public class MemberAkun {
    private int id;
    private String PhoneNumber;
    private String isLogged;
    private String uidReal;

    public String getUidReal() {
        return uidReal;
    }

    public void setUidReal(String uidReal) {
        this.uidReal = uidReal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getIsLogged() {
        return isLogged;
    }

    public void setIsLogged(String isLogged) {
        this.isLogged = isLogged;
    }
}
