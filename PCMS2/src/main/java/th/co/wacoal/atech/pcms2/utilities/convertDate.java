/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.wacoal.atech.pcms2.utilities;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author Administrator
 */
public class convertDate {

    public String convert(long time) {
        Date date = new Date(time);
        Format format = new SimpleDateFormat("dd/MM/yyyy",Locale.US);
        return format.format(date);
    }

    public static String main(
            long time
    ) {
        String convert = new convertDate().convert(time);

        return convert;
    }

}
