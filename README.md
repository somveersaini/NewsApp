
## News App MVI
### Using MVI clean Architecture, Data binding, JetPack Navigation/SafeArgs 
A two-screen (news view + details view) app using  [NewsAPI.org](https://newsapi.org/docs/endpoints/top-headlines) top-headlines API  

### Description  
 1. Home screen -> Contains scrollable __vertical list__ for __Article Items__ and a scrollable __horizontal list__ for __Category Items__ at bottom of screen. Support for swipe-to-refresh and error showing.  
 2. Details page -> shows __details__ of particular News Article item. Navigates to __full news article__ in web/chrome.  
  

### Things to look for
* MVI Architecture (intent -> action -> result -> viewstate) unidirectional flow and state machine
* JetPack Navigation component / Nav Graph / SafeArgs
* Databing with binding adapters
* Clean Architecture
* Offline/Caching approach using RxJava and Room DB
* Dagger2 for dependency injection  
  
  
### NewsApi.org top-headlines API  
 * Documentation: [https://newsapi.org/docs/endpoints/top-headlines ](https://newsapi.org/docs/endpoints/top-headlines)
  
  
### Libraries used  
  
* Android architecture component  
* OkHttp and Retrofit for networking  
* RxJava & RxAndroid  
* Dagger2 for dependency injection  
* Room DB for caching  
* Glide imaging loading  
* JetPack Navigation SafeArgs  
  
  
Developed By __Somveer Saini__

[somveersaini1234@gmail.com](somveersaini1234@gmail.com)
  
[https://play.google.com/store/apps/developer?id=Somveer+Saini](https://play.google.com/store/apps/developer?id=Somveer+Saini)