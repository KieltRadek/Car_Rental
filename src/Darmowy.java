public class Darmowy extends Pojazd {
    public Darmowy(String marka, int maxKilometry) {
        super(marka, Math.min(maxKilometry, 50));
    }

    @Override
    public double obliczCene(boolean abonament) {
        return 0;
    }


    @Override
    public TypPojazdu pobierzTypPojazdu() {
        return TypPojazdu.DARMO;
    }
}
