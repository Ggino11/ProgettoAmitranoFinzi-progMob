# Vimata - Fitness Training App

Vimata is a fitness management application designed to facilitate seamless communication between trainers and athletes. This app allows trainers to create workout plans and tests. Both parties can communicate effectively through a built-in chat system that generates new chats when athletes add their traineres. The app is currently a basic tool requiring offline contact between athletes and trainers, but future updates will transition this communication online using links or QR codes.

## Features

### LOGIN & REGISTRATION
- Users can register as athletes or trainers, based on this type the application offers different funcionalities.
- The authentication process was implemented using firebase Auth and firestore for storing data
- Password for semplicty during testing are stored in the database, in the feature will be hashed or managed by firebase Auth

### Trainer Features
1. **Profile Management**
    - Trainers can securely log out.

2. **Test Creation and Management**
    - Trainers can create new test sets by filling out a form with details such as title, status, and associated athlete.
    - Tests are stored in Firebase Firestore, and video files are uploaded to Firebase Storage.

3. **Athlete Management**
    - Trainers can assign tests and workout plans to athletes.
    - Trainers can view athlete profiles and track their progress.

### Athlete Features
1. **Profile Management**
    - Athletes can add new coaches using their email (offline contact required).
    - Athletes can securely log out.

2. **Test Tracking**
    - Athletes can view assigned Test and workout plans.
    - Athletes can log their workout and test progress.
    - We focused mainly on test sets 

### Communication Features
1. **Chat System**
    - Trainers and athletes can communicate through a built-in chat system.
    - Messages are sent and received in real-time using Firebase Firestore.
    - Functions for deleting messages or chats weren't implemented yet. 

## How to Use the App

### Getting Started

1. **Login**
    - Trainers and athletes can log in using their email and password.

2. **Profile**
    - Once logged in, users can view their profile information.
    - Editing personal details will be implemented in the future.
    - Use the log out button to securely sign out of the app.

### For Trainers

1. **Create a Workout**
    - Click on the desired athlete and follow the steps to create a workout or a test set.
    - Currently, the focus is on tests; workouts simulate the sending process.

2. **Manage Workouts**
    - View and manage your created workouts from the main screen.
    - Assign workouts to athletes and track their progress.

3. **Communicate with Athletes**
    - Use the chat feature to communicate with athletes.
    - Select a chat to view messages and send new ones.

### For Athletes

1. **View Assigned Workouts**
    - Navigate to the workout section to view workouts assigned by your trainer.
    - Log your progress and provide feedback.
    - Athletes currently see dummy workouts assigned by trainers; the focus is on tests.

2. **Communicate with Trainers**
    - Use the chat feature to communicate with your trainer.
    - Select a chat to view messages and send new ones.

## Future Features

1. **Enhanced Workout Logging**
    - Enable athletes to log individual exercises within a workout, track sets, reps, and weights used.

2. **Progress Tracking**
    - Provide visual progress tracking for athletes and trainers.
    - Include charts and graphs to visualize improvements over time.

3. **Notifications**
    - Implement push notifications for new messages, workout assignments, and other important updates.
      
4. **Uploading Workout PDFs and PDF Viewer**
    - Allow trainers to upload and athletes to view workout PDFs.

5. **Exercise Collection**
    - Build a collection of exercises that can be added to workouts.

6. **Adding Users by Link or QR Code**
    - Simplify adding users by generating and scanning QR codes or sharing links.
7. **UI Design**
   -  UI will be improved by follwing a proper design 
## Technical Details

### Firebase Integration

- **Firestore**: Used for storing user profiles, tests, and chat messages.
- **Firebase Storage**: Used for storing uploaded workout plans (PDF functionality to be implemented).
- **Authentication**: Firebase Authentication is used for secure user login and management.

### Project Structure

- **ui/screen**: Contains the composable functions for different screens such as `TrainerProfileScreen`, `AthleteProfileScreen`, and `SingleChatScreen`.
- **ui/components**: Contains reusable UI components.
- **data/model**: Contains data classes for `Workout`, `Message`, and `User`, etc.
- **viewmodel**: Contains ViewModel classes for managing UI-related data in a lifecycle-conscious way.
- **data/repository**: Contains classes for data operations and connections with Firestore and Firebase Storage.

## Setup and Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/yourusername/vimata.git
    cd vimata
    ```

2. Open the project in Android Studio.

3. Set up Firebase:
    - Add your `google-services.json` file to the `app` directory.
    - Ensure that Firestore, Firebase Storage, and Authentication are enabled in your Firebase project.

4. Build and run the app on your emulator or physical device.

## License

This project is licensed under the MIT License - see the LICENSE file for details.
