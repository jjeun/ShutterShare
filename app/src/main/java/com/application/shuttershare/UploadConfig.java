package com.application.shuttershare;


public class UploadConfig {

    // File upload url (replace the ip with your server address)
    public static final String UPLOAD_URL = "http://192.168.0.4/ShutterShareUpload/pictureUpload.php";

    public static final String CHECK_URL = "http://192.168.0.4/ShutterShareUpload/checkEventCode.php";

    // Directory name to store captured images
    public static final String DIRECTORY_NAME = "uploads";
}
