# RememberThings


Aplicație care sa înlocuiască clasicul post-it. După logarea în aplicație, putem crea notițe care să ne amintească diverse lucruri. Aceste notițe sunt salvate în baza de date, ele putând fi accesate oricând. De asemenea, putem consulta vremea pe București ce se actualizează în timp real. 

Ea implementează următoarele: 

1.	O operație cu camera – fotografie: fiecărei notițe îi putem atașa și o imagine; 
2.	Recycle View cu funcție de căutare: în activitatea main putem căuta notițe; 
3.	Navigation Drawer: ce înglobează acțiunea de LogOut, afișează utilizatorul logat prin serviciul Google și afișează vremea din București; 
4.  O metodă de share – Android share: notițele noastre pot fi share-uite; 
5.  Social Login – Google: autentificarea în aplicație se realizeaza cu un cont de Google; 
6.  UI adaptat pentru landscape mode – întrega aplicație merge orientată și în landscape; 
7.  Persistența datelor folosind Realm: notițele sunt salvate, împreuna cu pozele, în baza de date locală; 
8.  Web service – Retrofit: aplează Weather API (https://openweathermap.org/); 
9.  Dependency injection – Dagger: folosit pentru injectarea Retrofit în MainActivity; 
10. Picasso: librărie folosită pentru manipularea imaginilor și descărcarea lor de pe web (iconița corespondentă stării meteo).


Pentru rularea aplicației este nevoie de Android Studio și Virtual Android Machine.
