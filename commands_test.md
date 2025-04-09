Skip tests when running Gradle commands. Here are several ways to do it:

### **1. Skip Tests for a Single Command**
Add `-x test` to your command:
```bash
./gradlew build -x test
```

### **2. Skip Tests Permanently (in build.gradle)**
Add this to your `build.gradle`:
```groovy
tasks.withType(Test) {
    enabled = false
}
```

### **3. Only Build Without Tests**
Use the `assemble` task instead of `build`:
```bash
./gradlew assemble
```

### **4. Skip Tests Conditionally**
Use a property to control test execution:
```groovy
test {
    onlyIf { !project.hasProperty('skipTests') }
}
```
Then run:
```bash
./gradlew build -PskipTests
```

### **5. Skip Specific Test Tasks**
```bash
./gradlew build --exclude-task test
```

### **Key Differences**
| Command | Behavior |
|---------|----------|
| `./gradlew build -x test` | Builds everything except tests |
| `./gradlew assemble` | Only builds artifacts, no tests |
| `./gradlew check` | Runs tests without building artifacts |
| `./gradlew clean build -x test` | Clean build without tests |

### **When You Might Want This**
1. When you're only changing non-test code
2. During dependency resolution
3. When you need a quick build
4. In CI pipelines where tests run separately

### **Important Note**
The `build` task always depends on `check` (which includes `test`), which is why tests 
run by default. Using any of these methods will properly skip the test execution phase 
while still performing other build steps.

