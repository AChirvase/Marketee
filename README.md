# Marketee
#### This is a demo project.


## What was aked for?

Our client needs an MVP application which will have the following functionalities (flows/steps):  
1. Listing of the targeting specifics where he is allowed to multiple choose
2. Listing of the channels which offer his selected specifics. He can select any channel  
3. Listing of the campaigns available for the channel, with monthly fees and details for each  campaign. This step will allow him to select any campaign (single selection) or reset to no  selection.  
4. Return to the previous (2) listing where he can repeat step 3  
5. Review the campaign(s) which he selected.  
6. The final step will send an email with the selected campaigns details to a bogus email  (bogus@bogus.com).  
All screens design, screen transitions, listing type are up to your imagination.  (Bonus points for retrieving the datasources from a free json hosting service) 


## How was it implemented?

### Architecture
The Marketee app is based on a **Domain Driven Architecture** (having 4 layers - Domain, Data, Framework and Presentation) with respect to **SOLID** principles. The Presentation layer, containing the ViewModel, MainActivity and Fragments, was implemented with the **MVI** pattern. The main components were injected using **Koin DI**. The app was written in **Kotlin**.
The Architecture and the dependency tree is depicted below:

<pre>

Presentation Layer           ->  |  Data Layer   ->  |  Domain Layer              | <-  Framework Layer    

Fragments  ------> ViewModel -----> Repository ------>  Business Logic Classes  <----- Local DataSource
    ^                ^                                           ^                                        
Activity   ----------|                                           |-------------------- Remote DataSource 

</pre>


### Database
RemoteDataSource was implemented with Retrofit and it fetches the data from a free JSON hosting platform. LocalDataSource was implemented using Room. This allows a LiveData flow between Framework Layer and Presentation Layer. For the LocalDataSource, the Framework layer has it's own model classes (@Entities for Room Db)to preserve the Dependency Inversion principle (so we don't transform the business logic classes in the Domain layer into @Entities).

### Repository
Depends on abstractions of Remote and Local DataSources.

### ViewModel
The ViewModel is injected and shared between Fragments and Activity. It holds multiple MutableLiveData fields that aid the communication between Fragments and Activity. It handles all the decisional logic so Activity and Fragments handle only the actual data display.

### Activity
The Activity can have multiple states as described by the MVI pattern. Based on this states it displays specific Fragments and updates it's layout fields. For logic inside onBackPressed() or onMainActivityFabPressed() it calls the viewModel.




                                                                                                          
                                      
