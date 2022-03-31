package sample;

public class Accident {
    private String tip, data, pagube, angajati, locatie, administrator, descriere;

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPagube() {
        return pagube;
    }

    public void setPagube(String pagube) {
        this.pagube = pagube;
    }

    public String getAngajati() {
        return angajati;
    }

    public void setAngajati(String angajati) {
        this.angajati = angajati;
    }

    public String getLocatie() {
        return locatie;
    }

    public void setLocatie(String locatie) {
        this.locatie = locatie;
    }

    public String getAdministrator() {
        return administrator;
    }

    public void setAdministrator(String administrator) {
        this.administrator = administrator;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public Accident(String tip, String data, String pagube, String angajati, String locatie, String administrator, String descriere) {
        this.tip = tip;
        this.data = data;
        this.pagube = pagube;
        this.angajati = angajati;
        this.locatie = locatie;
        this.administrator = administrator;
        this.descriere = descriere;
    }
}


