# WeatherDemo
A weather App for AV Devs final round task

The App show weather info from OpenWeatherMap Api: https://api.openweathermap.org/data/2.5/weather  

Uses MVVM architecture with 3 layers: Data, Domain and Ui to keep code clean and layered. Interactors/Usecases are not used due to simplicity
Uses Room Db for maintaining local cache
Uses a LocationHelper helper class for managing location permissions and showing appropriate info dialogs to user.  
Uses GooglePlay Location Apis for scanning user location

Ui is kept simple due to time constraints.  

Due to simplicity of the app, all remote operations are performed using common RemoteRepositorySource and local operations are performed using LocalRepositorySource.
