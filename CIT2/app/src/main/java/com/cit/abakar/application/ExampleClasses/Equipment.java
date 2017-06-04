package com.cit.abakar.application.ExampleClasses;

public class Equipment  {


    public int centerId;

    public int id;

    public String serialNumber;


    public String inventoryNumber;


    public String name;


    public String fgDismantled;


    public String fgNotInstall;


    public Center center;


    public Equipment() {

    }

    public Equipment(int centerId, int id, String name, String fgDismantled, String fgNotInstall) {
        this.centerId = centerId;
        this.id = id;
        this.name = name;
        this.fgDismantled = fgDismantled;
        this.fgNotInstall = fgNotInstall;
    }
}

