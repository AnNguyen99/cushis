package com.viettel.ocs;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;

@ManagedBean(name = "home", eager = true)
@RequestScoped
public class Home implements Serializable {
    private static final long serialVersionUID = 1L;

    public Home() {
    }

    public String getMessage() {
        return "Welcome Cushis";
    }

}
