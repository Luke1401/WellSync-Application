# ARCHITECTURE

## 3-tier architecture

### Domain Specific Objects (DSO) 
Objects that represent data that is passed around between the layers.<br> 
- **UserCredentials:** Consists of the  username and password used during the login process to verify and authenticate a user's identity.<br>
- **DailyLog:** Corresponds to a single day's input from the patient health trackers, including mood rating, hours of sleep, and lists of symptoms, medications, and substances used.<br>
- **Patient:** Includes patient's personal information like name, age, blood type, and any other relevant personal data.<br>
- **Doctor:** Includes doctor's name and the list of patients they have.<br>

### Presentation Layer
Responsible for handling the user interface (UI) and user experience (UX). It is the part of the system that users directly interact with, providing a means for them to input data, receive information, and interact with the application's features.<br>
- **LoginActivity:** Page that will allow user to login or to sign up either as a patient or as a doctor, and will retrieve user credentials for its verification.<br>
- **HomePageActivity:** Main screen of the app for patients, providing users with an overview of the 4 different trackers and an option for user datails.<br>

#### Patient users only
- **UserDetailsActivity:**  Provides the user interface for managing personal information.<br>
- **MoodTrackerActivity:** Allows users to input and visualize their mood. It would likely include graphical representations to show mood trends, an option to input the hrs of sleep and one for notes<br>
- **SymptomTrackerActivity:** Allows users to input and track the frequency of any symptoms they are experiencing. The app could possibly provide insights or trends.<br>
- **MedicationTrackerActivity:** Allows users to input and track their medication information, such as name of medications, doses or side-effects.<br>
- **SubstanceUseTrackerActivity:** Allows users to input how often they use any substances, allowing them to describe what you used and how you felt on those days.<br>

#### Doctor users only
- **DoctorPageActivity:**  Home page for doctor users, lists their patients and allows the addition of new patients.<br>
- **PatientInfoActivity:**  Displays the personal information of one of a doctor's patients.<br>

### Logic Layer
Responsible for implementing the business rules, processing data, and coordinating the flow of information between the user interface (presentation layer) and the data storage (persistence layer). The Logic Layer contains the core functionality and rules that define how the application operates.<br>
- **UserAuthenticationHandler:** Verify user credentials, and provide access to the app's secured features upon successful authentication.<br>
- **PatientHandler:**  Manages and retrieves patients' personal information.<br>
- **DoctorHandler:**  Manages and retrieves doctors' information, including each of their lists of patients.<br>
- **DailyLogHandler:** Handles the recording and retrieval of patients' health journal entries. May include logic for analyzing and presenting trends or insights based on health data.<br>

### Data/Persistence Layer
Responsible for managing the storage and retrieval of data. The primary role of the Persistence Layer is to abstract the details of data storage, providing a clean and consistent interface for the Logic Layer to interact with data.<br> 
- **UserPersistence:** Responsible for storing and retrieving information related to user registrations. It may include details such as usernames, passwords, email addresses, users details. Interfaces with the PatientHandler, DoctorHandler, and UserAuthenticationHandler from the Business Logic Layer, to ensure accurate and secure handling of user registration data.<br>
- **DailyLogPersistence:** This component deals with storing and retrieving mood-related information logged by users. It interfaces with DailyLogHandler to organize and store each patient's logs.<br>

## Iteration 3 Diagram
Note that some minor presentation classes that do not read/write data are omitted from the diagram.

For clarity, the addition of `DailyLogPatientActivity` in Iteration 3 is for doctors to view the daily logs of their patients.

![architecture](architecture-3.png)

## Iterations 2 & 3 Diagram

![architecture](architecture-2.png)

## Iteration 1 Diagram

![architecture](architecture-1.png)
