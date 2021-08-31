# ShowTime
## Great app to be up-to-date with your favourite movies and tv shows, search for a movie or tv show you like and even get similar and related movies or tv shows. With modern UI and great user experience.


## Tech stack & Open-source libraries
- Minimum SDK level 21
- 100% [Kotlin](https://kotlinlang.org/) based, [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) together with [Flow](https://developer.android.com/kotlin/flow) for asynchronous streams 
and one side viewModel to fragment communication.
- Dagger Hilt for dependency injection.
- [Retrofit](https://square.github.io/retrofit/) A type-safe HTTP client for Android and Java
- [Glide](https://github.com/bumptech/glide) for loading images.
- JetPack
  - Lifecycle - Dispose of observing data when the lifecycle state changes.
  - ViewModel - UI related data holder, lifecycle aware.
  - ViewBinding - Interact with XML views in safeway and avoid findViewById() 
  - Room - Persistence library that provides an abstraction layer over SQLite database (Used for caching upcomming feature)
  - Navigation Component - Make it easy to navigate between different screens and pass data in type-safe way
- Architecture
  - MVVM Architecture (View - ViewModel - Model) together with Events that decide what Fragment or Activity should do
  - Repository pattern
- [Material-Components](https://github.com/material-components/material-components-android) - Material design components like cardView
- SaveStateHandler to handle process death
