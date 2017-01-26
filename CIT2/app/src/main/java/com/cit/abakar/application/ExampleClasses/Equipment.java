package com.cit.abakar.application.ExampleClasses;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;


public class Equipment extends Model {


    public String center_id;


    public String serial_number;


    public String inventory_number;


    public String name;


    public String fg_dismantled;


    public String fg_not_install;


    public Center center;


    public Equipment(){
        super();

    }

    public Equipment(String center_id, String serial_number, String inventory_number,
                     String name, String fg_dismantled, String fg_not_install,
                     Center center) {
        this.center_id = center_id;
        this.serial_number = serial_number;
        this.inventory_number = inventory_number;
        this.name = name;
        this.fg_dismantled = fg_dismantled;
        this.fg_not_install = fg_not_install;
        this.center = center;
    }
}
