public class Dostawczy extends Pojazd {
    public Dostawczy(String marka, int maxKilometry) {
        super(marka, maxKilometry);
    }

    @Override
    public double obliczCene(boolean maAbonament) {
        Cennik cennik = Cennik.pobierzCennik();
        double[] ceny = cennik.pobierzCene(this.marka);
        double cenakoncowa;
        if (ceny != null) {
           if (this.maxKilometry <= ceny[2] && ceny[2] != 0) {
                cenakoncowa = ceny[0] * this.maxKilometry;
            } else {
                cenakoncowa = ceny[0] * ceny[2] + ceny[3] * (this.maxKilometry - ceny[2]);
            }
            return cenakoncowa;
        }
        return 0; // brak ceny w cenniku
    }

    @Override
    public TypPojazdu pobierzTypPojazdu() {
        return TypPojazdu.DOSTAWCZY;
    }
}
