package com.cit.abakar.application.database;


import android.widget.ListView;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

@Table(name = "Center")
public class Center extends Model {
    @Column(name = "name")
    public String name;

    public Center(){
        super();
    }

    public List<Visit> visits(){
        return getMany(Visit.class, "Center");
    }

    public List<Equipment> equipments(){
        return getMany(Equipment.class, "Center");
    }

    public static List<Center> getAll(){
        return  new Select().from(Center.class).execute();
    }

    public Center(String name) {
        super();
        this.name = name;
    }
}