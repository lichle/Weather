You can built the source code or download the apk here and install it on a device to run this app

com.lichle.weather.data

## Architecture:
- MVI
	- Model: represents the state of the application
	- View: the UI component that renders the Model and displays it to the user
	- Intent: The Intent is a user action or event that triggers a state change in the ViewModel
- Clean architecture: the dependency is: View -> ViewModel -> UseCase -> Repository -> Database


## Project structure:
- Data layer: 
	- local: manages data stored on the device
	- remote: handles network operations and API interactions
	- repository: Acts as the mediator between the data sources (local and remote) and Domain layer
- Domain layer: contains usecases for execute business logic, interacts with the repository to get necessary data and transform it for the presentation layer.
- Presentation layer: deals with the user interface and user interaction
	- navigation: manages the flow between different screens or components of the app
	- screen: contains UI components and the logic associated with the visual representation of the app

## Task list:
- User Interface: Jetpack Compose, Material Design ✔
- Database: Realm ✔
- Network: Retrofit ✔
- Dependency Injection: Hilt ✔
- Security: 
	- SSL with Certificate pinning ✔
	- Hide API key in stripped *.so file ✔
- Test: Unit Test, UI Test ✔
- Others: LeakCanary, Coroutines, Flow ✔
 
