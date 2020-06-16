# Movies Database

## Configure

To start, it's necessary create a Firebase project.
Go to https://console.firebase.google.com/, create a new project with applicationId (com.guilhermelucas.moviedatabase). Download the generated google-services.json file and copy it to the app/ directory.

## Remote config

The following variables can be changed on Firebase Remote Config

| Variable | Default |
| --- | --- |
| server_url | https://api.themoviedb.org/3 |
| api_key | 1f54bd990f1cdfb230adb312546d765d |
| request_default_language | pt-BR |
| request_default_region | BR |
| request_ignore_user_localization | true |
| poster_url | https://image.tmdb.org/t/p/w154 |
| backdrop_url | https://image.tmdb.org/t/p/w780 |

- request_ignore_user_localization = When this variable is true, user localization and language will be ignored and the app only return upcoming films on Brazil and movie details are in brazilian portuguese  