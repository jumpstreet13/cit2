package com.cit.abakar.application.ExampleClasses;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

public class Center extends Model  {

    public String name;

    public Center(){

    }


    public Center(String name) {

        this.name = name;
    }

}
