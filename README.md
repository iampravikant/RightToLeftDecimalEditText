[![MIT Licence](https://badges.frapsoft.com/os/mit/mit.svg?v=103)](https://opensource.org/licenses/mit-license.php)

# RightToLeft Decimal EditText
EditText which allows to append decimal numbers from right to left

# Example

![alt text](https://media.giphy.com/media/3gPFh76IrjJPx9QkrY/giphy.gif "Example")

# Usage

**Step 1**: Add `RightToLeftDecimalEditText` in XML

```xml
<io.pravikant.rtledittext.RightToLeftDecimalEditText
        android:id="@+id/main_weight"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Weight"
        app:rtl_decimal_points="3"
        app:rtl_decimal_value="0.000" />
```
**Step 2** *(Optional)*: Define `decimalPoints` and `decimalValue` in code

```kotlin
main_weight.setDecimalPoints(3)
main_weight.setDecimalValue(BigDecimal(56))
```

**Step 3** *(Optional)*: Define `nextFocusableView` and `listener` in code

```kotlin
main_weight.setNextFocusableView(main_width)
main_width.setNextFocusableView(main_height)
main_height.setListener(object : RightToLeftDecimalEditText.RightToLeftDecimalEditTextListener {
    override fun onKeyboardEnterClicked() {
        onSubmit()
    }
})
```

**Step 4**: Get the `BigDecimal` value

```kotlin
main_weight.getDecimalValue()
```

<h2>Attributes</h2>

```xml
<attr name="rtl_decimal_points" format="integer" />
<attr name="rtl_decimal_value" format="float" />
```

# Download

<h4>Gradle:</h4>

```gradle
repositories {
  mavenCentral()
  google()
}

dependencies {
  implementation 'io.iampravikant:rtledittext:1.0.0'
}
```
Note: Don't forget to add `mavenCentral()` under `repositories` in root-level `build.gradle`
 
<h4>Maven:</h4>

```xml
<dependency>
  <groupId>io.iampravikant</groupId>
  <artifactId>rtledittext</artifactId>
  <version>1.0.0</version>
  <type>aar</type>
</dependency>
```

# Author
Pramod Ravikant

[![alt text][1.1]][1]
[![alt text][2.1]][2]
[![alt text][3.1]][3]

[1.1]: http://i.imgur.com/tXSoThF.png (twitter icon with padding)
[2.1]: http://i.imgur.com/P3YfQoD.png (facebook icon with padding)
[3.1]: http://i.imgur.com/0o48UoR.png (github icon with padding)

[1]: http://www.twitter.com/iampravikant
[2]: http://www.facebook.com/iampravikant
[3]: http://www.github.com/iampravikant

# Licence
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
