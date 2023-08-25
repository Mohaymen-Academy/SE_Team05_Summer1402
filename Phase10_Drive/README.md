# About

This project is a File Server application (like Google Drive) implemented using Spring Boot framework and H2 database
with Java.

# Features

You can upload your files easily using the implemented UI. You can also see your files, download them, or delete them
from the server. If you're bored just roll the dice!

# Usage

1. Upload your files from the `Upload` option on the bar. You can also use the `/files/{id}` request with the `POST`
   method.
2. See all your files from the `Files` option on the bar or just use the `/files` request.
3. See a specific file of yours using `/files/{id}` request with the `GET` method.
4. Download a file using `/download/{id}` request.
5. Delete your files from the server using `/files/{id}` request with the `DELETE` method.
6. Roll the dice from the `Dice` option on the bar or just use the `/dice` request!
