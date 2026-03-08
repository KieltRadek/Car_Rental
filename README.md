# Car_Rental – Wypożyczalnia Samochodów

Aplikacja konsolowa napisana w języku Java, symulująca system zarządzania wypożyczalnią samochodów. Umożliwia obsługę klientów, definiowanie cenników, zarządzanie koszykami zamówień oraz przetwarzanie płatności i zwrotów.

---

## Spis treści

- [Opis projektu](#opis-projektu)
- [Technologie](#technologie)
- [Struktura projektu](#struktura-projektu)
- [Funkcjonalności](#funkcjonalności)
- [Typy pojazdów](#typy-pojazdów)
- [Model cennika](#model-cennika)
- [Metody płatności](#metody-płatności)
- [Jak uruchomić](#jak-uruchomić)
- [Przykład działania](#przykład-działania)

---

## Opis projektu

System wypożyczalni samochodów pozwala na:
- definiowanie cennika dla różnych typów pojazdów,
- tworzenie klientów z budżetem i statusem abonamentowym,
- dodawanie pojazdów do listy życzeń i przenoszenie ich do koszyka,
- obliczanie kosztów wynajmu z uwzględnieniem taryf i abonamentu,
- realizowanie płatności kartą lub przelewem,
- obsługę zwrotów z automatycznym przeliczeniem refundacji.

---

## Technologie

- **Język:** Java (JDK 8+)
- **IDE:** IntelliJ IDEA
- **Zależności:** brak zewnętrznych bibliotek – czyste Java SE

---

## Struktura projektu

```
Car_Rental/
├── src/
│   ├── Main.java              # Punkt wejścia – demonstracja działania systemu
│   ├── Pojazd.java            # Abstrakcyjna klasa bazowa dla wszystkich pojazdów
│   ├── Osobowy.java           # Pojazd osobowy
│   ├── Dostawczy.java         # Pojazd dostawczy
│   ├── Zabytkowy.java         # Pojazd zabytkowy
│   ├── Darmowy.java           # Pojazd darmowy (dla abonentów)
│   ├── TypPojazdu.java        # Enum: typy pojazdów
│   ├── Cennik.java            # Singleton z cennikiem pojazdów
│   ├── Klient.java            # Klasa klienta (portfel, abonament, koszyk)
│   ├── Koszyk.java            # Koszyk zamówień z obliczaniem cen
│   ├── ListaZyczen.java       # Lista życzeń klienta
│   └── MetodaPlatnosci.java   # Enum: metody płatności
├── Wypozyczalnia.iml          # Plik projektu IntelliJ IDEA
├── .gitignore
└── README.md
```

---

## Funkcjonalności

### Zarządzanie klientem
- Tworzenie klienta z nazwą, budżetem (portfelem) i statusem abonamentu.
- Klient może posiadać listę życzeń oraz koszyk zamówień.

### Lista życzeń i koszyk
- Klient dodaje pojazdy do **listy życzeń**.
- Metoda `przepakuj()` przenosi pojazdy z listy życzeń do **koszyka**, pomijając te, dla których brak ceny w cenniku.

### Cennik (Singleton)
- Centralny rejestr cen pojazdów.
- Obsługuje różne modele taryfowania (patrz [Model cennika](#model-cennika)).

### Płatność
- Klient płaci za pojazdy w koszyku metodą `zaplac(MetodaPlatnosci, boolean)`.
- Parametr `boolean` decyduje, czy w przypadku braku środków aplikacja automatycznie odłoży nadmiarowe kilometry/pojazdy (`true`), czy wyczyści koszyk bez płatności (`false`).

### Zwroty
- Metoda `zwroc(TypPojazdu, marka, km)` przetwarza zwrot określonej liczby kilometrów.
- Kwota refundacji jest zwracana do portfela klienta.

---

## Typy pojazdów

| Typ           | Klasa         | Opis                                                  |
|---------------|---------------|-------------------------------------------------------|
| `OSOBOWY`     | `Osobowy`     | Samochód osobowy; obsługuje taryfy abonamentowe       |
| `DOSTAWCZY`   | `Dostawczy`   | Pojazd dostawczy; stała stawka z limitem kilometrów   |
| `ZABYTKOWY`   | `Zabytkowy`   | Pojazd zabytkowy; stawka stała za każdy km            |
| `DARMO`       | `Darmowy`     | Darmowy przejazd dla abonentów (do określonego limitu km) |

---

## Model cennika

Cennik jest zarządzany przez klasę `Cennik` (wzorzec Singleton). Dostępne są cztery przeciążenia metody `dodaj()`:

| Metoda                                              | Zastosowanie                                                  |
|-----------------------------------------------------|---------------------------------------------------------------|
| `dodaj(typ, nazwa, cenaZAbo, cenaBezAbo, limit, cenaPoLim)` | Pojazd osobowy z abonamentem i limitem km         |
| `dodaj(typ, nazwa, cenaDoLimitu, limit, cenaPoLimitu)`      | Pojazd dostawczy z limitem km                     |
| `dodaj(typ, nazwa, cenaZaKm)`                               | Pojazd zabytkowy – stawka stała za km             |
| `dodaj(typ, limitKm, nazwa)`                                | Pojazd darmowy – bezpłatny do określonego limitu  |

**Przykłady:**
```java
// Syrena: 1.5 zł/km z abonamentem; bez abonamentu: 2.5 zł/km (do 100 km), 1.85 zł/km powyżej
cennik.dodaj(TypPojazdu.OSOBOWY, "Syrena", 1.5, 2.5, 100, 1.85);

// Żuk: 4 zł/km (do 150 km), 3 zł/km powyżej
cennik.dodaj(TypPojazdu.DOSTAWCZY, "Żuk", 4, 150, 3);

// Ford T: 10 zł/km
cennik.dodaj(TypPojazdu.ZABYTKOWY, "Ford T", 10);

// Tuk-Tuk: darmowy do 50 km (tylko abonenci)
cennik.dodaj(TypPojazdu.DARMO, 50, "Tuk-Tuk");
```

---

## Metody płatności

| Metoda        | Prowizja | Opis                   |
|---------------|----------|------------------------|
| `KARTA`       | 1%       | Płatność kartą         |
| `PRZELEW`     | 0%       | Płatność przelewem     |

---

## Jak uruchomić

### Wymagania
- Java JDK 8 lub nowszy

### Kompilacja i uruchomienie (wiersz poleceń)

```bash
# Przejdź do katalogu projektu
cd Car_Rental

# Skompiluj wszystkie pliki źródłowe
javac -d out src/*.java

# Uruchom aplikację
java -cp out Main
```

### Uruchomienie w IntelliJ IDEA
1. Otwórz projekt (`File → Open → Car_Rental`).
2. Ustaw `Main` jako klasę startową.
3. Kliknij `Run`.

---

## Przykład działania

```
Lista życzeń klienta f1: [Syrena, typ: osobowy, ile: 80 km, Żuk, typ: dostawczy, ile: 200 km, ...]
Po przepakowaniu, lista życzeń klienta f1: []
Po przepakowaniu, koszyk klienta f1: [Syrena, typ: osobowy, ile: 80 km, ...]
Samochody Syrena w koszyku klienta f1 kosztowały: 120.0
Po zapłaceniu, klientowi f1 zostało: X zł
...
Po zwrocie, klientowi dakar zostało: Y zł
```

Pełna demonstracja w pliku `src/Main.java`.