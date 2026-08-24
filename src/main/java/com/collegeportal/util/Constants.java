package com.collegeportal.util;

import java.util.Set;

public class Constants {

    public static final String[] DEPARTMENTS = {
        "Computer Science",
        "Electronics & Communication",
        "Mechanical Engineering",
        "Civil Engineering",
        "Electrical Engineering",
        "Information Technology"
    };

    // Student profile image upload settings
    public static final String UPLOAD_DIR = "C:/collegeportal-uploads/students/";

    public static final long MAX_FILE_SIZE = 2 * 1024 * 1024;

    public static final Set<String> ALLOWED_IMAGE_TYPES = Set.of(
        "image/jpeg",
        "image/png",
        "image/webp"
    );
}