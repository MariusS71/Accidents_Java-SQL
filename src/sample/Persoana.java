package sample;

import javafx.beans.property.SimpleStringProperty;
import java.time.LocalDate;

public class Persoana {
    private SimpleStringProperty nume, prenume, cnp, telefon;
    private String sex, nastere, angajare;;
    private int salariu, intretinuti, accidente;

    public Persoana(String nume, String prenume, String cnp,
                    String sex, String telefon, int salariu, int intretinuti,
                    int accidente, String nastere, String angajare) {
        this.nume = new SimpleStringProperty(nume);
        this.prenume = new SimpleStringProperty(prenume);
        this.cnp = new SimpleStringProperty(cnp);
        this.sex = sex;
        this.telefon = new SimpleStringProperty(telefon);
        this.salariu = salariu;
        this.intretinuti = intretinuti;
        this.accidente = accidente;
        this.nastere = nastere;
        this.angajare = angajare;
    }

    public String getNume() {
        return nume.get();
    }

    public  SimpleStringProperty numeProperty() {
        return nume;
    }

    public String getPrenume() {
        return prenume.get();
    }

    public SimpleStringProperty prenumeProperty() {
        return prenume;
    }

    public String getCnp() {
        return cnp.get();
    }

    public SimpleStringProperty cnpProperty() {
        return cnp;
    }

    public String getTelefon() {
        return telefon.get();
    }

    public SimpleStringProperty telefonProperty() {
        return telefon;
    }

    public String getSex() {
        return sex;
    }

    public int getSalariu() {
        return salariu;
    }

    public int getIntretinuti() {
        return intretinuti;
    }

    public int getAccidente() {
        return accidente;
    }

    public String getNastere() {
        return nastere;
    }

    public String getAngajare() {
        return angajare;
    }

    public void setNume(String nume) {
        this.nume.set(nume);
    }

    public void setPrenume(String prenume) {
        this.prenume.set(prenume);
    }

    public void setCnp(String cnp) {
        this.cnp.set(cnp);
    }

    public void setTelefon(String telefon) {
        this.telefon.set(telefon);
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setSalariu(int salariu) {
        this.salariu = salariu;
    }

    public void setIntretinuti(int intretinuti) {
        this.intretinuti = intretinuti;
    }

    public void setAccidente(int accidente) {
        this.accidente = accidente;
    }

    public void setNastere(String nastere) {
        this.nastere = nastere;
    }

    public void setAngajare(String angajare) {
        this.angajare = angajare;
    }
}
