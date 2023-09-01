# RAF_Cloud-Backend
Advance Web Programming - Final project (January)

Cilj rada je implementacija WEB aplikacije koja će predstavljati imitaciju našeg cloud provajdera. 
Aplikacija treba da stvori utisak da je korisnik u stanju da kreira i kontroliše stanje fizičke mašine i vezanih resursa.

Akcije koje se mogu izvršiti na mašini:
●	SEARCH
●	START
●	STOP
●	RESTART
●	CREATE
●	DESTROY 


Svaka akcija je ispraćena permisijom. Potrebno je proširiti skup permisija i podržati još 6 novih, za svaku od mogućih akcija na mašini: can_search_machines, can_start_machines, can_stop_machines, can_restart_machines, can_create_machines, can_destroy_machines. 
Shodno tome, korisnik bez specifične permisije nema pristup, ni mogućnost za izvršavanja odgovarajuće akcije.

Operacije START, STOP i RESTART nisu instant i za njihovo izvršavanje je potreban neki vremenski period. 
Dok se neka od tih operacija izvršava nad mašinom, obezbediti da nije moguće izvršiti ni jednu drugu operaciju nad tom istom mašinom.

Svaki uspešni poziv ka nekoj od funkcionalnosti mora da vrati odgovor odmah sa statusom 2xx, a ne nakon X sekundi - operacije START, STOP i RESTART nakon poziva odmah vraćaju odgovor, a njihovo izvršavanje na odabranoj mašini će se nastaviti u pozadini.

Akcije/operacije na mašini:
SEARCH
Sledeca tabela ne predstavlja entitet već moguće parametre pretrage. Svi parametri su opcioni.

name - String - Vratiti mašine čije ime je jednako ili sadrži vrednost ovog parametra. Case insensitive komparacija.
status - List<String> - Vratiti mašine čiji je status u jednom od poslatih.
dateFrom - Date (datum bez vremena) - Predstavlja početak vremenskog opsega.Samo ukoliko je i dateTo definisan onda će vratiti mašine čiji je momenat kreiranja između dateFrom i dateTo.
dateTo - Date (datum bez vremena) - Predstavlja kraj vremenskog opsega. Pročitati dateTo.

START
Mašina koja je u bilo kom stanju koje nije STOPPED ne može biti startovana. Proces starta traje 10+ sekundi sa implementiranom vremenskom devijaciom po nahođenju.
Nakon proteklog navedenog vremena mašinu prebaciti u stanje RUNNING.

STOP
Mašina koja je u bilo kom stanju koje nije RUNNING se ne može isključiti. Proces isključivanja traje 10+ sekundi sa implementiranom vremenskom devijaciom po nahođenju.
Nakon proteklog navedenog vremena mašinu prebaciti u stanje STOPPED.

RESTART
Mašina koja je u bilo kom stanju koje nije RUNNING ne može biti restartovana. Proces restarta traje 10+ sekundi sa implementiranom vremenskom devijaciom po nahođenju.
Nakon protekle polovine navedenog vremena mašinu prebaciti u stanje STOPPED, a po završetku druge polovine prebaciti ponovo u RUNNING.

CREATE
Ova akcija je instant i služi za kreiranje nove mašine. Prilikom kreiranja mašine potrebno je proslediti sve obavezne parametre. Rezultat je mašina koja je u stanju STOPPED. 
Pri kreiranju mašine potrebno je povezati ulogovanog korisnika sa njom.

DESTROY
Destroy akcija je instant i može se izvršiti samo na mašinama koje su u stanju STOPPED. Ova akcija samo markira mašinu kao obrisanu, ali je ne briše iz baze.


Zakazivanje operacije na mašini:
Operacije START, STOP i RESTART se mogu zakazati - korisnik može da odredi datum i vreme kada će željena operacije da se izvrši. U odgovarajuće vreme, potrebno je da sistem samostalno pokuša da izvrši operaciju, ukoliko je usov za njeno izvršavanje ispunjen. Ukoliko operacija nije uspela, potrebno je to evidentirati u novoj tabeli - ErrorMessage. Entitet ErrorMessage mora da sadrži datum, id mašine, operaciju koja je bila zakazana i poruku o greški koja se dogodila.

Frontend
Na frontendu, pored grafičkog interfejsa za upravljanjem korisnicima, potrebno je implementirati dve nove stranice: 

  1.	Stranicu za pretragu mašina
  2.	Stranicu za kreiranje mašina
  3.	Stranicu sa istorijom grešaka
1. Pretraga mašina:
Implementirati da se upotrebom SEARCH-a prikažu sve mašine bez prosleđenih parametara kada se stranica učita. Pored toga, implemenitari formu iznad tabele koja sadrži potrebna polja da pokrije sve funkcionalnosti za pretragu koje nudi Backend. Filteri će se primeniti submitom te forme.
2. Kreiranje mašine
Napraviti jednostavnu formu koja će sadržati ime koje je potrebno da bi se mašina kreirala na Backend strani. Ova forma neće sadržati id, status, niti korisnika, to će Backend zaključiti sam.
3. Stranica sa istorijom grešaka
Tabela u kojoj je potrebno prikazati greške koje su se dogodile pri izvršavanju zakazane operacije. Potrebno je prikazati samo greške koje su se dogodile nad mašinama ulogovanog korisnika.
Korisnik bez specifične permisije nema pristup, ni mogućnost za izvršavanja odgovarajuće akcije na frontend-u. 
Ukoliko korisnik nema nijednu permisiju, nakon uspešnog login-a, obavestiti ga alertom. 




