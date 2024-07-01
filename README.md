# Zavrsni rad - preddiplomski studij računarstva
Mobilna Android aplikacija za višekriterijsko preporučivanje turističkih odredišta.

Uzimajući u obzir postojeća slična rješenja i pristupe, u završnom radu treba analizirati i opisati izazove i mogućnosti preporučivanja turističkih odredišta. Uzimajući u obzir sklonosti korisnika i postojeća turistička odredišta nekog područja, mogućnosti stvaranja preporuka temeljenih na sadržaju, te kolaborativnom filtriranju i rangiranju, treba definirati funkcionalne i nefunkcionalne zahtjeve, kao i predložiti model i arhitekturu mobilne Android aplikacije. Mobilna aplikacija treba omogućiti prijavu i stvaranje profila s interesima korisnika, pregled i izbor multimedijski prikazanih turističkih odredišta, ocjenjivanje i rangiranje, te višekriterijsko preporučivanje odredišta. U praktičnom dijelu rada, koristeći predloženi model i arhitekturu, te prikladne programske jezike i tehnologije (Kotlin, Firebase i druge po potrebi) treba programski ostvariti mobilnu aplikaciju s bazom podataka i sustavom stvaranja preporuka. Mobilnu aplikaciju potrebno je ispitati i analizirati za različite slučajeve korištenja na primjerima odredišta grada Virovitice i okoline.

## Virovitički turistički vodič

- Aplikacija je ostvarena s Kotlin, XML i Firebase kao bazom podataka.
- Firebase Storage je korišten za pohranu učitanih fotografija.
- Korisnik se mora prijaviti ili registrirati kako bi koristio aplikaciju.
- Na karti su prikazane sve unesene lokacije, koje se dohvaćaju iz Firebase baze podataka.
- Klikom na neki pin, otvara se novi zaslon s više informacija o odabranoj lokaciji i na tom zaslonu korisnik može ocijeniti lokaciju po dostupnim kriterijima.
- Preporuke su korisniku sortirane po najboljim ocjenama, za kategorije koje je označio da ga zanimaju.
- Na zaslonu s profilom korinika, korisnik može vidjeti ukupni broj lokacija koje je ocijenio, dodati nove lokacije, ažurirati svoju profilnu fotografiju ili dati feedback.
