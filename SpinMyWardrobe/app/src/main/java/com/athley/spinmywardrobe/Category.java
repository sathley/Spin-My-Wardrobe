package com.athley.spinmywardrobe;

/**
 * Created by sathley on 11/6/2014.
 */
public enum Category {

    Formal(1),
    Casual(2),
    Party(3),
    Ethnic(4),
    Shoes(5),
    Accessory(6);

    private int numVal;
    
    Category(int value)
    {
       this.numVal = value;
    }

    public int getValue()
    {
        return this.numVal;
    }
}
