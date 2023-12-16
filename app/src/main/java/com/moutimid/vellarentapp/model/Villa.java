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
    private int bill;
    boolean bills_included;
    String image, user_image, user_name;
    String key;
    private double lat;
    private double lng;
    private String title;


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

    public String getImage() {
        return image;
    }

    public int getBill() {
        return bill;
    }

    public boolean isBills_included() {
        return bills_included;
    }

    public String getUser_image() {
        return user_image;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getKey() {
        return key;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getTitle() {
        return title;
    }
}
