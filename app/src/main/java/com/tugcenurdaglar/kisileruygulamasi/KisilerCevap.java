
package com.tugcenurdaglar.kisileruygulamasi;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KisilerCevap {

    @SerializedName("kisiler")
    @Expose
    private List<Kisiler> kisiler = null;
    @SerializedName("success")
    @Expose
    private Integer success;

    public List<Kisiler> getKisiler() {
        return kisiler;
    }

    public void setKisiler(List<Kisiler> kisiler) {
        this.kisiler = kisiler;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

}
