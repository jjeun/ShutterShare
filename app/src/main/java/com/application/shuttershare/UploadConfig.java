package com.application.shuttershare;

/*
* Authors: Jesse Jeun
* Date: 10-19-2015
* Description: Class that hold server connection information.
*       Can change the addresses here to hit different servers and php's that will
*       be used to access the database.
*/


public class UploadConfig {

    // File upload url (replace the ip with your server address)
    public static final String UPLOAD_URL = "http://52.27.86.208/pictureUpload3.php";

    public static final String CHECK_URL = "http://52.27.86.208/checkEvent2.php";

    // Directory name to store captured images
    public static final String DIRECTORY_NAME = "uploads";
}
