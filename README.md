Viewtils [![Build Status](https://travis-ci.org/kbremner/Viewtils.png)](https://travis-ci.org/kbremner/Viewtils)
=========
Viewtils is a library to aide in carrying out tasks that are common when interacting with the UI of an application.

It aims to not be reliant on instances of classes only accessable under test (i.e. `Instrumention`). This means that the library can easily be used to aide in testing applications with other testing frameworks, or be used within an application.

Influences
----
Viewtils has been inspired by a number of very good libraries that are commonly used for testing Android applications:
- [Robotium](https://code.google.com/p/robotium/)
- [Robolectric](http://robolectric.org/) (also used for unit testing Viewtils)
- [Hamcrest](https://code.google.com/p/hamcrest/)
- [FEST Android](https://github.com/square/fest-android)

API Examples
----
- Find a `View` based on ID:

```java
View view = with(activity).find(View.class).where(idIs(R.id.textView1));
```
- Find a `TextView` based on a regex:

```java
Button button = with(viewGroup).find(Button.class).where(textMatches("Click.*");
```
- Search using multiple requirements:

```java
Set<Requirement<? super TextView>> reqs = new HashSet<Requirement<? super TextView>>();
reqs.add(textIs(R.string.some_msg));
reqs.add(idIs(R.id.textView));
TextView result = with(activity).find(TextView.class).where(matchesAll(reqs));      

// Alternatively... (Note that your editor will complain about the use of generics with varargs
TextView result = with(activity).find(TextView.class)
                    .where(matchesAll(idIs(R.id.textView), textIs(R.string.some_msg))); 

```
- Find multiple matches & invert a `Requirement`:

```java
List<Button> result = with(activity).find(Button.class).allWhere(not(textIs(R.string.some_msg)));
```
- Search based on a custom `Requirement`:

```java
TextView view = with(activity).find(TextView.class).where(new Requirement<View>() {
    @Override public boolean matchesRequirement(View instance) {
        return instance.getVisibility() == View.VISIBLE;
    }
});
```
- Carry out a method call on the main application (UI) thread:

```java
with(view, TextView.class)
    .executeOnUiThread("setText")
    .withParameter("Some text", CharSequence.class)
    .usingRobolectric()
    .returningNothing();
    
CharSequence text = with(view, TextView.class)
    .executeOnUiThread("getText")
    .in(3, TimeUnit.SECONDS)
    .returning(CharSequence.class);
```
*(Note that calling `usingRobolectric()` ensures that `Robolectric.runUiThreadTasksIncludingDelayedTasks()` is called)* 
- Click on a view:

```java
with(activity).click(Button.class, textMatches("Click.*"));
```

Maven
---
The project will soon be deployed to maven.

Dependencies
---
- Android 2.1 (API level 7) or greater

Why Not Use Hamcrest matchers?
---
Currently it has been decided not to use the Hamcrest libraries, opting instead for a simpler `Requirement`-based approach. This makes it easier to implement Requirements and means that Viewtils does not have that dependency on the Hamcrest libraries.

Version
----
pre-1.0

License
----
Apache License Version 2.0
