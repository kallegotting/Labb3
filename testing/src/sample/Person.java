package sample;


public class Person {

  private String AnställningsID;
  private String fNamn;
  private String enamn;
  private String rumsnummer;
  private String personnummer;


  public Person(String anställningsId, String fNamn, String enamn, String rumsnummer, String personnummer) {
    AnställningsID = anställningsId;
    this.fNamn = fNamn;
    this.enamn = enamn;
    this.rumsnummer = rumsnummer;
    this.personnummer = personnummer;
  }

  public String getAnställningsId() {
    return AnställningsID;
  }

  public void setAnställningsId(String anställningsId) {
    this.AnställningsID = anställningsId;
  }


  public String getFNamn() {
    return fNamn;
  }

  public void setFNamn(String fNamn) {
    this.fNamn = fNamn;
  }


  public String getEnamn() {
    return enamn;
  }

  public void setEnamn(String enamn) {
    this.enamn = enamn;
  }


  public String getRumsnummer() {
    return rumsnummer;
  }

  public void setRumsnummer(String rumsnummer) {
    this.rumsnummer = rumsnummer;
  }


  public String getPersonnummer() {
    return personnummer;
  }

  public void setPersonnummer(String personnummer) {
    this.personnummer = personnummer;
  }

}
