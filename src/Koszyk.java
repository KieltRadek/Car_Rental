import java.util.ArrayList;
import java.util.List;

public class Koszyk {
    private List<Pojazd> listaKoszyk;
    private boolean abonament;

    private String idKlienta;

    public Koszyk(boolean abonament, String idKlienta) {
        listaKoszyk = new ArrayList<>();
        this.abonament = abonament;
        this.idKlienta=idKlienta;
    }

    public double cena(String marka){
        double cena = 0;
        Cennik cennik = Cennik.pobierzCennik();
        double[] ceny = cennik.pobierzCene(marka);

        if(ceny == null) {
            return 0;
        }

        for(Pojazd p : listaKoszyk){
            if (p.getMarka().equals(marka)) {
                double stawka;
                if(abonament){
                    stawka = ceny[0];
                }else{
                    stawka = ceny[1];
                }
                double stawkaPoLimicie = ceny[3];
                double limitkm = ceny[2];

                if(p.maxKilometry<=limitkm){
                    cena+=p.maxKilometry*stawka;
                } else{
                    cena+= limitkm * stawka + (p.maxKilometry - limitkm) * stawkaPoLimicie;
                }
            }
        }
        return cena;
    }

    public List<Pojazd> pobierzPojazdy() {
        return new ArrayList<>(listaKoszyk);
    }


    public void wyczysc() {
        listaKoszyk.clear();
    }

    public void dodaj(Pojazd p) {
        listaKoszyk.add(p);
    }

    public void usun(Pojazd p){
        listaKoszyk.remove(p);
    }

    @Override
    public String toString() {
        if (listaKoszyk.isEmpty()) {
            return idKlienta + ": --pusto";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(this.idKlienta).append(":\n");
        for (Pojazd pojazd : listaKoszyk) {
            Cennik cennik = Cennik.pobierzCennik();
            double[] ceny = cennik.pobierzCene(pojazd.getMarka());
            String cenaStr = "";
            if (ceny != null) {
                switch (pojazd.pobierzTypPojazdu()) {
                    case OSOBOWY:
                        if (pojazd.maxKilometry > ceny[2] && ceny[2] != 0) {
                            cenaStr = "cena " + ceny[0] + " (do " + (int) ceny[2] + "), " + ceny[3] + " (od " + ((int) ceny[2] + 1) + ")";
                            break;
                        } else {
                            cenaStr = "cena " + ceny[0];
                            break;
                        }
                    case DOSTAWCZY:
                        if (pojazd.maxKilometry > ceny[2] && ceny[2] != 0) {
                            cenaStr = "cena " + ceny[0] + " (do " + (int) ceny[2] + "), " + ceny[3] + " (od " + ((int) ceny[2] + 1) + ")";
                            break;
                        } else {
                            cenaStr = "cena " + ceny[0];
                            break;
                        }
                    case ZABYTKOWY:
                        if (pojazd.maxKilometry > ceny[2] && ceny[2] != 0) {
                            cenaStr = "cena " + ceny[0] + " (do " + (int) ceny[2] + "), " + ceny[3] + " (od " + ((int) ceny[2] + 1) + ")";
                            break;
                        } else {
                            cenaStr = "cena " + ceny[0];
                            break;
                        }
                    case DARMO:
                        cenaStr = "ceny " + ceny[0];
                        break;
                }
            } else {
                cenaStr = "ceny brak";
            }
            sb.append(pojazd).append(", ").append(cenaStr).append("\n");
        }

        return sb.toString();
    }

    public void dodajPojazdy(List<Pojazd> pojazdy) {
        for (Pojazd p : pojazdy) {
            listaKoszyk.add(p);
        }
    }


}
