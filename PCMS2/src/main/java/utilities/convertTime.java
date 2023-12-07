/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author Administrator
 */
public class convertTime {

    public String convert(long time) {
        Date date = new Date(time);
        Format format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",Locale.US);
        return format.format(date);
    }

    public static String main(
            long time
    ) {
        String convert = new convertTime().convert(time);
       
        return convert;
    }

}
