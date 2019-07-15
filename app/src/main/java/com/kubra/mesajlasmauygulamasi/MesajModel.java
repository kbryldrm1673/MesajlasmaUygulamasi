package com.kubra.mesajlasmauygulamasi;


public class MesajModel {
    private String from;
    private String text;



    public MesajModel() {

    }

    public MesajModel(String from, String text) {
        this.from = from;
        this.text = text;


    }

    public String getFrom() {
        return from;
    }


    public String getText() {
        return text;
    }


    @Override
    public String toString() {
        return "MesajModel{" +
                "from='" + from + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
