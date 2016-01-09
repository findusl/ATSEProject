appengine-skeleton
=============================

This is a generated application from the appengine-skeleton archetype.

## Setting Up & Running the Application
1. On https://console.developers.google.com, a new application called *transport4you-1185* has been created.
2. The application's ID has been inserted into `/src/main/webapp/appengine-web.xml`
3. Running `mvn clean install` installs all dependencies, progress can be controlled by running the local devserver `mvn appengine:devserver`
4. The application has been deployed to GAE by running `mvn appengine:update`. After that, it seemed necessary to run `mvn appengine:update_indexes` and clear the application's cache at https://console.developers.google.com/appengine/memcache?project=transport4you-1185
5. The application is available under https://transport4you-1185.appspot.com/