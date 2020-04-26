package com.adivid.zpattendanceadmin.model_dropdown;

import com.google.gson.annotations.SerializedName;

public class BlockInfo {

    @SerializedName("id")
    private String id;
    @SerializedName("block_name")
    private String blockName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }
}
