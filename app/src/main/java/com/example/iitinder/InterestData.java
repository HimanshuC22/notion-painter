package com.example.iitinder;

/**
 * Class for data of an interest
 */

public class InterestData {

    private String interestName;
    private int interestImage;
    public boolean selected;

    public InterestData(String interestName, int interestImage, boolean selected) {
        this.interestName = interestName;
        this.interestImage = interestImage;
        this.selected = selected;
    }

    public String getInterestName() {
        return interestName;
    }

    public boolean isSelected() {
        return selected;
    }

    public int getInterestImage() {
        return interestImage;
    }
}
