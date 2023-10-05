package com.sensor;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        final String URL_GET_BASE =  "https://api.hgbrasil.com/weather?fields=only_results,temp,date,description,currently,city,humidity,rain,sunrise,sunset,moon_phase&key=4b390769&woeid=";
        String sjc = URL_GET_BASE + "455912";
        String canoinhas = URL_GET_BASE + "456116";
        String sp = URL_GET_BASE + "455827";
        String florianopolis = URL_GET_BASE + "90200648";

        WeatherThread runnableCanoinhas = new WeatherThread(canoinhas, Long.valueOf(1));
        WeatherThread runnableSJC = new WeatherThread(sjc, Long.valueOf(2));
        WeatherThread runnableFlorianopolis = new WeatherThread(florianopolis, Long.valueOf(3));
        WeatherThread runnableSp = new WeatherThread(sp, Long.valueOf(4));
        
        Thread threadSjc = new Thread(runnableSJC);
        Thread threadCanoinhas = new Thread(runnableCanoinhas);
        Thread threadSp = new Thread(runnableSp);
        Thread threadFlorianopolis = new Thread(runnableFlorianopolis);

        threadSjc.start();
        threadCanoinhas.start();
        threadSp.start();
        threadFlorianopolis.start();
    }
}