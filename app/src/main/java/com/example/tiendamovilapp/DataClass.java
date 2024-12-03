package com.example.tiendamovilapp;

public class DataClass {

    private String dataCodigo;
    private String dataNom;
    private String dataCant;
    private String dataPrec;
    private String dataImage;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDataCodigo() {
        return dataCodigo;
    }

    public String getDataNom() {
        return dataNom;
    }

    public String getDataCant() {
        return dataCant;
    }

    public String getDataPrec() {
        return dataPrec;
    }

    public String getDataImage() {
        return dataImage;
    }

    public DataClass(String dataCodigo, String dataNom, String dataCant, String dataPrec, String dataImage) {
        this.dataCodigo = dataCodigo;
        this.dataNom = dataNom;
        this.dataCant = dataCant;
        this.dataPrec = dataPrec;
        this.dataImage = dataImage;
    }

    public DataClass(){

    }
}
