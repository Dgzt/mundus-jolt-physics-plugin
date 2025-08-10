package com.github.dgzt.mundus.plugin.joltphysics.runtime.constant;

public class SaveConstants {
    // Common
    public static final String SHAPE = "shape";

    // Box shape
    public static final String BOX_WIDTH = "box-width"; // Mandatory field
    public static final String BOX_HEIGHT = "box-height"; // Mandatory field
    public static final String BOX_DEPTH = "box-depth"; // Mandatory field
    public static final String BOX_MASS = "box-mass"; // Optional field. If this field is not specified then this is a static box shape

    // Sphere shape
    public static final String SPHERE_RADIUS = "sphere-radius"; // Mandatory field
    public static final String SPHERE_MASS = "sphere-mass"; // Optional field. If this field is not specified then this is a static sphere shape

    // Cylinder shape
    public static final String CYLINDER_RADIUS = "cylinder-radius"; // Mandatory field
    public static final String CYLINDER_HEIGHT = "cylinder-height"; // Mandatory field
    public static final String CYLINDER_MASS = "cylinder-mass"; // Optional field. If this field is not specified then this is a static cylinder shape

    // Capsule shape
    public static final String CAPSULE_RADIUS = "capsule-radius"; // Mandatory field
    public static final String CAPSULE_HEIGHT = "capsule-height"; // Mandatory field
    public static final String CAPSULE_MASS = "capsule-mass"; // Optional field. If this field is not specified then this is a static capsule shape
}
