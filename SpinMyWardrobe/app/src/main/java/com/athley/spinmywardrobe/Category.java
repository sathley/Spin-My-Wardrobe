package com.athley.spinmywardrobe;

/**
 * Created by sathley on 11/6/2014.
 */
public enum Category {

    Formals(1),
    Casuals(2),
    Party(3),
    Ethnic(4),
    Shoes(5),
    Accessories(6);

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
