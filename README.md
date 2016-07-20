# CharacterViewer

Technology choices:

- RecyclerView because it is a flexible way of displaying items in a list without wasting resources.

- CardView for the list items because it visually separates them in a way that is obvious and familiar to the user.

- Android resources API, to avoid hardcoding margin and padding sizes in the layouts.

- WebView to re-use existing HTML documents inside the Android app instead of remaking them from scratch.

- Bound services to separate the data loading code from the UI, Java Threads to run it elsewhere than in the UI thread, and the Messenger/Handler API to notify the UI thread of about new data.

- Fragments to lay out the main areas of the UI differently depending on whether the client device is a phone or a tablet, without duplicating UI code.

- Animations to smoothly transition between list and detail displays.

- Parcelable to allow data items to cross between threads and processes.

- Retrofit for data transfer from the web service, because it calls for concise code and it works efficiently.

- ButterKnife to make the view initialization code more concise.

- Glide to make image loading both more reliable and more resource-efficient than with the built-in APIs.