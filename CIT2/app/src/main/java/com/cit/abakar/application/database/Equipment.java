package com.cit.abakar.application.database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;


@Table(name = "Equipment")
public class Equipment extends Model {

    @Column(name = "center_id")
    public String center_id;

    @Column(name = "serial_number")
    public String serial_number;

    @Column(name = "inventory_number")
    public String inventory_number;

    @Column(name = "name")
    public String name;

    @Column(name = "fg_dismantled")
    public String fg_dismantled;

    @Column(name = "fg_not_install")
    public String fg_not_install;

    public Equipment(){
        super();

    }

    public Equipment(String center_id, String serial_number, String inventory_number, String name, String fg_dismantled,
                     String fg_not_install) {
        super();
        this.center_id = center_id;
        this.serial_number = serial_number;
        this.inventory_number = inventory_number;
        this.name = name;
        this.fg_dismantled = fg_dismantled;
        this.fg_not_install = fg_not_install;
    }
}
