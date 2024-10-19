## How to Run
1. Clone the project down to your computer.
2. Open it in Android Studio (Android Studio Koala or newer are recommended)
3. To build debug version you just need to click on the play button on Android Studio.
4. Or just play with this [**apk**](https://github.com/lichle/Weather/blob/main/docs)


## Architecture
- Clean architecture: the dependency is: View -> ViewModel -> UseCase -> Repository -> Database
- Unidirectional MVVM:
	- Model: represents the data layer, typically responsible for business logic, data access, and network or database operations.
	- View: represents the user interface (UI) that displays data and receives user inputs (e.g., buttons, text fields).
	- ViewModel: Acts as a bridge between the View and the Model. It manages the UI-related logic, transforms data from the Model, and exposes it to the View. It also handles user actions and updates the Model or its internal state accordingly.

Why Unidirectional MVVM?
**Unidirectional MVVM**: The flow of data in one direction— from the Model to the ViewModel to the View and back—ensures that the application’s state is predictable. Since state changes always flow in one direction, it’s easier to track how data moves through the system, which reduces the likelihood of bugs.
**Traditional MVVM**: In traditional MVVM, especially with two-way data binding, state changes can happen in multiple directions, often simultaneously. The View can directly modify the ViewModel or even the Model, making it harder to trace where the state is being changed. This can lead to unpredictable behavior and harder-to-debug issues.

## Project structure
- Data package: 
	- local package: manages data stored on the device
	- remote package: handles network operations and API interactions
	- repository folder: Acts as the mediator between the data sources (local and remote) and Domain layer
- Domain package: contains usecases for execute business logic, interacts with the repository to get necessary data and transform it for the presentation layer.
- View package: deals with the user interface and user interaction
	- navigation package: manages the flow between different screens or components of the app
	- screen package: contains UI components and the logic associated with the visual representation of the app


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
 
