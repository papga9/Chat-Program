package cht.projekt_cht;

import java.util.HashMap;

public class IdandPasswords {
    HashMap<String, String> logininfo = new HashMap<String,String>();
    IdandPasswords(){
        logininfo.put("test", "test");
    }

    protected HashMap getLoginInfo(){
        return logininfo;
    }
}
