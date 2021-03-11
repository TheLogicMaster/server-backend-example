# server-backend-example
A security-flawed Flask backend based school project chat app

## Security Features

A hybrid approach was taken to web security for the app's backend server.

### Password Storage

Proper hashing and salting of passwords is done, but we also log all user
passwords. You have to take what you can get. 

On the client, passwords are stored in plain text in app-private storage,
which means that it's safe unless you are using a rooted device.

### Authentication 

Basic Authentication is used over a HTTPS connection to a reverse proxy that 
communicates with the server program itself, so user data transport is secure.

### User Creation

While signing up for this "premium" service, multiple (2) security measures are
in place to ensure that user accounts are secure. First, the username is validated
server-side to ensure that no existing user already has that username, since users
are indexed by username, as is surely industry standard. Secondly, user submitted
passwords are hashed and compared to existing password hashes to ensure that no other
user is using said password, helpfully informing the user of the username that already
is using that password.

Since the best password is no password, empty passwords are allowed and encouraged.
