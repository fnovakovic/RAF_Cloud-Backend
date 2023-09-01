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
