package com.cit.abakar.application.ExampleClasses;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;


public class Equipment extends Model {


    public int centerId;

    public int id;

    public String serialNumber;


    public String inventoryNumber;


    public String name;


    public String fgDismantled;


    public String fgNotInstall;


    public Center center;


    public Equipment() {
        super();

    }

    public Equipment(int centerId, int id, String serialNumber, String inventoryNumber,
                     String name, String fgDismantled, String fgNotInstall,
                     Center center) {
        this.centerId = centerId;
        this.id = id;
        this.serialNumber = serialNumber;
        this.inventoryNumber = inventoryNumber;
        this.name = name;
        this.fgDismantled = fgDismantled;
        this.fgNotInstall = fgNotInstall;
        this.center = center;
    }
}

