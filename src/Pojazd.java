public abstract class Pojazd {
    protected String marka;
    protected int maxKilometry;

    public Pojazd(String marka, int maxKilometry) {
        this.marka = marka;
        this.maxKilometry = maxKilometry;
    }

    public double obliczCene(boolean abonament){
        Cennik cennik = Cennik.pobierzCennik();
        double[] ceny = cennik.pobierzCene(marka);

        if(ceny == null) {
            return 0;
        }
        double stawka;
        double stawkaPoLimicie = ceny[3];
        double limitkm = ceny[2];

        if (abonament){
            stawka = ceny[0];
        } else {
            stawka = ceny[1];
        }

        if(maxKilometry<= limitkm) {
            return stawka * maxKilometry;
        } else {
            return limitkm * stawka + (maxKilometry - limitkm) * stawkaPoLimicie;
        }
    }

    public double obliczCeneZKm(boolean abonament, int km){
        Cennik cennik = Cennik.pobierzCennik();
        double[] ceny = cennik.pobierzCene(marka);

        if(ceny == null) {
            return 0;
        }
        double stawka;
        double stawkaPoLimicie = ceny[3];
        double limitkm = ceny[2];

        if (abonament){
            stawka = ceny[0];
        } else {
            stawka = ceny[1];
        }

        if (km <= limitkm) {
            return stawka * km;
        } else {
            return limitkm * stawka + (km - limitkm) * stawkaPoLimicie;
        }
    }

    public abstract TypPojazdu pobierzTypPojazdu();

    public String getMarka() {
        return marka;
    }


    @Override
    public String toString() {
        return marka + ", typ: " + (getClass().getSimpleName().toLowerCase()) + ", ile: " + maxKilometry + " km";
    }
}
