-keep class com.example.gymnasios.** { *; }

-keep class androidx.** { *; }
-keep interface androidx.** { *; }

-keep class com.google.android.material.** { *; }

-keep class com.google.firebase.** { *; }

-keep class com.google.android.gms.** { *; }

-keep class com.bumptech.glide.** { *; }

-keep class de.hdodenhof.** { *; }

-keep class com.mikhaellopez.** { *; }

-keep class com.example.gymnasios.model.** { *; }

-keepclassmembers class com.example.gymnasios.model.** { *; }

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class * implements java.io.Serializable {
  static final long serialVersionUID;
  private static final java.io.ObjectStreamField[] serialPersistentFields;
  private void writeObject(java.io.ObjectOutputStream);
  private void readObject(java.io.ObjectInputStream);
  java.lang.Object writeReplace();
  java.lang.Object readResolve();
}

-assumenosideeffects class android.util.Log {
  public static *** d(...);
  public static *** v(...);
  public static *** i(...);
  public static *** w(...);
  public static *** e(...);
}
