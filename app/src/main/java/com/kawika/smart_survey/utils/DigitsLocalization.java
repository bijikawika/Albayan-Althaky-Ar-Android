package com.kawika.smart_survey.utils;

/**
 * Created by senthiljs on 20/03/18.
 */

public class DigitsLocalization {

    public static String convertEnglishDigitsToArabic(String arabicInput){
        return arabicInput.replace("0","٠")
        .replace("1","١")
                .replace("2","٢")
                .replace("3","٣")
                .replace("4", "٤")
                .replace("5", "٥")
                .replace("6", "٦")
                .replace("7", "٧")
                .replace("8", "٨")
                .replace("9", "٩");
    }
}
