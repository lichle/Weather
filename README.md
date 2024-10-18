## How to Run
1. Clone the project down to your computer.
2. Open it in Android Studio (Android Studio Koala or newer are recommended)
3. To build debug version you just need to click on the play button on Android Studio.
4. Or just play with this [**apk**](https://github.com/lichle/Weather/blob/main/docs)


## Architecture
- MVI
	- Model: represents the state of the application
	- View: the UI component that renders the Model and displays it to the user
	- Intent: The Intent is a user action or event that triggers a state change in the ViewModel
- Clean architecture: the dependency is: View -> ViewModel -> UseCase -> Repository -> Database


## Project structure
- Data folder: 
	- local folder: manages data stored on the device
	- remote folder: handles network operations and API interactions
	- repository folder: Acts as the mediator between the data sources (local and remote) and Domain layer
- Domain folder: contains usecases for execute business logic, interacts with the repository to get necessary data and transform it for the presentation layer.
- View folder: deals with the user interface and user interaction
	- navigation folder: manages the flow between different screens or components of the app
	- screen folder: contains UI components and the logic associated with the visual representation of the app


## Task list
- User Interface: Jetpack Compose, Material Design ✔
- Database: Realm ✔
- Network: Retrofit ✔
- Dependency Injection: Hilt ✔
- Security: 
	- SSL with Certificate pinning ✔
	- Hide API key in stripped *.so file ✔
- Test: Unit Test, UI Test ✔
- Others: LeakCanary, Coroutines, Flow ✔
 
