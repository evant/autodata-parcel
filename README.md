# AutoDataParcel
An Android Parcelable extensions to AutoData

## Usage
Apply the `@AutoParcel` annotation to your data object or custom annotation, then implement Parcelable.
```java
@AutoData @AutoParcel
public abstract class MyClass implements Parcelable {
  public static MyClass create() {
    return new AutoData_MyClass();
  }
}
```
