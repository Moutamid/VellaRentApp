package com.moutimid.vellarentapp.model;

public class Villa {
    private HouseRules houseRules;
    private Location location;
    private PropertyAmenities propertyAmenities;
    private PropertyDetails propertyDetails;
    private int area;
    private int bathRoom;
    private int bedroom;
    private String description;
    private String name;
    private int roomType;

    // Default constructor for Firebase
    public Villa() {
    }

    public HouseRules getHouseRules() {
        return houseRules;
    }

    public Location getLocation() {
        return location;
    }

    public PropertyAmenities getPropertyAmenities() {
        return propertyAmenities;
    }

    public PropertyDetails getPropertyDetails() {
        return propertyDetails;
    }

    public int getArea() {
        return area;
    }

    public int getBathRoom() {
        return bathRoom;
    }

    public int getBedroom() {
        return bedroom;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public int getRoomType() {
        return roomType;
    }
}
