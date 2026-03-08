public class Osobowy extends Pojazd {
    public Osobowy(String marka, int maxKilometry) {
        super(marka, maxKilometry);
    }

    @Override
    public TypPojazdu pobierzTypPojazdu() {
        return TypPojazdu.OSOBOWY;
    }


    @Override
    public double obliczCene(boolean maAbonament) {
        Cennik cennik = Cennik.pobierzCennik();
        double[] ceny = cennik.pobierzCene(this.marka);
        double cenakoncowa;
        if (ceny != null) {
            if (maAbonament) {
                cenakoncowa = ceny[0] * this.maxKilometry;
            } else if (this.maxKilometry <= ceny[2] && ceny[2] != 0) {
                cenakoncowa = ceny[1] * this.maxKilometry;
            } else {
                cenakoncowa = ceny[1] * ceny[2] + ceny[3] * (this.maxKilometry - ceny[2]);
            }
            return cenakoncowa;
        }
        return 0; // brak ceny w cenniku
    }
}
