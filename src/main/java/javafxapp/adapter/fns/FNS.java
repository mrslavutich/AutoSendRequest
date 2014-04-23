package javafxapp.adapter.fns;


import javafxapp.adapter.SmevFields;

import java.util.UUID;

public class FNS extends SmevFields {
    public String ogrn;
    public String inn;
    public String idDoc;
    public String isOgrn;
    public String isInn;
    public String ИдЗапросФ;


    public String getOgrn() {
        return ogrn;
    }

    public void setOgrn(String ogrn) {
        this.ogrn = ogrn;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getIsOgrn() {
        return isOgrn;
    }

    public void setIsOgrn(String isOgrn) {
        this.isOgrn = isOgrn;
    }

    public String getIsInn() {
        return isInn;
    }

    public void setIsInn(String isInn) {
        this.isInn = isInn;
    }

    public String getИдЗапросФ() {
        return ИдЗапросФ;
    }

    public void setИдЗапросФ(String идЗапросФ) {
        ИдЗапросФ = идЗапросФ;
    }

    public String getIdDoc() {
        return idDoc;
    }

    public void setIdDoc(String idDoc) {
        this.idDoc = UUID.randomUUID().toString();
    }



}
