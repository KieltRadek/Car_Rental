public class Zabytkowy extends Pojazd {
    public Zabytkowy(String marka, int maxKilometry) {
        super(marka, maxKilometry);
    }

    @Override
    public double obliczCene(boolean abonament) {
        Cennik cennik = Cennik.pobierzCennik();
        double[] ceny = cennik.pobierzCene(this.marka);
        if(ceny!=null){
            return ceny[0] * this.maxKilometry;
        }
        return 0; //brak ceny w cenniku
    }



    @Override
    public TypPojazdu pobierzTypPojazdu() {
        return TypPojazdu.ZABYTKOWY;
    }
}
