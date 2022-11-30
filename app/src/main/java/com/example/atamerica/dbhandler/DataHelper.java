package com.example.atamerica.dbhandler;

import JavaLibrary.MySQL.DataAccess;

public class DataHelper {

    public static DataAccess Query = new DataAccess("jdbc:mysql://150.107.136.126:1443/AtAmerica?useSSL=false&user=atamerica&password=aa1234!!", 20);

}