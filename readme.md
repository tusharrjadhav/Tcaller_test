Application Architecture

src
├─ di (Providers are mention)
├─ Repository
   ├─ api/ NetworkService (Retrofit network call)
   └─ DataRepository (Business Logic UI data)
├─ screens
   ├─ ContentFragment (UI with button and output textView)
   ├─ DataModel (BaseObservable with Bindable data variable)
   └─ MainScreenViewModel (View model to get UI data)
├─ use_cases / GetDataUseCase (Fetching data )
├─ Util (Utilities classes)
├─ BaseApplication (Core application context)
├─ MainActivity
├─ StringOperationsTest (Unit test for testing string operation)
└─ TestApp (Incomplete UI test)

Assignment Output

1. Truecaller10thCharacterRequest:
   For this point, returned the 10th Character of the html response received which with given url returns html response as <!DOCTYPE  so 10th character output is a space character“ ”

2. TruecallerEvery10thCharacterRequest:
   For this point, returned every 10th, 20th, 30th ect Character in array.

3. TruecallerWordCounterRequest:
   For this point, returned the  words count.

Code summary ::

Use the coroutines with Retrofit lib.
Use the Hilt lib.
Tried to use the google recommended architecture, reference from https://github.com/android/architecture-samples
Unit test is write testing string operation StringOperationsTest.
All network related calls has been decoupled from modules. Its being used for code extensiblity.

Thinks not able to complete ::

Tried to create UI using Compose.
Tried to write UI test in TestApp

