package sciJava;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;

public abstract class SciJava extends DefaultTask {

    @Input
    abstract String getAddress();
    abstract void setAddress(String address);

    @Input
    abstract String getPomVersion();
    abstract void setPomVersion(String pom);

    @Input
    abstract String getPomBaseVersion();
    abstract void setPomBaseVersion(String pom);
}
