import java.util.ArrayList;
import java.util.List;

public class ListaZyczen {
    private List<Pojazd> lista;
    private String id;

    public ListaZyczen(String id) {
        this.lista = new ArrayList<>();
        this.id=id;
    }

    public void dodaj(Pojazd pojazd) {
        lista.add(pojazd);
    }

    public void usun(Pojazd pojazd) {
        lista.remove(pojazd);
    }

    public void wyczysc() {
        lista.clear();
    }

    public List<Pojazd> pobierzListeZyczen() {
        return lista;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(this.id).append(":\n");
        if (lista.isEmpty()) {
            return this.id + ": -- pusto";
        }
        for (Pojazd p : lista) {
            Cennik cennik = Cennik.pobierzCennik();
            double[] ceny = cennik.pobierzCene(p.marka);
            String cenaStr = "";
            if (ceny != null) {
                switch (p.pobierzTypPojazdu()) {
                    case OSOBOWY:
                        if(p.maxKilometry>ceny[2] && ceny[2] != 0) {
                            cenaStr = "cena " + ceny[0] + " (do " + (int) ceny[2] + "), " + ceny[3] + " (od " + ((int) ceny[2] + 1) + ")";
                            break;
                        }else{
                            cenaStr = "cena " + ceny[0];
                            break;
                        }
                    case DOSTAWCZY:
                        if(p.maxKilometry>ceny[2] && ceny[2] != 0) {
                            cenaStr = "cena " + ceny[0] + " (do " + (int) ceny[2] + "), " + ceny[3] + " (od " + ((int) ceny[2] + 1) + ")";
                            break;
                        }else{
                            cenaStr = "cena " + ceny[0];
                            break;
                        }
                    case ZABYTKOWY:
                        if(p.maxKilometry>ceny[2] && ceny[2] != 0) {
                            cenaStr = "cena " + ceny[0] + " (do " + (int) ceny[2] + "), " + ceny[3] + " (od " + ((int) ceny[2] + 1) + ")";
                            break;
                        }else{
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
            result.append(p.toString()).append(", ").append(cenaStr).append("\n");
        }
        return result.toString();
    }
}
