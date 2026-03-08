import java.util.ArrayList;
import java.util.List;

public class Klient {
    private String id;
    private double portfel;
    private boolean abonament;
    private ListaZyczen listaZyczen;
    private Koszyk koszyk;



    public Klient(String id, double portfel, boolean abonament) {
        this.id = id;
        this.portfel = portfel;
        this.abonament = abonament;
        this.listaZyczen = new ListaZyczen(id);
        this.koszyk = new Koszyk(abonament,id);
    }

    public void dodaj(Pojazd pojazd) {
        listaZyczen.dodaj(pojazd);
    }

    public void przepakuj() {
        Cennik cennik = Cennik.pobierzCennik();
        Koszyk koszyk = pobierzKoszyk();
        ListaZyczen listaZyczen = pobierzListeZyczen();

        koszyk.wyczysc();

        List<Pojazd> listaZyczenCopy = new ArrayList<>(listaZyczen.pobierzListeZyczen());
        double sumaKoszyka = 0;
        for(Pojazd p : listaZyczenCopy) {
            double[] ceny = cennik.pobierzCene(p.getMarka());
            if(ceny != null) {
                double cenaPojazdu = p.obliczCene(abonament);
                if (cenaPojazdu <= portfel) {
                    koszyk.dodaj(p);
                    sumaKoszyka+= cenaPojazdu;
                    listaZyczen.usun(p);
                }else if(cenaPojazdu!=0 && cenaPojazdu>portfel){
                    koszyk.dodaj(p);
                    listaZyczen.usun(p);
                }
            }
        }
        if(sumaKoszyka<portfel){
            portfel-= sumaKoszyka;
        }
    }


    public ListaZyczen pobierzListeZyczen() {
        return listaZyczen;
    }

    public Koszyk pobierzKoszyk() {
        return koszyk;
    }

    private Pojazd utworzPojazd(TypPojazdu typ, String marka, int km) {
        Pojazd pojazd = null;
        switch (typ) {
            case OSOBOWY:
                pojazd = new Osobowy(marka, km);
                break;
            case DOSTAWCZY:
                pojazd = new Dostawczy(marka, km);
                break;
            case ZABYTKOWY:
                pojazd = new Zabytkowy(marka, km);
                break;
            case DARMO:
                pojazd = new Darmowy(marka, km);
                break;
        }
        return pojazd;
    }

    public void zaplac(MetodaPlatnosci metoda, boolean odkladaj) {
        Cennik cennik = Cennik.pobierzCennik();
        double suma = 0;
        List<Pojazd> wszystkiePojazdy = new ArrayList<>(koszyk.pobierzPojazdy());
        for (Pojazd p : wszystkiePojazdy) {
            suma += p.obliczCene(abonament);
        }
        double prowizja;
        if (MetodaPlatnosci.KARTA.equals(metoda)) {
            prowizja = suma * 0.01;
        } else {
            prowizja = 0;
        }

        double kwotaDoZaplaty = suma + prowizja;
        double pozostalaKwotaDoZaplaty = kwotaDoZaplaty - portfel;

        if (kwotaDoZaplaty <= portfel) {
            portfel -= kwotaDoZaplaty;
            koszyk.wyczysc();
        } else if (odkladaj) {
            List<Pojazd> zmniejszonePojazdy = new ArrayList<>();
            double sumaZmniejszonychPojazdow = 0;

            while (pozostalaKwotaDoZaplaty > 0 && !wszystkiePojazdy.isEmpty()) {
                double najnizszaCenaZaKm = Double.MAX_VALUE;
                Pojazd najtanszy = null;
                for (Pojazd p : wszystkiePojazdy) {
                    double[] ceny = cennik.pobierzCene(p.getMarka());
                    if (ceny != null) {
                        double cenaZaKm = ceny[0];
                        if (cenaZaKm < najnizszaCenaZaKm) {
                            najnizszaCenaZaKm = cenaZaKm;
                            najtanszy = p;
                        }
                    }
                }

                if (najtanszy != null) {
                    int resztakm = (int) Math.ceil(pozostalaKwotaDoZaplaty / najnizszaCenaZaKm);
                    if (resztakm > 0) {
                        Pojazd zmniejszonyPojazd = utworzPojazd(najtanszy.pobierzTypPojazdu(), najtanszy.getMarka(), resztakm);
                        if (zmniejszonyPojazd != null) {
                            zmniejszonePojazdy.add(zmniejszonyPojazd);
                            double cenaZmniejszonegoPojazdu = zmniejszonyPojazd.obliczCene(abonament);
                            sumaZmniejszonychPojazdow += cenaZmniejszonegoPojazdu;
                            pozostalaKwotaDoZaplaty -= cenaZmniejszonegoPojazdu;
                            wszystkiePojazdy.remove(najtanszy);
                        }
                    } else {
                        wszystkiePojazdy.remove(najtanszy);
                    }
                }
            }
            koszyk.wyczysc();
            for (Pojazd p : zmniejszonePojazdy) {
                koszyk.dodaj(p);
            }
            portfel -= kwotaDoZaplaty - sumaZmniejszonychPojazdow; // Odejmowanie różnicy od portfela
        } else {
            koszyk.wyczysc();
        }
        portfel -= prowizja;
    }

    public void zwroc(TypPojazdu typ, String nazwa, int km) {
        Cennik cennik = Cennik.pobierzCennik();
        double[] ceny = cennik.pobierzCene(nazwa);

        if (ceny != null) {
            List<Pojazd> pojazdyWKoszyku = koszyk.pobierzPojazdy();
            boolean znaleziono = false;
            for (Pojazd p : pojazdyWKoszyku) {
                if (p.getMarka().equals(nazwa) && p.pobierzTypPojazdu() == typ) {
                    znaleziono = true;
                    int pozostalaLiczbaKm = p.maxKilometry + km;
                    if (pozostalaLiczbaKm > 0) {
                        Pojazd nowyPojazd = utworzPojazd(typ, nazwa, pozostalaLiczbaKm);
                        if (nowyPojazd != null) {
                            koszyk.usun(p);
                            koszyk.dodaj(nowyPojazd);
                            portfel += p.obliczCeneZKm(abonament, km);
                        }
                    } else if (pozostalaLiczbaKm == 0) {
                        koszyk.usun(p);
                        portfel += p.obliczCene(abonament);
                    }
                    break;
                }
            }
            if (!znaleziono) {
                Pojazd pojazd = utworzPojazd(typ, nazwa, km);
                if (pojazd != null) {
                    koszyk.dodaj(pojazd);
                    portfel += pojazd.obliczCene(abonament);
                }
            }
        }
    }


    public double pobierzPortfel() {
        return Math.round(portfel * 100.0) / 100.0;
    }
}
