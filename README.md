# Adding External Libraries

For some reason, the project doesn't let VSCode easily add external libraries. What seems to work 
is this:
- Add the jar file to the `Steelcase/lib` directory
- Edit the `Steelcase/.classpath` file and add an entry in the `<classpath>` element that follows this syntax:
```
      # add this v
      <classpathentry exported="true" kind="lib" path="lib/<your_jar_here>.jar"/>
```

# Connecting to the Database

## Testing Your Connection

The database is hosted on a Google Cloud SQL instance, and in order to connect to it you need to authentice with it. To test your connection, run the DBConnectionTest.java class. A large block of text will print, but you can ignore most of it. On success, the last two lines will say it worked. On failure, a stacktrace will print out (contact me, Ethan, and I'll work out the problem).

### Authentication Credentials

Currently, the connection uses End User Credentials, and because nobody other than I (Ethan) have it tested at the time of writing, it may not work for you guys. I plan on eventually switching to a Service Account which is what Google recommends, which should fix any issues with the credentials themselves.

### Switching to a Service Account

When this happens, I'll share a json file with everyone but **under no circumstances should it go to the repo,** and I've already updated .gitignore to, well, ignore it.


