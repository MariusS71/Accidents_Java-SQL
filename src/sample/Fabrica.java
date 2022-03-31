package sample;

import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class Fabrica {
    private SimpleStringProperty nume, administrator, judet, oras;
    private int angajati, intretinuti, accidente;

    public Fabrica(String nume, String administrator, String judet, String oras, int angajati, int intretinuti, int accidente) {
        this.nume = new SimpleStringProperty(nume);
        this.administrator = new SimpleStringProperty(administrator);
        this.judet = new SimpleStringProperty(judet);
        this.oras = new SimpleStringProperty(oras);
        this.angajati = angajati;
        this.intretinuti = intretinuti;
        this.accidente = accidente;
    }


    public String getNume() {
        return nume.get();
    }

    public SimpleStringProperty numeProperty() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume.set(nume);
    }

    public String getAdministrator() {
        return administrator.get();
    }

    public SimpleStringProperty administratorProperty() {
        return administrator;
    }

    public void setAdministrator(String administrator) {
        this.administrator.set(administrator);
    }

    public String getJudet() {
        return judet.get();
    }

    public SimpleStringProperty judetProperty() {
        return judet;
    }

    public void setJudet(String judet) {
        this.judet.set(judet);
    }

    public String getOras() {
        return oras.get();
    }

    public SimpleStringProperty orasProperty() {
        return oras;
    }

    public void setOras(String oras) {
        this.oras.set(oras);
    }

    public int getAngajati() {
        return angajati;
    }

    public void setAngajati(int angajati) {
        this.angajati = angajati;
    }

    public int getIntretinuti() {
        return intretinuti;
    }

    public void setIntretinuti(int intretinuti) {
        this.intretinuti = intretinuti;
    }

    public int getAccidente() {
        return accidente;
    }

    public void setAccidente(int accidente) {
        this.accidente = accidente;
    }
}