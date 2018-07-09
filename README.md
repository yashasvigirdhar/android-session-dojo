# dojo-android-session
This repository contains the demo project that I have used for the android session that I took in Dojo Bali.
The targeted audience for the session are the people who are completely new to android.


I have covered the following topics :

- Activities
- Intents
- Services
- Broadcast Receiver
- Recycler View
- Storage in Android
- floating action button
- Permissions


The repository contains different branches, each corresponding to a particular functionality. So, if you want to see the code changes for a particular functionality, you can just see the diff between this branch and the its previous branch. 

Here's the order in which I have prepared the branches :

1. *helloworld* : basic hello world project
2. *listview* : added a simple listview of cities
3. *addCity* : added a floating action button to add a new city to the list
4. *addDatabase* : integrated SQLite database, so that the cities storage is persistent.
5. *addService* : syncing the cities list with the server using an IntentService.

So, for example, if you want to see how to sync cities with server when you have db already set up, you can run :

`git diff addDatabase..addService`

Feel free to report any issues.

Cheers !


