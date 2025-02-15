package org.cnr.plantvocdb.exceptions;

public class PlantNotFoundException extends RuntimeException{

    public PlantNotFoundException(String message){
        super(message);
    }

}
