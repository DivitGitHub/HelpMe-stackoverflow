package com.divitngoc.android.helpme_stackoverflow;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by DxAlchemistv1 on 22/05/2017.
 */

public class ListWrapper {

    //JSONbase
    @SerializedName("items")
    @Expose
    private List<Item> items = null;

    public List<Item> getItems() {
        return items;
    }

}
