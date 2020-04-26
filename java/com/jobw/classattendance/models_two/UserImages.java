package com.adivid.zpattendanceadmin.models_two;

import com.google.gson.annotations.SerializedName;

public class UserImages {


    @SerializedName("image_1")
    private String image1;
    @SerializedName("image_2")
    private String image2;
    @SerializedName("image_3")
    private String image3;
    @SerializedName("image_4")
    private String image4;
    @SerializedName("image_5")
    private String image5;

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public String getImage5() {
        return image5;
    }

    public void setImage5(String image5) {
        this.image5 = image5;
    }
}
