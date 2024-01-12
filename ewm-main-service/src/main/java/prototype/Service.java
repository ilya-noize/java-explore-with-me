package prototype;

import java.util.List;

public interface Service<T, T1> {
    T create(T1 newDto);

    T update(Long id, T1 newDto);

    T get(Long id);

    List<T> getAll();

    void remove(Long id);
}
