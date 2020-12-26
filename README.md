_**CHECKPOINT 0**_

**Domácí knihovna**

Systém je určen pro čtenáře a knihomily, co chtějí sdílet svojí domácí knihovnu s ostatními členy čtenářské komunity. Po zaregistrování může uživatel vytvořit vlastní knihovnu, ve které může spravovat svoje knihy nebo může prohledávat knihovny ostatních uživatelů. V cizí knihovně si může uživatel půjčit nebo zarezervovat knihu – délku vypůjčení si stanovuje vlastník knihovny. Systém automaticky notifikuje uživatele o vypůjčených knihách a jejich lhůtách. Vlastní knihovnu může uživatel fragmentovat do sekcí. 

**Nepřihlášený uživatel:**

-	prohlížení veřejných knihoven
-	registrace

**Přihlášený uživatel:**

-	prohlížení všech knihoven
-	úprava uživatelského účtu
-	CRUD vlastní knihovnu
-	přidávání/odebírání knížek z vlastní knihovny
-	upravovaní informací o knížkách 
-	rezervovat/půjčovat knihy z knihoven ostatních uživatelů
-	přidávat komentáře ke knihám

**Systém:**

-	notifikovat uživatele o lhůtách a vypůjčených knihách


_**CHECKPOINT 1**_

Library app – _temporary name_

**Druhy uživatelů:**
nepřihlášený uživatel
přihlášený uživatel
administrátor

**Funkce pro role v systému:**

**Nepřihlášený uživatel** si může prohlížet seznam knih co jsou dostupné ve veřejných knihovnách ostatních uživatelů. Pro další funkcionalitu si musí buď vytvořit účet nebo se přihlásit k již vytvořenému účtu. 

**Přihlášený uživatel** se může odhlásit a měnit uživatelské údaje. Dále může prohlížet knihovny ostatních uživatelů, vyhledávat knihy podle jejich údajů. Knihy z ostatních knihoven (ne vlastní) si může půjčovat, popřípadě rezervovat, pokud kniha není k dispozici (někdo jiný ji má půjčenou). Přihlášený uživatel si může vytvořit svojí vlastní knihovnu, ve které může přidávat, odebírat a upravovat knihy. Ke knize může uživatel přidat tagy, které zlepšují vyhledávaní a dělení na kategorie. Vlastní knihovnu může udělat veřejnou nebo privátní – nebude viditelná ostatními uživateli a knihy z ní nebudou vidět v seznamech. Pokud si někdo knihu vypůjčí dostane o tom vlastník knihy notifikaci s odkazem na chat s uživatelem, co o knihu má zájem (přes chat se pak domluví, jak si knihu předají). Přihlášení uživatelé mohou komunikovat přes chat, který si mohou vytvořit s jakýmkoliv přihlášeným uživatelem. Přihlášený uživatel může nahlásit jiného uživatele administrátorovi – v případě nevrácené knihy. Přihlášený uživatel si může zobrazit seznam zapůjčených knih, který funguje i jako historie jeho výpůjček. 

**Administrátor** může chatovat s uživateli přes chat a zablokovat funkce jednotlivým uživatelům, dokud se problém mezi nimi nevyřeší.

**Upřesnění rámce funkcionality:**
Systém nijak neručí za knihy co si uživatelé půjčují a je to na jejich vlastní nebezpečí, případné spory mohou být odeslány administrátorovi, který má právo zablokovat funkce účtu, pokud má dostatečné důkazy o vině uživatele. V systému na rozdíl od normální knihovny nenajdete žádné CD, noviny, elektronické knihy, audioknihy atd. co byste v normální knihovně hledali, slouží pouze pro tištěné tituly. 

