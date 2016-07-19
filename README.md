# CharacterViewer

Technology choices:

- RecyclerView was used because it is a flexible way of displaying items in a list without wasting resources.

- CardView was used for the list items because it visually separates them in a way that is obvious and familiar to the user.

- Retrofit was used for data transfer from the web service, because it calls for concise code and it works efficiently.

- ButterKnife was used to make the view initialization code more concise.

- Glide was used to make image loading both more reliable and more resource-efficient than with the built-in APIs.

- Bound services to separate the data loading code from the UI, Java Threads to run it elsewhere than in the UI thread, and the Messenger/Handler API to send the data to the UI thread.

- Android resources API, to avoid hardcoding margin and padding sizes in the layouts.

- WebView, to re-use existing HTML documents inside the Android app instead of remaking them from scratch.# CharacterViewer
