package com.example.tiendamovilapp;

public class DataClass {

    private String dataCodigo;
    private String dataNom;
    private int dataCant;
    private int dataPrec;
    private String dataImage;
    private String key;

    public void setDataCodigo(String dataCodigo) {
        this.dataCodigo = dataCodigo;
    }

    public void setDataNom(String dataNom) {
        this.dataNom = dataNom;
    }

    public void setDataCant(int dataCant) {
        this.dataCant = dataCant;
    }

    public void setDataPrec(int dataPrec) {
        this.dataPrec = dataPrec;
    }

    public void setDataImage(String dataImage) {
        this.dataImage = dataImage;
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

    public int getDataCant() {
        return dataCant;
    }

    public int getDataPrec() {
        return dataPrec;
    }

    public String getDataImage() {
        return dataImage;
    }

    public String getKey() {
        return key;
    }

    public DataClass(String dataCodigo, String dataNom, int dataCant, int dataPrec, String dataImage, String key) {
        this.dataCodigo = dataCodigo;
        this.dataNom = dataNom;
        this.dataCant = dataCant;
        this.dataPrec = dataPrec;
        this.dataImage = dataImage;
        this.key = key;
    }

    public DataClass(){

    }
}
