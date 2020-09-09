import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyPair<T,F> {
    private T first;
    private F second;

    public MyPair(T first, F second) {
        this.first = first;
        this.second = second;
    }
}
