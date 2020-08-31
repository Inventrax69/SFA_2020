package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Naresh on 02-Feb-16.
 */
public class ExecutionResponse {

    @SerializedName("Success")
    private int success;
    @SerializedName("Errors")
    private List<String> errors;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
