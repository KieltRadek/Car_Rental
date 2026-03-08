import java.util.HashMap;
import java.util.Map;

public class Cennik {
    private static Cennik instance;
    private Map<String, double[]> ceny;

    private Cennik() {
        ceny = new HashMap<>();
    }

    public static Cennik pobierzCennik() {
        if (instance == null) {
            instance = new Cennik();
        }
        return instance;
    }


    public double[] pobierzCene(String nazwa) {
        return ceny.get(nazwa);
    }

    public void dodaj(TypPojazdu typ, String nazwa, double cenaZAbonamentem, double cenaBezAbonamentu, int limitKm, double cenaPoLimicie) {
        ceny.put(nazwa, new double[]{cenaZAbonamentem, cenaBezAbonamentu, (double) limitKm, cenaPoLimicie});
    }

    public void dodaj(TypPojazdu typ, String nazwa, double cenaDoLimitu, int limitKm, double cenaPoLimicie) {
        ceny.put(nazwa, new double[]{cenaDoLimitu, cenaDoLimitu, (double) limitKm, cenaPoLimicie});
    }

    public void dodaj(TypPojazdu typ, String nazwa, double cenaZaKm) {
        ceny.put(nazwa, new double[]{cenaZaKm, cenaZaKm, 0, cenaZaKm});
    }

    public void dodaj(TypPojazdu typ, int limitKm, String nazwa) {
        ceny.put(nazwa, new double[]{0, 0, (double) limitKm, 0});
    }


}
