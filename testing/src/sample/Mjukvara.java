package sample;


public class Mjukvara {

  private String verision;
  private String typ;
  private String mNamn;

  public Mjukvara(String verision, String typ, String mNamn) {
    this.verision = verision;
    this.typ = typ;
    this.mNamn = mNamn;
  }

  public String getVerision() {
    return verision;
  }

  public void setVerision(String verision) {
    this.verision = verision;
  }


  public String getTyp() {
    return typ;
  }

  public void setTyp(String typ) {
    this.typ = typ;
  }


  public String getMNamn() {
    return mNamn;
  }

  public void setMNamn(String mNamn) {
    this.mNamn = mNamn;
  }

}
